package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.task.Task;
import org.hyperion.rs2.util.ChatUtils;

import java.util.Iterator;

/**
 * A task which creates and sends the player update block.
 *
 * @author Graham Edgecombe
 */
public class PlayerUpdateTask implements Task {

    /**
     * The player.
     */
    private Player player;

    /**
     * Creates an update task.
     *
     * @param player The player.
     */
    public PlayerUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute(GameEngine context) {
        /*
         * If the map region changed send the new one. We do this immediately as
         * the client can begin loading it before the actual packet is received.
         */
        if (player.isMapRegionChanging()) {
            player.getActionSender().sendMapRegion();
        }

        /*
         * The update block packet holds update blocks and is send after the
         * main packet.
         */
        final PacketBuilder updateBlock = new PacketBuilder();

        /*
         * The main packet is written in bits instead of bytes and holds
         * information about the local list, players to add and remove, movement
         * and which updates are required.
         */
        final PacketBuilder updatePacket = new PacketBuilder(216, Packet.Type.VARIABLE_SHORT);
        updatePacket.startBitAccess();

        /*
         * Updates this player.
         */
        updateThisPlayerMovement(updatePacket);
        updatePlayer(updateBlock, player, false);

        /*
         * Write the current size of the player list.
         */
        updatePacket.putBits(8, player.getLocalPlayers().size());

        /*
         * Iterate through the local player list.
         */
        for (final Iterator<Player> it$ = player.getLocalPlayers().iterator(); it$.hasNext();) {
            /*
             * Get the next player.
             */
            final Player otherPlayer = it$.next();

            /*
             * If the player should still be in our list.
             */
            if (World.getWorld().getPlayers().contains(otherPlayer)
                    && !otherPlayer.isTeleporting()
                    && otherPlayer.getLocation().isWithinDistance(
                    player.getLocation(), 16)) {

                /*
                 * Update the movement.
                 */
                updatePlayerMovement(updatePacket, otherPlayer);

                /*
                 * Check if an update is required, and if so, send the update.
                 */
                if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
                    updatePlayer(updateBlock, otherPlayer, false);
                }
            } else {
                /*
                 * Otherwise, remove the player from the list.
                 */
                it$.remove();

                /*
                 * Tell the client to remove the player from the list.
                 */
                updatePacket.putBits(1, 1);
                updatePacket.putBits(2, 3);
            }
        }
        /*
         * Loop through every player.
         */
        for (final Player otherPlayer : World.getWorld().getRegionManager().getLocalPlayers(player)) {
            /*
             * Check if there is room left in the local list.
             */
            if (player.getLocalPlayers().size() >= 255) {
                /*
                 * There is no more room left in the local list. We cannot add
                 * more players, so we just ignore the extra ones. They will be
                 * added as other players get removed.
                 */
                break;
            }

            /*
             * If they should not be added ignore them.
             */
            if (otherPlayer == player || player.getLocalPlayers().contains(otherPlayer)) {
                continue;
            }

            /*
             * Add the player to the local list if it is within distance.
             */
            player.getLocalPlayers().add(otherPlayer);

            /*
             * Add the player in the packet.
             */
            addNewPlayer(updatePacket, otherPlayer);
            /*
             * Update the player, forcing the appearance flag.
             */
            updatePlayer(updateBlock, otherPlayer, true);
        }

        /*
         * Check if the update block is not empty.
         */
        if (!updateBlock.isEmpty()) {
            /*
             * Write a magic id indicating an update block follows.
             */
            updatePacket.putBits(11, 2047);
            updatePacket.finishBitAccess();
            /*
             * Add the update block at the end of this packet.
             */
            updatePacket.put(updateBlock.toPacket().getPayload());
        } else {
            /*
             * Terminate the packet normally.
             */
            updatePacket.finishBitAccess();
        }
        /*
         * Write the packet.
         */
        player.write(updatePacket.toPacket());
    }

    /**
     * Adds a new player.
     *
     * @param packet The packet.
     * @param otherPlayer The player.
     */
    public void addNewPlayer(PacketBuilder packet, Player otherPlayer) {
        packet.putBits(11, otherPlayer.getIndex());
        int yPos = otherPlayer.getLocation().getY() - player.getLocation().getY();
        int xPos = otherPlayer.getLocation().getX() - player.getLocation().getX();
        if (xPos < 0) {
            xPos += 32;
        }
        if (yPos < 0) {
            yPos += 32;
        }
        packet.putBits(5, xPos);
        packet.putBits(1, 1);
        packet.putBits(3, 1);
        packet.putBits(1, 1);
        packet.putBits(5, yPos);
    }

    /**
     * Updates a non-this player's movement.
     *
     * @param packet The packet.
     * @param otherPlayer The player.
     */
    public void updatePlayerMovement(PacketBuilder packet, Player otherPlayer) {
        if (otherPlayer.getSprites().getPrimarySprite() == -1) {
            if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
                packet.putBits(1, 1);
                packet.putBits(2, 0);
            } else {
                packet.putBits(1, 0);
            }
        } else if (otherPlayer.getSprites().getSecondarySprite() == -1) {
            packet.putBits(1, 1);
            packet.putBits(2, 1);
            packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());
            packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        } else {
            packet.putBits(1, 1);
            packet.putBits(2, 2);
            packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());
            packet.putBits(3, otherPlayer.getSprites().getSecondarySprite());
            packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        }
    }

    /**
     * Updates this player's movement.
     *
     * @param packet The packet.
     */
    private void updateThisPlayerMovement(final PacketBuilder packet) {
        if (player.isTeleporting() || player.isMapRegionChanging()) {
            packet.putBits(1, 1);
            packet.putBits(2, 3);
            packet.putBits(7, player.getLocation().getLocalX(player.getLastKnownRegion()));
            packet.putBits(1, 1);
            packet.putBits(2, player.getLocation().getZ());
            packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
            packet.putBits(7, player.getLocation().getLocalY(player.getLastKnownRegion()));
        } else {
            if (player.getSprites().getPrimarySprite() == -1) {
                packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                if (player.getUpdateFlags().isUpdateRequired()) {
                    packet.putBits(2, 0);
                }
            } else {
                if (player.getSprites().getSecondarySprite() != -1) {
                    packet.putBits(1, 1);
                    packet.putBits(2, 2);
                    packet.putBits(3, player.getSprites().getPrimarySprite());
                    packet.putBits(3, player.getSprites().getSecondarySprite());
                    packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                } else {
                    packet.putBits(1, 1);
                    packet.putBits(2, 1);
                    packet.putBits(3, player.getSprites().getPrimarySprite());
                    packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                }
            }
        }
    }

    /**
     * Updates a player.
     *
     * @param packet The packet.
     * @param otherPlayer The other player.
     * @param forceAppearance The force appearance flag.
     */
    public void updatePlayer(PacketBuilder packet, Player otherPlayer, boolean forceAppearance) {
        /*
         * If no update is required and we don't have to force an appearance
         * update, don't write anything.
         */
        if (!otherPlayer.getUpdateFlags().isUpdateRequired() && !forceAppearance) {
            return;
        }

        /*
         * We can used the cached update block!
         */
        synchronized (otherPlayer) {
          /*  if (otherPlayer.hasCachedUpdateBlock() && otherPlayer != player && !forceAppearance) {
                packet.put(otherPlayer.getCachedUpdateBlock().getPayload().flip());
                return;
            }*/
            /*
             * We have to construct and cache our own block.
             */
            PacketBuilder block = new PacketBuilder();

            /*
             * Calculate the bitmask.
             */
            int mask = 0x0;
            final UpdateFlags flags = otherPlayer.getUpdateFlags();
            if (flags.get(UpdateFlag.FACE_ENTITY)) {
                mask |= 0x20;
            }
            if (flags.get(UpdateFlag.GRAPHICS)) {
                mask |= 0x400;
            }
            if (flags.get(UpdateFlag.CHAT)) {
                mask |= 0x8;
            }
            if (flags.get(UpdateFlag.ANIMATION)) {
                mask |= 0x1;
            }
            if (flags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
                mask |= 0x80;
            }
            if (flags.get(UpdateFlag.HIT)) {
                mask |= 0x2;
            }
            if (flags.get(UpdateFlag.HIT_2)) {
                mask |= 0x200;
            }

            /*
             * Check if the bitmask would overflow a byte.
             */
            if (mask >= 0x100) {
                /*
                 * Write it as a short and indicate we have done so.
                 */
                mask |= 0x10;
                block.put((byte) (mask & 0xFF));
                block.put((byte) (mask >> 8));
            } else {
                /*
                 * Write it as a byte.
                 */
                block.put((byte) (mask));
            }
            /*
             * Append the appropriate updates.
             */
            if (flags.get(UpdateFlag.FACE_ENTITY)) {
                Entity entity = otherPlayer.getInteractingEntity();
                block.putShort(entity == null ? -1 : entity.getClientIndex());
            }
            if (flags.get(UpdateFlag.GRAPHICS)) {
                appendGraphicsUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlag.CHAT)) {
                appendChatUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlag.ANIMATION)) {
                appendAnimationUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
                appendPlayerAppearanceUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlag.HIT)) {
                appendHitUpdate(otherPlayer, block);
            }
            if (flags.get(UpdateFlag.HIT_2)) {
                appendHit2Update(otherPlayer, block);
            }
            /*
             * Convert the block builder to a packet.
             */
            Packet blockPacket = block.toPacket();

            /*
             * Now it is over, cache the block if we can.
             */
            if (otherPlayer != player && !forceAppearance) {
                otherPlayer.setCachedUpdateBlock(blockPacket);
            }

            /*
             * And finally append the block at the end.
             */
            packet.put(blockPacket.getPayload());
        }
    }

    private static void appendHitUpdate(Player p, PacketBuilder updateBlock) {
        updateBlock.putByteS((byte) p.getDamage().getHitDamage1());
        updateBlock.putByteS((byte) p.getDamage().getHitType1());
        final int hpRatio = p.getSkills().getLevel(3) * 255 / p.getSkills().getLevelForExperience(3);
        updateBlock.putByteS((byte) hpRatio);
    }

    private static void appendHit2Update(final Player p, final PacketBuilder updateBlock) {
        updateBlock.putByteS((byte) p.getDamage().getHitDamage2());
        updateBlock.putByteA(p.getDamage().getHitType2());
    }

    /**
     * Appends an animation update.
     *
     * @param block The update block.
     * @param otherPlayer The player.
     */
    private void appendAnimationUpdate(PacketBuilder block, Player otherPlayer) {
       //TODO: CHEAP FIX!! Caused errors if you attacked someone and clicked away
        Animation anim = otherPlayer.getCurrentAnimation() != null ? otherPlayer.getCurrentAnimation() : Animation.create(-1,0);
        block.putShort(anim.getId());
        block.putByteS((byte) anim.getDelay());
    }

    /**
     * Appends a graphics update.
     *
     * @param block The update block.
     * @param otherPlayer The player.
     */
    private void appendGraphicsUpdate(PacketBuilder block, Player otherPlayer) {
        block.putShort(otherPlayer.getCurrentGraphic().getId());
        block.putInt1(otherPlayer.getCurrentGraphic().getDelay());
    }

    /**
     * Appends a chat text update.
     *
     * @param block The packet.
     * @param otherPlayer The player.
     */
    private void appendChatUpdate(PacketBuilder block, Player otherPlayer) {
        final ChatMessage chatMessage = otherPlayer.getCurrentChatMessage();
        block.putShortA(chatMessage.getEffects());
        block.putByteC(otherPlayer.getRights().toInteger());
        byte[] chatStr = new byte[256];
        chatStr[0] = (byte) chatMessage.getText().length();
        int offset = 2 + ChatUtils.encryptPlayerChat(chatStr, 0, 1, chatMessage.getText().length(), chatMessage.getText().getBytes());
        block.putByteC(offset);
        block.put(chatStr, 0, offset);
    }

    /**
     * Appends an appearance update.
     *
     * @param packet The packet.
     * @param otherPlayer The player.
     */
    private void appendPlayerAppearanceUpdate(PacketBuilder packet, Player otherPlayer) {
        Appearance app = otherPlayer.getAppearance();
        Container eq = otherPlayer.getEquipment();

        PacketBuilder playerProps = new PacketBuilder();
        playerProps.put((byte) app.getGender()); // gender
        if ((app.getGender() & 0x2) == 2) {
            playerProps.put((byte) 0);
            playerProps.put((byte) 0);
        }
        playerProps.put((byte) -1); // skull icon
        playerProps.put((byte) -1); // prayer icon

        if (!otherPlayer.getAppearance().isNpc()) {
            for (int i = 0; i < 4; i++) {
                if (eq.isSlotUsed(i)) {
                    playerProps.putShort((short) 32768 + eq.get(i).getDefinition().getEquipId());
                } else {
                    playerProps.put((byte) 0);
                }
            }
            if (eq.isSlotUsed(Equipment.SLOT_CHEST)) {
                playerProps.putShort((short) 32768 + eq.get(Equipment.SLOT_CHEST).getDefinition().getEquipId());
            } else {
                playerProps.putShort((short) 0x100 + app.getChest()); // chest
            }
            if (eq.isSlotUsed(Equipment.SLOT_SHIELD)) {
                playerProps.putShort((short) 32768 + eq.get(Equipment.SLOT_SHIELD).getDefinition().getEquipId());
            } else {
                playerProps.put((byte) 0);
            }
            final Item chest = eq.get(Equipment.SLOT_CHEST);
            if (chest != null) {
                if (!Equipment.is(EquipmentType.PLATEBODY, chest)) {
                    playerProps.putShort((short) 0x100 + app.getArms());
                } else {
                    playerProps.putShort((short) 32768 + chest.getDefinition().getEquipId());
                }
            } else {
                playerProps.putShort((short) 0x100 + app.getArms());
            }
            if (eq.isSlotUsed(Equipment.SLOT_BOTTOMS)) {
                playerProps.putShort((short) 32768 + eq.get(Equipment.SLOT_BOTTOMS).getDefinition().getEquipId());
            } else {
                playerProps.putShort((short) 0x100 + app.getLegs());
            }
            final Item helm = eq.get(Equipment.SLOT_HELM);
            if (helm != null) {
                if (!Equipment.is(EquipmentType.FULL_HELM, helm)
                        && !Equipment.is(EquipmentType.FULL_MASK, helm)) {
                    playerProps.putShort((short) 0x100 + app.getHead());
                } else {
                    playerProps.put((byte) 0);
                }
            } else {
                playerProps.putShort((short) 0x100 + app.getHead());
            }
            if (eq.isSlotUsed(Equipment.SLOT_GLOVES)) {
                playerProps.putShort((short) 32768 + eq.get(Equipment.SLOT_GLOVES).getDefinition().getEquipId());
            } else {
                playerProps.putShort((short) 0x100 + app.getHands());
            }
            if (eq.isSlotUsed(Equipment.SLOT_BOOTS)) {
                playerProps.putShort((short) 32768 + eq.get(Equipment.SLOT_BOOTS).getDefinition().getEquipId());
            } else {
                playerProps.putShort((short) 0x100 + app.getFeet());
            }
            boolean fullHelm = false;
            if (helm != null) {
                fullHelm = !Equipment.is(EquipmentType.FULL_HELM, helm);
            }
            if (fullHelm || app.getGender() == 1) {
                playerProps.put((byte) 0);
            } else {
                playerProps.putShort((short) 0x100 + app.getBeard());
            }
        } else {
            playerProps.putShort(-1);
            playerProps.putShort(otherPlayer.getAppearance().getNpcIndex());
        }

        playerProps.put((byte) app.getHairColour()); // hairc
        playerProps.put((byte) app.getTorsoColour()); // torsoc
        playerProps.put((byte) app.getLegColour()); // legc
        playerProps.put((byte) app.getFeetColour()); // feetc
        playerProps.put((byte) app.getSkinColour()); // skinc

        Item weapon = eq.get(Equipment.SLOT_WEAPON);

        playerProps.putShort((short) Equipment.standAnimation(weapon)); // stand
        playerProps.putShort((short) 0x337); // stand turn
        playerProps.putShort((short) Equipment.walkAnimation(weapon)); // walk
        playerProps.putShort((short) 0x334); // turn 180
        playerProps.putShort((short) 0x335); // turn 90 cw
        playerProps.putShort((short) 0x336); // turn 90 ccw
        playerProps.putShort((short) Equipment.runAnimation(weapon)); // run


        playerProps.putLong(otherPlayer.getNameAsLong()); // player name
        playerProps.put((byte) otherPlayer.getSkills().getCombatLevel()); // combat level
        playerProps.putShort(0); // (skill-level instead of combat-level) otherPlayer.getSkills().getTotalLevel()); // total level
        Packet propsPacket = playerProps.toPacket();
        packet.put((byte) (propsPacket.getLength() & 0xff));
        packet.putBytes(propsPacket.getPayload(), 0, propsPacket.getLength());
    }
}
