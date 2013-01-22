package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Whenever a Client attempts to drop an item, this class will be executed. It
 * creates a new GroundItem and removes the item from the Player's Inventory.
 *
 * @author Bloodraider
 */
public final class DropItemPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        packet.getInt();
        int slot = packet.getLEShortA() & 0xFFFF;
        @SuppressWarnings("unused")
        int id = packet.getLEShort() & 0xFFFF;

        Item item = player.getInventory().get(slot);

        GroundItem.create(player, item);
        player.getInventory().remove(slot, item);
        return new DefaultPacket();
    }
}