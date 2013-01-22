package org.hyperion.rs2.content;

import org.hyperion.rs2.model.Player;

/**
 * Handles wilderness miscellaneous methods.
 *
 * @author Linux
 */
public class Wilderness {

    /**
     * The last wilderness level
     */
    private int lastKnownWildernessLevel;
    /**
     * Check if a update is needed.
     */
    private boolean update = false;

    /**
     * Updates the levels in the wilderness and options.
     *
     * @param player
     */
    public void updateWilderness(Player player) {
        final int currentLevel = player.getLocation().wildernessLevel(
                player.getLocation());
        if (lastKnownWildernessLevel == -1) {
            update = true;
        }
        if (currentLevel != lastKnownWildernessLevel) {
            update = true;
        }
        if (update) {
            lastKnownWildernessLevel = currentLevel;
            player.getActionSender().sendInterface(197);
            player.getActionSender().sendInterfaceString(
                    "Level: " + currentLevel, 199, 0);
        }
    }
}
