package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.task.Task;

/**
 * @author Linux
 */
public class PlayerResetTask implements Task {

    private final Player player;

    public PlayerResetTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute(GameEngine context) {
        player.getUpdateFlags().reset();
        player.setTeleporting(false);
        player.setMapRegionChanging(false);
        player.resetTeleportTarget();
        player.reset();
    }
}
