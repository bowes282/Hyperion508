package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;

public abstract class ProducingAction extends Action {

    /**
     * Creates the producing action.
     *
     * @param player The player to create the action for.
     */
    public ProducingAction(Player player) {
        super(player, 0);
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.ALWAYS;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    /**
     * Gets the production delay.
     *
     * @return The delay between consecutive productions.
     */
    public abstract long getProductionDelay();

    @Override
    public void execute() {
        if (getDelay() == 0) {
            setDelay(getProductionDelay());
        } else {
        }
    }
}
