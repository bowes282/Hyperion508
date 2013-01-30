package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.task.Task;

import java.util.Iterator;

/**
 * A task which creates and sends the NPC update block.
 *
 * @author Graham Edgecombe
 */
public class NPCUpdateTask implements Task {

    /**
     * The player.
     */
    private Player player;

    /**
     * Creates an npc update task.
     *
     * @param player The player.
     */
    public NPCUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute(GameEngine context) {        /*
         * The update block holds the update masks and data, and is written
         * after the main block.
         */
        PacketBuilder updateBlock = new PacketBuilder();

        /*
         * The main packet holds information about adding, moving and removing
         * NPCs.
         */
        PacketBuilder packet = new PacketBuilder(222, Packet.Type.VARIABLE_SHORT);
        packet.startBitAccess();

        /*
         * Write the current size of the npc list.
         */
        packet.putBits(8, player.getLocalNPCs().size());

        /*
         * Iterate through the local npc list.
         */
        for (Iterator<NPC> it$ = player.getLocalNPCs().iterator(); it$.hasNext();) {
            /*
             * Get the next NPC.
             */
            NPC npc = it$.next();

            /*
             * If the NPC should still be in our list.
             */
            if (World.getWorld().getNPCs().contains(npc) && !npc.isTeleporting() && npc.getLocation().isWithinDistance(player.getLocation())) {
                /*
                 * Update the movement.
                 */
                updateNPCMovement(packet, npc);

                /*
                 * Check if an update is required, and if so, send the update.
                 */
                if (npc.getUpdateFlags().isUpdateRequired()) {
                    updateNPC(updateBlock, npc);
                }
            } else {
                /*
                 * Otherwise, remove the NPC from the list.
                 */
                it$.remove();

                /*
                 * Tell the client to remove the NPC from the list.
                 */
                packet.putBits(1, 1);
                packet.putBits(2, 3);
            }
        }

        /*
         * Loop through all NPCs in the world.
         */
        for (NPC npc : World.getWorld().getRegionManager().getLocalNpcs(player)) {
            /*
             * Check if there is room left in the local list.
             */
            if (player.getLocalNPCs().size() >= 255) {
                /*
                 * There is no more room left in the local list. We cannot add
                 * more NPCs, so we just ignore the extra ones. They will be
                 * added as other NPCs get removed.
                 */
                break;
            }

            /*
             * If they should not be added ignore them.
             */
            if (npc == null || player.getLocalNPCs().contains(npc) || !npc.getLocation().isWithinDistance(player.getLocation())) {
                continue;
            }

            /*
             * Add the npc to the local list if it is within distance.
             */
            player.getLocalNPCs().add(npc);

            /*
             * Add the npc in the packet.
             */
            addNewNPC(packet, npc);

            /*
             * Check if an update is required.
             */
            if (npc.getUpdateFlags().isUpdateRequired()) {

                /*
                 * If so, update the npc.
                 */
                updateNPC(updateBlock, npc);

            }
        }

        /*
         * Check if the update block isn't empty.
         */
        if (!updateBlock.isEmpty()) {
            /*
             * If so, put a flag indicating that an update block follows.
             */
            packet.putBits(15, 32767);
            packet.finishBitAccess();

            /*
             * And append the update block.
             */
            packet.put(updateBlock.toPacket().getPayload());
        } else {
            /*
             * Terminate the packet normally.
             */
            packet.finishBitAccess();
        }

        /*
         * Write the packet.
         */
        player.write(packet.toPacket());
    }

    /**
     * Adds a new NPC.
     *
     * @param packet The main packet.
     * @param npc The npc to add.
     */
    private void addNewNPC(PacketBuilder packet, NPC npc) {
        packet.putBits(15, npc.getIndex());
        packet.putBits(14, npc.getDefinition().getId());
        packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        int yPos = npc.getLocation().getY() - player.getLocation().getY();
        int xPos = npc.getLocation().getX() - player.getLocation().getX();
        packet.putBits(5, yPos);
        packet.putBits(5, xPos);
        packet.putBits(3, npc.getDirection());//NPC DIRECTION
        packet.putBits(1, 1);
    }

    /**
     * Update an NPC's movement.
     *
     * @param packet The main packet.
     * @param npc The npc.
     */
    private void updateNPCMovement(PacketBuilder packet, NPC npc) {
        //if(npc.getSprites().getSecondarySprite() == -1) {
        if (npc.getSprites().getPrimarySprite() == -1) {
            if (npc.getUpdateFlags().isUpdateRequired()) {
                packet.putBits(1, 1);
                packet.putBits(2, 0);
            } else {
                packet.putBits(1, 0);
            }
        } else {
            packet.putBits(1, 1);
            packet.putBits(2, 1);
            packet.putBits(3, npc.getSprites().getPrimarySprite());
            packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        }
    }

    /**
     * Update an NPC.
     *
     * @param packet The update block.
     * @param npc The npc.
     */
    private void updateNPC(PacketBuilder packet, NPC npc) {
        /*
         * Calculate the mask.
         */
        int mask = 0x0;
        final UpdateFlags flags = npc.getUpdateFlags();
        if (flags.get(UpdateFlag.FACE_ENTITY)) {
            mask |= 0x10;
        }
        if (flags.get(UpdateFlag.ANIMATION)) {
            mask |= 0x1;
        }
        if (flags.get(UpdateFlag.GRAPHICS)) {
            mask |= 0x2;
        }
        if (flags.get(UpdateFlag.HIT_2)) {
            mask |= 0x20;
        }
        if (flags.get(UpdateFlag.HIT)) {
            mask |= 0x4;
        }

        /*if(flags.get(UpdateFlag.FORCED_CHAT)) {
         mask |= 0x20;
         }
         if(flags.get(UpdateFlag.FACE_COORDINATE)) {
         mask |= 0x200;
         }
         if (flags.get(UpdateFlag.FACE_COORDINATE)) {
         mask |= 0x40;
         }	
         if(flags.get(UpdateFlag.FORCED_MOVEMENT)) {
         mask |= 0x80;
         }*/

        /*if(flags.get(UpdateFlag.TRANSFORM)) {//Doesn't exist in 459 as far as I know?
         mask |= 0x2;
         }*/


        /*
         * And write the mask.
         */
        packet.put((byte) mask);
        if (flags.get(UpdateFlag.FACE_ENTITY)) {
            Entity entity = npc.getInteractingEntity();
            packet.putShort(entity == null ? -1 : entity.getClientIndex());
        }
        if (flags.get(UpdateFlag.ANIMATION)) {
            packet.putShortA(npc.getCurrentAnimation().getId());
            packet.put((byte) npc.getCurrentAnimation().getDelay());
        }
        if (flags.get(UpdateFlag.GRAPHICS)) {
            packet.putShortA(npc.getCurrentGraphic().getId());
            packet.putInt2(npc.getCurrentGraphic().getDelay());
        }
        if (flags.get(UpdateFlag.HIT_2)) {
            packet.put((byte) npc.getDamage().getHitDamage2());
            packet.putByteS((byte) npc.getDamage().getHitType2());
        }
        if (flags.get(UpdateFlag.HIT)) {
            packet.put((byte) npc.getDamage().getHitDamage1());
            packet.put((byte) npc.getDamage().getHitType1());
            int ratio = npc.getHealth() * 255 / npc.getHealth();
            packet.putByteS((byte) ratio);
        }
        /*if(flags.get(UpdateFlag.FORCED_MOVEMENT)) {
		
         }*/
        /*if(flags.get(UpdateFlag.FORCED_CHAT)) {
         packet.putString(npc.getForceChatText());
         }
         if(flags.get(UpdateFlag.FACE_COORDINATE)) {
         Location loc = npc.getFaceLocation();
         if(loc == null) {
         packet.putShortA(0);
         packet.putShort(0);
         } else {
         packet.putShortA(loc.getX() * 2 + 1);
         packet.putShort(loc.getY() * 2 + 1);
         }
         }
         /*if(flags.get(UpdateFlag.TRANSFORM)) {//Doesn't exist in 459 as far as I know?
			
         }*/

    }
}
