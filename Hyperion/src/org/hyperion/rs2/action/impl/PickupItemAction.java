package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

/**
 * Executed when the player clicks on a GroundItem. Responsible for walking to
 * and picking it up.
 *
 * @author Bloodraider
 */
public final class PickupItemAction extends Action {

    /**
     * The target groundItem.
     */
    private final GroundItem item;

    public PickupItemAction(Player player, GroundItem item) {
        super(player, 50);
        this.item = item;
    }

    @Override
    public QueuePolicy getQueuePolicy() {
        return QueuePolicy.NEVER;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.WALKABLE;
    }

    @Override
    public void execute() {
        Player player = this.getPlayer();
        Location pLocation = player.getLocation();
        Location tLocation = this.item.getLocation();

        if (!this.item.isAvailable()) {
            player.getWalkingQueue().reset();
            this.stop();
        } else if (pLocation.getX() == tLocation.getX() && pLocation.getY() == tLocation.getY()) {
            if (this.item.isAvailable()) {
                if (player.getInventory().hasRoomFor(item.getItem())) {
                    player.getInventory().add(this.item.getItem());
                    item.remove();
                } else {
                    player.getActionSender().sendMessage("You do not have enough room for that!");
                }
            }
            this.stop();
        }
    }
}