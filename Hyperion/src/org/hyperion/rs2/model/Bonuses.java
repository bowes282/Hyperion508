package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.Equipment;

/**
 * Handles the item bonuses.
 *
 * @author Linux
 */
public class Bonuses {

    /**
     * The player.
     */
    public Player player;
    /**
     * The bonus names.
     */
    private final String[] BONUS_NAMES = new String[]{"Stab", "Slash",
        "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic",
        "Range", "Strength", "Prayer", "Summoning"};
    /**
     * The bonus size.
     */
    private final int SIZE = 13;
    /**
     * The bonuses array.
     */
    public short[] bonuses = new short[SIZE];

    /**
     * Creates the bonuses.
     *
     * @param player The player
     */
    public Bonuses(Player player) {
        this.player = player;
    }

    /**
     * Sets the bonus when item is changed.
     */
    public void itemChanged() {
        for (int i = 0; i < SIZE; i++) {
            bonuses[i] = 0;
        }
        for (int i = 0; i < Equipment.SIZE; i++) {
            final Item item = player.getEquipment().get(i);
            if (item != null) {
                for (int j = 0; j < SIZE; j++) {
                    bonuses[j] += item.getDefinition().getBonus(j);
                }
            }
        }
        sendBonus(bonuses);
    }

    /**
     * Sends the bonus to the interface.
     *
     * @param bonuses The bonuses.
     */
    public void sendBonus(short[] bonuses) {
        int id = 35;
        for (int i = 0; i < bonuses.length - 1; i++) {
            player.getActionSender()
                    .sendInterfaceString(
                    (BONUS_NAMES[i] + ": "
                    + (bonuses[i] > 0 ? "+" : "") + bonuses[i]),
                    667, id++);
            if (id == 45) {
                player.getActionSender()
                        .sendInterfaceString(
                        (BONUS_NAMES[12] + ": "
                        + (bonuses[12] > 0 ? "+" : "") + bonuses[12]),
                        667, id++);
                id = 47;
            }
        }
    }
}
