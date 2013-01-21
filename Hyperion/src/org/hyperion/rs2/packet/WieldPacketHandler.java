package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

public class WieldPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        packet.getInt();
        int id = packet.getLEShort();
        int slot = packet.getByte() & 0xFF;
        packet.getByte();

        if (slot >= 0 && slot < Inventory.SIZE) {
            final Item item = player.getInventory().get(slot);
            if (item != null && item.getId() == id) {
                final EquipmentType type = Equipment.getType(item);
                Item oldEquip = null;
                final boolean stackable = false;
                if (player.getEquipment().isSlotUsed(type.getSlot()) && !stackable) {
                    oldEquip = player.getEquipment().get(type.getSlot());
                    player.getEquipment().set(type.getSlot(), null);
                }
                player.getInventory().set(slot, null);
                if (type == EquipmentType.WEAPON) {
                    if (Equipment.isTwoHanded(item.getDefinition()) && player.getEquipment().get(5) != null) {
                        if (!player.getInventory().add(player.getEquipment().get(5))) {
                            player.getInventory().add(item);
                            return new DefaultPacket();
                        }
                        player.getEquipment().set(5, null);
                    }
                } else if (type == EquipmentType.SHIELD) {
                    if (player.getEquipment().get(3) != null && Equipment.isTwoHanded(player.getEquipment().get(3).getDefinition())) {
                        if (!player.getInventory().add(player.getEquipment().get(3))) {
                            player.getInventory().add(item);
                            return new DefaultPacket();
                        }
                        player.getEquipment().set(3, null);
                    }
                }
                if (oldEquip != null) {
                    player.getInventory().add(oldEquip);
                }
                if (!stackable) {
                    player.getEquipment().set(type.getSlot(), item);
                } else {
                    player.getEquipment().add(item);
                }
            }

        }
        return new DefaultPacket();
    }
}
