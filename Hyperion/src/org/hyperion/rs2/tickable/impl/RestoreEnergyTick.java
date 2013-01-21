package org.hyperion.rs2.tickable.impl;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.task.impl.RestoreEnergyTask;
import org.hyperion.rs2.tickable.Tickable;

public class RestoreEnergyTick extends Tickable {
    /**
     * The delay in milliseconds between restoring.
     */
    public static final int DELAY = 2250;

    /**
     * Sets the server to run event to run every 2.25 seconds.
     */
    public RestoreEnergyTick() {
        super(DELAY);
    }

    @Override
    public void execute() {
        World.getWorld().submit(new RestoreEnergyTask());
    }
}
