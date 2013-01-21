package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.NPCDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Handles examining.
 *
 * @author Graham
 */
public class ExaminePacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            /**
             * Examine Item
             */
            case 38:
                examineItem(player, packet);
                break;
            /**
             * Examine Npc
             */
            case 88:
                examineNpc(player, packet);
                break;
            /**
             * Examine Object
             */
            case 84:
                examineObject(player, packet);
                break;
        }
        return new DefaultPacket();
    }

    /**
     * Handles examining items
     *
     * @param player The player
     * @param packet The packet
     */
    private void examineItem(Player player, Packet packet) {
        final int id = packet.getLEShortA();
        player.getActionSender().sendMessage(ItemDefinition.forId(id).getDescription());
    }

    /**
     * Handles examining an NPC
     *
     * @param player The player
     * @param packet The packet
     */
    private void examineNpc(Player player, Packet packet) {
        final int id = packet.getShort();
        NPCDefinition def = NPCDefinition.forId(id);
        player.getActionSender().sendMessage(def != null ? def.getDescription() : "it's an NPC!");
    }

    /**
     * Handles examining an object
     *
     * @param player The player
     * @param packet The packet
     */
    private void examineObject(Player player, Packet packet) {
        final int id = packet.getShort();
        player.getActionSender().sendMessage("It's an object!");
    }
}
