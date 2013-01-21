package org.hyperion.rs2.tickable.impl;

import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.tickable.Tickable;

/**
 * Runs through the lifecycle of a GroundItem.
 *
 * @author Bloodraider
 */
public final class GroundItemTick extends Tickable {

    /**
     * The delay between each stage.
     */
    private static final int STAGE_DELAY = 50;

    /**
     * The GroundItem to run the event for.
     */
    private final GroundItem item;

    /**
     * Creates a new GroundItemEvent for a GroundItem.
     *
     * @param groundItem The GroundItem to create the event for.
     */
    public GroundItemTick(GroundItem groundItem) {
        super(STAGE_DELAY);
        this.item = groundItem;
    }

    @Override
    public void execute() {
        if (!this.item.isGlobal()) {
            if (this.item.isAvailable()) {
                this.item.globalize();
            } else {
                this.stop();
            }
        } else {
            if (this.item.isAvailable())
                this.item.remove();
            this.stop();
        }
    }

}