package org.hyperion.rs2.model;

import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.util.xStreamManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

public class NPCSpawn {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(NPCSpawn.class.getName());

    /**
     * The list of spawns
     */
    public static List<NPCSpawn> spawns;

    @SuppressWarnings("unchecked")
    public static void init() {
        try {
            spawns = (List<NPCSpawn>) xStreamManager.load(new FileInputStream("./data/spawns.xml"));
            for (NPCSpawn npc : spawns) {
                spawn(npc);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info("Loaded " + spawns.size() + " NPC spawns.");
    }

    /**
     * The action spawning of npcs
     *
     * @param n The npc to spawn
     */
    public static void spawn(NPCSpawn n) {
        NPC npc = new NPC(NPCDefinition.forId(n.getId()));
        npc.setLocation(n.getSpawnLocation());
        Region region = World.getWorld().getRegionManager().getRegionByLocation(npc.getLocation());
        region.addNpc(npc);
        World.getWorld().register(npc);
    }

    /**
     * The NPC id.
     */
    private int id;
    /**
     * The facing direction.
     */
    private int direction;

    /**
     * The spawn location.
     */
    private Location spawnLocation;

    /**
     * Gets the NPC id
     *
     * @return id The NPC id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the facing direction
     *
     * @return direction The facing direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Gets the spawn location
     *
     * @return spawnLocation The spawn location
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

}