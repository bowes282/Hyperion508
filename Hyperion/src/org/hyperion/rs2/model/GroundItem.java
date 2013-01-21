package org.hyperion.rs2.model;

import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.tickable.impl.GroundItemTick;

import java.util.Collection;

/**
 * Represents an item on the ground (Such as loot).
 *
 * @author Bloodraider
 */
public final class GroundItem {

    /**
     * The owner of this GroundItem. Will be the only person who is able
     * to see the it until it goes Global.
     */
    private final Player player;

    /**
     * The Item
     */
    private final Item item;

    /**
     * The Region
     */
    private final Region region;

    /**
     * The Location
     */
    private final Location location;

    /**
     * The GroundItemEvent.
     */
    private final GroundItemTick lifespan;

    /**
     * Determines if this GroundItem is visible to everyone.
     */
    private boolean global;

    /**
     * Determines whether or not this GroundItem is still available.
     */
    private boolean available;

    /**
     * Creates a new GroundItem instance, and adds it to the region.
     *
     * @param player The owner.
     * @param item   The Item.
     */
    private GroundItem(Player player, Item item) {
        this.item = item;
        this.player = player;
        this.available = true;
        this.region = this.player.getRegion();
        this.location = this.player.getLocation();
        this.region.addItem(this);
        this.lifespan = new GroundItemTick(this);
    }

    /**
     * Creates a new GroundItem, displays it to the owner, and submits a new
     * GroundItemEvent into the world.
     *
     * @param player The owner.
     * @param item   The Item.
     */
    public static void create(Player player, Item item) {
        GroundItem g = new GroundItem(player, item);
        player.getActionSender().sendGroundItemCreation(g);
        World.getWorld().submit(g.lifespan);
    }

    /**
     * When called, the Item will be displayed for every player.
     */
    public void globalize() {
        this.global = true;

        Collection<Player> players = this.region.getPlayers();

        for (Player p : players)
            if (p != this.player)
                p.getActionSender().sendGroundItemCreation(this);
    }

    /**
     * When called, the GroundItem will be removed from the region for
     * any player who can see it, and it's event will be stopped.
     * <p/>
     * Synchronized in order to prevent duplication.
     * <p/>
     * The player who picked the item up, or null.
     */
    public synchronized boolean remove() {
        if (!this.isAvailable())
            return false;

        this.available = false;

        this.lifespan.stop();
        this.region.removeItem(this);

        if (this.isGlobal()) {
            Collection<Player> players = this.region.getPlayers();

            for (Player p : players)
                p.getActionSender().sendGroundItemRemoval(this);
        } else
            this.player.getActionSender().sendGroundItemRemoval(this);
        return true;
    }

    /**
     * @return The location.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * @return The Item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * If an item is global, it's visible to all Players within the map region.
     *
     * @return Whether or not this GroundItem has gone global yet.
     */
    public boolean isGlobal() {
        return this.global;
    }

    /**
     * If an item is available, it can be picked up by a player still.
     *
     * @return
     */
    public boolean isAvailable() {
        return this.available;
    }

}