package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;

public class Bank {

    /**
     * The bank size.
     */
    public static final int SIZE = 800;

    /**
     * Opens the bank for the specified player.
     *
     * @param player The player to open the bank for.
     */
    public static void open(Player player) {
        player.getBank().shift();
        player.getActionSender().sendConfigTwo(563, 4194304);
        player.getActionSender().sendConfigTwo(1248, -2013265920);
        sendBankOptions(player);
        player.getActionSender().sendInterface(762);
        player.getActionSender().sendInventoryInterface(763);
        player.getInterfaceState().addListener(player.getBank(),
                new InterfaceContainerListener(player, -1, 64207, 95));
        player.getInterfaceState().addListener(player.getInventory(),
                new InterfaceContainerListener(player, -1, 64209, 93));
        player.getInterfaceState().addListener(player.getInventory(),
                new InterfaceContainerListener(player, 149, 0, 93));
    }

    public static void sendBankOptions(Player player) {
        player.getActionSender().sendRightClickOptions(1278, 762 * 65536 + 73,
                0, 496);
        player.getActionSender().sendRightClickOptions(1150, (763 * 65536), 0,
                27);
        player.getActionSender().sendBlankRunScript(1451);
    }

    /**
     * /** Withdraws an item.
     *
     * @param player The player.
     * @param slot The slot in the player's inventory.
     * @param id The item id.
     * @param amount The amount of the item to deposit.
     */
    public static void withdraw(Player player, int slot, int amount) {
        final Item item = player.getBank().get(slot);
        if (item == null) {
            return; // invalid packet, or client out of sync
        }
        int transferAmount = item.getCount();
        if (transferAmount >= amount) {
            transferAmount = amount;
        } else if (transferAmount == 0) {
            return; // invalid packet, or client out of sync
        }
        int newId = item.getId(); // TODO deal with withdraw as notes!
        if (player.getSettings().isWithdrawingAsNotes()) {
            if (item.getDefinition().isNoted()) {
                newId = item.getId() + 1;
            }
        }
        final ItemDefinition def = ItemDefinition.forId(newId);
        if (def.isStackable()) {
            if (player.getInventory().freeSlots() <= 0
                    && player.getInventory().getById(newId) == null) {
                player.getActionSender()
                        .sendMessage(
                        "You don't have enough inventory space to withdraw that many.");
            }
        } else {
            final int free = player.getInventory().freeSlots();
            if (transferAmount > free) {
                player.getActionSender()
                        .sendMessage(
                        "You don't have enough inventory space to withdraw that many.");
                transferAmount = free;
            }
        }
        // now add it to inv
        if (player.getInventory().add(new Item(newId, transferAmount))) {
            // all items in the bank are stacked, makes it very easy!
            final int newAmount = item.getCount() - transferAmount;
            if (newAmount <= 0) {
                player.getBank().set(slot, null);
            } else {
                player.getBank().set(slot, new Item(item.getId(), newAmount));
            }
        } else {
            player.getActionSender()
                    .sendMessage(
                    "You don't have enough inventory space to withdraw that many.");
        }
    }

    /**
     * Deposits an item.
     *
     * @param player The player.
     * @param slot The slot in the player's inventory.
     * @param id The item id.
     * @param amount The amount of the item to deposit.
     */
    public static void deposit(Player player, int slot, int amount) {
        final boolean inventoryFiringEvents = player.getInventory()
                .isFiringEvents();
        player.getInventory().setFiringEvents(false);
        try {
            final Item item = player.getInventory().get(slot);
            if (item == null) {
                return; // invalid packet, or client out of sync
            }
            int transferAmount = player.getInventory().getCount(item.getId());
            if (transferAmount >= amount) {
                transferAmount = amount;
            } else if (transferAmount == 0) {
                return; // invalid packet, or client out of sync
            }
            final boolean noted = item.getDefinition().isNoted();
            if (item.getDefinition().isStackable() || noted) {
                final int bankedId = noted ? item.getId() - 1 : item.getId();
                if (player.getBank().freeSlots() < 1
                        && player.getBank().getById(bankedId) == null) {
                    player.getActionSender()
                            .sendMessage(
                            "You don't have enough space in your bank account.");
                }
                // we only need to remove from one stack
                final int newInventoryAmount = item.getCount() - transferAmount;
                Item newItem;
                if (newInventoryAmount <= 0) {
                    newItem = null;
                } else {
                    newItem = new Item(item.getId(), newInventoryAmount);
                }
                if (!player.getBank().add(new Item(bankedId, transferAmount))) {
                    player.getActionSender()
                            .sendMessage(
                            "You don't have enough space in your bank account.");
                } else {
                    player.getInventory().set(slot, newItem);
                    player.getInventory().fireItemsChanged();
                    player.getBank().fireItemsChanged();
                }
            } else {
                if (player.getBank().freeSlots() < transferAmount) {
                    player.getActionSender()
                            .sendMessage(
                            "You don't have enough space in your bank account.");
                }
                if (!player.getBank().add(
                        new Item(item.getId(), transferAmount))) {
                    player.getActionSender()
                            .sendMessage(
                            "You don't have enough space in your bank account.");
                } else {
                    // we need to remove multiple items
                    for (int i = 0; i < transferAmount; i++) {
                        player.getInventory()
                                .set(player.getInventory().getSlotById(
                                item.getId()), null);
                    }
                    player.getInventory().fireItemsChanged();
                }
            }
        } finally {
            player.getInventory().setFiringEvents(inventoryFiringEvents);
        }
    }
}
