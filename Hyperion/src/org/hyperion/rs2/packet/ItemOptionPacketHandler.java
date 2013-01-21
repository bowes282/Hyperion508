package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.action.impl.BuryingAction;
import org.hyperion.rs2.action.impl.BuryingAction.Bone;
import org.hyperion.rs2.action.impl.EatingAction;
import org.hyperion.rs2.action.impl.EatingAction.Food;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;

/**
 * Remove item options.
 *
 * @author Graham Edgecombe
 */
public class ItemOptionPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ItemOptionPacketHandler.class.getName());

    /**
     * Option 1
     */
    private static final int ITEM_OPTION_1 = 203;
    /**
     * Option 2
     */
    private static final int ITEM_OPTION_2 = 152;

    /**
     * Select Item
     */
    private static final int ITEM_SELECT = 220;

    /**
     * Switching Items
     */
    private static final int SWITCH_ITEMS = 167;

    /**
     * Switching Items 2
     */
    private static final int SWITCH_ITEMS_2 = 179;

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            /*
             * Item option 1.
			 */
            case ITEM_OPTION_1:
                handleItemOption1(player, packet);
                break;

            /**
             * Item option 2
             */
            case ITEM_OPTION_2:
                handleItemOption2(player, packet);
                break;

            /**
             * Item action 1
             */
            case ITEM_SELECT:
                handleItemSelect(player, packet);
                break;

            /**
             * Switch items
             */
            case SWITCH_ITEMS:
                switchItems(player, packet);
                break;

            /*
             * Transfer items between two interfaces.
			 */
            case SWITCH_ITEMS_2:
                switchItems2(player, packet);
                break;
        }
        return new DefaultPacket();
    }

    /**
     * Handles item action 1.
     *
     * @param player The player
     * @param packet The packet
     */
    private void handleItemOption1(Player player, Packet packet) {
        final int slot = packet.getLEShortA();
        final int interfaceId = packet.getShort();
        packet.getShort();
        final int itemId = packet.getShort();

        if (slot < 0 || itemId < 0) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Item Option 1",
                new Object[]{"slot=" + slot, "interface=" + interfaceId, "itemId=" + itemId});

        switch (interfaceId) {

            /**
             * Unequip item
             */
            case 387:
                final Item equipItem = player.getEquipment().get(slot);

                if (slot < Equipment.SIZE && equipItem != null) {
                    if (!player.getInventory().add(equipItem)) {
                        break;
                    }
                    player.getEquipment().set(slot, null);
                    player.getActionSender().sendTabs();
                }
                break;

            /**
             * Inventory
             */
            case 149:
                final Item inventoryItem = player.getInventory().get(slot);

                if (slot < 0 || slot >= Inventory.SIZE || inventoryItem == null) {
                    return;
                }
                if (inventoryItem.getId() != itemId) {
                    return;
                }
                break;
            default:
                logger.info("Unhandled item option 1 [id=" + itemId + " slot=" + slot + " interface=" + interfaceId + "]");
                break;
        }
    }

    /**
     * Handles item option 2
     *
     * @param player The player
     * @param packet The packet
     */
    private void handleItemOption2(Player player, Packet packet) {
        int slot = packet.getLEShortA() & 0xFFFF;
        int id = packet.getShortA() & 0xFFFF;
        int interfaceSet = packet.getInt();
        int interfaceId = interfaceSet >> 16;
        if (interfaceId == 149) {
            if (slot < 0 || slot >= Inventory.SIZE || player.getInventory().get(slot) == null) {
                return;
            }
            if (player.getInventory().get(slot).getId() != id) {
                return;
            }
            // ScriptManager.getInstance().invoke("item_option_2_" + id, player, slot);
        } else {
            logger.info("Unhandled item option 2 [id=" + id + " slot=" + slot + " interface=" + interfaceId + "]");
        }
    }

    /**
     * Handles item action 1.
     *
     * @param player The player
     * @param packet The packet
     */
    private void handleItemSelect(Player player, Packet packet) {
        int interfaceSet = packet.getLEInt();
        int interfaceId = interfaceSet >> 16;
        int id = packet.getLEShort() & 0xFFFF;
        int slot = packet.getShortA() & 0xFFFF;

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Item Action 1",
                new Object[]{"slot=" + slot, "interface=" + interfaceId, "itemId=" + id});

        switch (interfaceId) {
            case Inventory.INTERFACE:
                if (slot < 0 || slot >= Inventory.SIZE || player.getInventory().get(slot) == null) {
                    return;
                }
                if (player.getInventory().get(slot).getId() != id) {
                    return;
                }

                final Bone bone = Bone.forId(id);
                if (bone != null) {
                    player.getActionQueue().addAction(new BuryingAction(player, bone));
                }

                final Food food = Food.forId(id);
                if (food != null) {
                    player.getActionQueue().addAction(new EatingAction(player, food));
                }
                break;
            default:
                logger.info("Unhandled item action 1 [id=" + id + " slot=" + slot + " interface=" + interfaceId + "]");
                break;
        }
    }

    /**
     * Handles switching items in inventory
     *
     * @param player The player
     * @param packet The packet
     */
    private void switchItems(Player player, Packet packet) {
        int toId = packet.getLEShortA();
        packet.getByte();
        int fromId = packet.getLEShortA();
        packet.getShort();
        int interfaceId = packet.getByte() & 0xFF;
        packet.getByte();

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Switch Items",
                new Object[]{"toId=" + toId, "fromId=" + fromId, "interface=" + interfaceId});

        switch (interfaceId) {
            /**
             * Switching items in inventory
             */
            case 149:
                if (fromId < 0 || fromId >= Inventory.SIZE || toId < 0 || toId >= Inventory.SIZE) {
                    break;
                }
                Item temp = player.getInventory().get(fromId);
                Item temp2 = player.getInventory().get(toId);
                player.getInventory().set(fromId, temp2);
                player.getInventory().set(toId, temp);
                break;
            default:
                logger.info("Unhandled switch items [toId=" + toId + " fromId=" + fromId + " interface=" + interfaceId + "]");
                break;
        }
    }

    /**
     * Handles switching items between interfaces
     *
     * @param player The player
     * @param packet The packet
     */
    private void switchItems2(Player player, Packet packet) {
        int interfaceId = packet.getInt() >> 16;
        packet.getInt();
        int fromId = packet.getShort() & 0xFFFF;
        int toId = packet.getLEShort() & 0xFFFF;

        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Switch Items 2",
                new Object[]{"toId=" + toId, "fromId=" + fromId, "interface=" + interfaceId});

        switch (interfaceId) {

            /**
             * Bank
             */
            case 762:
                if (fromId < 0 || fromId >= Bank.SIZE || toId < 0 || toId >= Bank.SIZE) {
                    break;
                }
                Item temp = player.getBank().get(fromId);
                Item temp2 = player.getBank().get(toId);
                player.getBank().set(fromId, temp2);
                player.getBank().set(toId, temp);
                player.getBank().fireItemsChanged();
                break;

            /**
             * Inventory
             */
            case 763:
                if (fromId < 0 || fromId >= Inventory.SIZE || toId < 0 || toId >= Inventory.SIZE) {
                    break;
                }
                temp = player.getInventory().get(fromId);
                temp2 = player.getInventory().get(toId);
                player.getInventory().set(fromId, temp2);
                player.getInventory().set(toId, temp);
                player.getBank().fireItemsChanged();
                break;
        }
    }
}

