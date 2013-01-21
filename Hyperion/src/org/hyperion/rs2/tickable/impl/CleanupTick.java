package org.hyperion.rs2.tickable.impl;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.task.impl.CleanupTask;
import org.hyperion.rs2.tickable.Tickable;

public class CleanupTick extends Tickable {

    /**
     * The delay in milliseconds between consecutive cleanups.
     */
    public static final int CLEANUP_CYCLE_TIME = 500;

    /**
     * Creates the cleanup event to run every 5 minutes.
     */
    public CleanupTick() {
        super(CLEANUP_CYCLE_TIME);
    }

    @Override
    public void execute() {
        World.getWorld().submit(new CleanupTask());
    }
}
