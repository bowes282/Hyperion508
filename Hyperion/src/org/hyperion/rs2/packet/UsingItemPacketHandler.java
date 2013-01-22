package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;

public class UsingItemPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(UsingItemPacketHandler.class.getName());
    /**
     * Item on Object
     */
    private static final int ITEM_ON_OBJECT = 224;
    /**
     * Item on Item
     */
    private static final int ITEM_ON_ITEM = 40;
    /**
     * Item on Player
     */
    private static final int ITEM_ON_PLAYER = 131;
    /**
     * Item on Npc
     */
    private static final int ITEM_ON_NPC = 12;

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            /**
             * Item on item
             */
            case ITEM_ON_ITEM:
                itemOnItem(player, packet);
                break;
            /**
             * Item on object
             */
            case ITEM_ON_OBJECT:
                itemOnObject(player, packet);
                break;
            /**
             * Item on player
             */
            case ITEM_ON_PLAYER:
                itemOnPlayer(player, packet);
                break;
            /**
             * Item on npc
             */
            case ITEM_ON_NPC:
                itemOnNPC(player, packet);
                break;
        }
        return new DefaultPacket();

    }

    /**
     * Handles item on player
     *
     * @param player The player
     * @param packet The packet
     */
    private void itemOnPlayer(Player player, Packet packet) {
        packet.getInt();// junk
        int playerId = packet.getShort() & 0xFFFF;
        int slot = packet.getLEShortA() & 0xFFFF;
        int id = packet.getShortA() & 0xFFFF;
        Player other = (Player) World.getWorld().getPlayers().get(playerId);

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Item on Player",
                new Object[]{"id=" + id, "slot=" + slot, "playerId=" + playerId});

        if (slot < 0 || slot >= Inventory.SIZE) {
            return;
        }
        Item item = player.getInventory().get(slot);
        // ScriptManager.getInstance().invoke("item_on_player_" + id, player, slot, other);
    }

    /**
     * Handles item on npc
     *
     * @param player The player
     * @param packet The packet
     */
    private void itemOnNPC(Player player, Packet packet) {
        packet.getInt(); // junk
        int npcId = packet.getShort() & 0xFFFF;
        int slot = packet.getLEShortA() & 0xFFFF;
        int id = packet.getShortA() & 0xFFFF;
        NPC npc = (NPC) World.getWorld().getNPCs().get(npcId);

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Item on Npc",
                new Object[]{"id=" + id, "slot=" + slot, "npcId=" + npcId});

        if (slot < 0 || slot >= Inventory.SIZE) {
            return;
        }
        Item item = player.getInventory().get(slot);
        // ScriptManager.getInstance().invoke("item_on_npc_" + id, player, slot, npc);
    }

    /**
     * Handles item on object
     *
     * @param player The player
     * @param packet The packet
     */
    private void itemOnObject(Player player, Packet packet) {
        int y = packet.getLEShort();
        int itemId = packet.getShort() & 0xFFFF;
        packet.getShort(); // junk
        packet.getShort(); // junk
        packet.getShort(); // junk
        int objectId = packet.getShortA() & 0xFFFF;
        int x = packet.getShort() & 0xFFFF;
        int slot = player.getInventory().getSlotById(itemId);
        if (slot == -1) {
            return;
        }

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Item on Object",
                new Object[]{"itemId=" + itemId, "objectId=" + objectId, "slot=" + slot, "x=" + x, "y=" + y});
        // ScriptManager.getInstance().invoke("item_on_object_" + itemId + "_" + objectId, player, slot, x, y);
    }

    /**
     * Handles item on item
     *
     * @param player The player
     * @param packet The packet
     */
    private void itemOnItem(Player player, Packet packet) {
        int usedWith = packet.getLEShort();
        int itemUsed = packet.getShortA();
        packet.getInt();
        packet.getInt();
        int slot = packet.getShortA();
        int withSlot = packet.getShortA();
        if (slot < 0 || slot >= Inventory.SIZE) {
            return;
        }
        if (withSlot < 0 || withSlot >= Inventory.SIZE) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Item on Item",
                new Object[]{"itemUsed=" + itemUsed, "usedwith=" + usedWith, "slot=" + slot, "withSlot=" + withSlot});
        //  ScriptManager.getInstance().invoke("item_on_item_" + itemUsed + "_" + usedWith, player, slot, withSlot);
    }
}
