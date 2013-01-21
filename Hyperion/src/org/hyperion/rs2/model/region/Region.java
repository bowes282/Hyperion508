package org.hyperion.rs2.model.region;

import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Region {
    /**
     * The region coordinates.
     */
    private final RegionCoordinates coordinate;
    /**
     * A list of players in this region.
     */
    private final List<Player> players = new LinkedList<Player>();
    /**
     * A list of NPCs in this region.
     */
    private final List<NPC> npcs = new LinkedList<NPC>();
    /**
     * A list of items in this region.
     */
    private List<GroundItem> items = new LinkedList<GroundItem>();

    /**
     * Creates a region.
     *
     * @param coordinate The coordinate.
     */
    public Region(RegionCoordinates coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Gets the region coordinates.
     *
     * @return The region coordinates.
     */
    public RegionCoordinates getCoordinates() {
        return coordinate;
    }

    /**
     * Gets the list of players.
     *
     * @return The list of players.
     */
    public Collection<Player> getPlayers() {
        synchronized (this) {
            return Collections.unmodifiableCollection(new LinkedList<Player>(players));
        }
    }

    /**
     * Gets the list of NPCs.
     *
     * @return The list of NPCs.
     */
    public Collection<NPC> getNpcs() {
        synchronized (this) {
            return Collections.unmodifiableCollection(new LinkedList<NPC>(npcs));
        }
    }

    /**
     * Gets the list of GroundItems in this region.
     *
     * @return The list of GroundItems in this region.
     */
    public Collection<GroundItem> getGroundItems() {
        synchronized (this) {
            return Collections.unmodifiableCollection(new LinkedList<GroundItem>(items));
        }
    }

    /**
     * Adds a new player.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        synchronized (this) {
            players.add(player);
        }
    }

    /**
     * Adds a new NPC.
     *
     * @param npc The NPC to add.
     */
    public void addNpc(NPC npc) {
        synchronized (this) {
            npcs.add(npc);
        }
    }

    /**
     * Adds a new GroundItem.
     *
     * @param item The GroundItem to add.
     */
    public void addItem(GroundItem item) {
        synchronized (this) {
            items.add(item);
        }
    }

    /**
     * Removes an old player.
     *
     * @param player The player to remove.
     */
    public void removePlayer(Player player) {
        synchronized (this) {
            players.remove(player);
        }
    }

    /**
     * Removes an old NPC.
     *
     * @param npc The NPC to remove.
     */
    public void removeNpc(NPC npc) {
        synchronized (this) {
            npcs.remove(npc);
        }
    }

    /**
     * Removes an old GroundItem.
     *
     * @param item The GroundItem to remove.
     */
    public void removeItem(GroundItem item) {
        synchronized (this) {
            items.remove(item);
        }
    }
}
