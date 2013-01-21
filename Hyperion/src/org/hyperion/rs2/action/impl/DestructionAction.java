package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;

public abstract class DestructionAction extends Action {

    /**
     * Creates the destruction action for the specified player.
     *
     * @param player The player to create the action for.
     */
    public DestructionAction(Player player) {
        super(player, 0);
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.NEVER;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    /**
     * Gets the destruction delay.
     *
     * @return The delay between consecutive destructions.
     */
    public abstract long getDestructionDelay();

    /**
     * Initialization method.
     */
    public abstract void init();

    @Override
    public void execute() {
        if (getDelay() == 0) {
            setDelay(getDestructionDelay());
            init();
        } else {
            stop();
        }
    }
}
