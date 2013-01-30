package org.hyperion.rs2.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.WorldLoader.LoginResult;
import org.hyperion.rs2.io.MapDataLoader;
import org.hyperion.rs2.io.MapDataPacker;
import org.hyperion.rs2.model.region.RegionManager;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.net.PacketManager;
import org.hyperion.rs2.packet.PacketHandler;
import org.hyperion.rs2.task.Task;
import org.hyperion.rs2.task.impl.SessionLoginTask;
import org.hyperion.rs2.tickable.Tickable;
import org.hyperion.rs2.tickable.TickableManager;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.event.EventManager;
import org.hyperion.rs2.event.impl.UpdateEvent;
import org.hyperion.rs2.tickable.impl.CleanupTick;
import org.hyperion.rs2.tickable.impl.RestoreEnergyTick;
import org.hyperion.rs2.util.ConfigurationParser;
import org.hyperion.rs2.util.EntityList;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.script.ScriptContext;
import org.hyperion.script.ScriptEnvironment;
import org.hyperion.script.ScriptEvents;
import org.hyperion.script.impl.RubyEnvironment;
import org.hyperion.util.BlockingExecutorService;

/**
 * @author 'Mystic Flow
 * @author Linux
 */
public final class World {

    /**
     * Logging class.
     */
    private static final Logger logger = Logger.getLogger(World.class.getName());
    /**
     * The world instance.
     */
    private static final World world = new World();
    /**
     * An executor service which handles background loading tasks.
     */
    private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newSingleThreadExecutor());
    /**
     * A list of connected players.
     */
    private final EntityList<Player> players = new EntityList<Player>(Constants.MAX_PLAYERS);
    /**
     * A list of active NPCs.
     */
    private final EntityList<NPC> npcs = new EntityList<NPC>(Constants.MAX_NPCS);
    /**
     * The packet handler.
     */
    private final PacketManager packetHandler = new PacketManager();
    /**
     * The region manager.
     */
    private final RegionManager regionManager = new RegionManager();
    /**
     * The game events
     */
    private final ScriptEvents scriptEvents = new ScriptEvents(this);
    /**
     * The script environment
     */
    private final ScriptEnvironment rubyEnvironment = new RubyEnvironment(Constants.SCRIPTS_DIR);
    /**
     * The game engine.
     */
    private GameEngine engine;
    /**
     * The event manager.
     */
    private EventManager eventManager;
    /**
     * The tick manager
     */
    private TickableManager tickableManager;
    /**
     * The current loader implementation.
     */
    private WorldLoader loader;
    /**
     * This makes you wish that Java supported typedefs.
     */
    private Map<Integer, int[]> mapData;

    /**
     * Creates the world and begins background loading tasks.
     */
    public World() {
        backgroundLoader.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                /*
                 * Check if mapdata packed file exists, if not, then we pack it.
                 */
                final File packedFile = new File("data/mapdata.dat");
                if (!packedFile.exists()) {
                    MapDataPacker.pack("data/mapdata/", "data/mapdata.dat");
                }
                /*
                 * Actually load the mapdata.
                 */
                mapData = new HashMap<Integer, int[]>();
                MapDataLoader.load(mapData);
                return null;
            }
        });
        backgroundLoader.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                ItemDefinition.load();
                return null;
            }
        });
        backgroundLoader.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                NPCDefinition.init();
                return null;
            }
        });
        backgroundLoader.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                NPCSpawn.init();
                return null;
            }
        });
    }

    /**
     * Initialises the world: loading configuration and registering global
     * events.
     *
     * @param engine The engine processing this world's tasks.
     * @throws IOException if an I/O error occurs loading configuration.
     * @throws ClassNotFoundException if a class loaded through reflection was
     * not found.
     * @throws IllegalAccessException if a class could not be accessed.
     * @throws InstantiationException if a class could not be created.
     * @throws IllegalStateException if the world is already initialized.
     */
    public void init(GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (this.engine != null) {
            throw new IllegalStateException("The world has already been initialised.");
        } else {
            this.engine = engine;
            eventManager = new EventManager(engine);
            tickableManager = new TickableManager();
            rubyEnvironment.setContext(new ScriptContext(this));
            rubyEnvironment.init();
            registerGlobalEvents();
            loadConfiguration();
        }
    }

    /**
     * Loads server configuration.
     *
     * @throws IOException if an I/O error occurs.
     * @throws ClassNotFoundException if a class loaded through reflection was
     * not found.
     * @throws IllegalAccessException if a class could not be accessed.
     * @throws InstantiationException if a class could not be created.
     */
    private void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        final FileInputStream fis = new FileInputStream("data/configuration.cfg");
        try {
            final ConfigurationParser p = new ConfigurationParser(fis);
            final Map<String, String> mappings = p.getMappings();
            if (mappings.containsKey("worldLoader")) {
                final String worldLoaderClass = mappings.get("worldLoader");
                final Class<?> loader = Class.forName(worldLoaderClass);
                this.loader = (WorldLoader) loader.newInstance();
                logger.fine("WorldLoader set to : " + worldLoaderClass);
            } else {
                loader = new GenericWorldLoader();
                logger.fine("WorldLoader set to default");
            }
            final Map<String, Map<String, String>> complexMappings = p.getComplexMappings();
            if (complexMappings.containsKey("packetHandlers")) {
                final Map<Class<?>, Object> loadedHandlers = new HashMap<Class<?>, Object>();
                for (final Map.Entry<String, String> handler : complexMappings.get("packetHandlers").entrySet()) {
                    final int id = Integer.parseInt(handler.getKey());
                    final Class<?> handlerClass = Class.forName(handler.getValue());
                    Object handlerInstance;
                    if (loadedHandlers.containsKey(handlerClass)) {
                        handlerInstance = loadedHandlers.get(loadedHandlers.get(handlerClass));
                    } else {
                        handlerInstance = handlerClass.newInstance();
                    }
                    PacketManager.getPacketManager().bind(id, (PacketHandler) handlerInstance);

                    logger.fine("Bound " + handler.getValue() + " to opcode : " + id);
                }
            }
        } finally {
            fis.close();
        }
    }

    /**
     * Registers global events such as updating.
     */
    private void registerGlobalEvents() {
        submit(new UpdateEvent());
        submit(new CleanupTick());
        submit(new RestoreEnergyTick());
    }

    /**
     * Gets the World class
     *
     * @return The World
     */
    public static World getWorld() {
        return world;
    }

    /**
     * Submits a new event.
     *
     * @param event The event to submit.
     */
    public void submit(Event event) {
        eventManager.submit(event);
    }

    /**
     * Submits the new tick
     *
     * @param tick The tick to submit.
     */
    public void submit(Tickable tick) {
        tickableManager.submit(tick);
    }

    /**
     * Submits a new task.
     *
     * @param task The task to submit.
     */
    public void submit(Task task) {
        engine.pushTask(task);
    }

    /**
     * Registers a new player.
     *
     * @param player The player to register.
     */
    /**
     * Loads a player's game in the work service.
     *
     * @param pd The player's details.
     */
    public void load(final PlayerDetails pd) {
        engine.submitWork(new Runnable() {
            @Override
            public void run() {
                final LoginResult lr = loader.checkLogin(pd);
                int code = lr.getReturnCode();
                if (!NameUtils.isValidName(pd.getName())) {
                    code = 11;
                }
                if (code != 2) {
                    final PacketBuilder bldr = new PacketBuilder();
                    bldr.put((byte) code);
                    pd.getSession().write(bldr.toPacket())
                            .addListener(new IoFutureListener<IoFuture>() {
                        @Override
                        public void operationComplete(IoFuture future) {
                            future.getSession().close(false);
                        }
                    });
                } else {
                    lr.getPlayer().getSession().setAttribute("player", lr.getPlayer());
                    // loader.loadPlayer(lr.getPlayer());
                    engine.pushTask(new SessionLoginTask(lr.getPlayer()));
                }
            }
        });
    }

    /**
     * Registers a new player.
     *
     * @param player The player to register.
     */
    public void register(final Player player) {
        // do final checks e.g. is player online? is world full?
        int returnCode = 2;
        if (isPlayerOnline(player.getName())) {
            returnCode = 5;
        }
        if (!players.add(player) && returnCode == 2) {
            returnCode = 7;
            logger.info("Could not register player : " + player + " [world full]");
        }
        final int fReturnCode = returnCode;
        PacketBuilder bldr = new PacketBuilder();
        bldr.put((byte) returnCode);
        bldr.put((byte) player.getRights().toInteger());
        bldr.put((byte) 0);
        bldr.put((byte) 0);
        bldr.put((byte) 0);
        bldr.put((byte) 1);
        bldr.putShort(player.getIndex());
        bldr.put((byte) 0);
        player.getSession().write(bldr.toPacket()).addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                if (fReturnCode != 2) {
                    player.getSession().close(false);
                } else {
                    player.getActionSender().sendLogin();
                }
            }
        });
        if (returnCode == 2) {
            logger.info("Registered player : " + player + " [online=" + players.size() + "]");
            player.getPrivateMessage().registered();
        }
    }

    /**
     * Unregisters a player, and saves their game.
     *
     * @param player The player to unregister.
     */
    public void unregister(final Player player) {
        player.getActionQueue().cancelQueuedActions();
        player.destroy();
        player.getSession().close(false);
        players.remove(player);
        player.getPrivateMessage().unregistered();
        logger.info("Unregistered player : " + player + " [online=" + players.size() + "]");
        /*engine.submitWork(new Runnable() {
         @Override
         public void run() {
         loader.savePlayer(player);
         }
         });*/
    }

    /**
     * Checks if a player is online.
     *
     * @param name The player's name.
     * @return <code>true</code> if they are online, <code>false</code> if not.
     */
    public boolean isPlayerOnline(final String name) {
        for (final Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registers a new npc.
     *
     * @param npc The npc to register.
     */
    public void register(NPC npc) {
        npcs.add(npc);
    }

    /**
     * Unregisters an old npc.
     *
     * @param npc The npc to unregister.
     */
    public void unregister(NPC npc) {
        npcs.remove(npc);
        npc.destroy();
    }

    /**
     * Gets the npc list.
     *
     * @return The npc list.
     */
    public EntityList<NPC> getNPCs() {
        return npcs;
    }

    /**
     * Gets the player list.
     *
     * @return The player list.
     */
    public EntityList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the packet manager.
     *
     * @return The packet manager.
     */
    public PacketManager getPacketManager() {
        return packetHandler;
    }

    /**
     * Gets the ruby script environment
     *
     * @return rubyEnvironment The script environment
     */
    public ScriptEnvironment getRubyEnvironment() {
        return rubyEnvironment;
    }

    /**
     * Gets the game engine.
     *
     * @return The game engine.
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Gets the background loader.
     *
     * @return The background loader.
     */
    public BlockingExecutorService getBackgroundLoader() {
        return backgroundLoader;
    }

    /**
     * Gets the region manager.
     *
     * @return The region manager.
     */
    public RegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * Gets the game events
     *
     * @return The game events
     */
    public ScriptEvents getScriptEvents() {
        return scriptEvents;
    }

    /**
     * Gets the tickable manager
     *
     * @return tickableManger The tickable manager
     */
    public TickableManager getTickableManager() {
        return tickableManager;
    }

    /**
     * Gets mapdata for a region.
     *
     * @param region The region.
     * @return The map data.
     */
    public int[] getMapData(int region) {
        return mapData.get(region);
    }

    /**
     * Handles an exception in any of the pools.
     *
     * @param t The exception.
     */
    public void handleError(Throwable t) {
        logger.severe("An error occurred in an executor service! The server will be halted immediately.");
        t.printStackTrace();
        System.exit(1);
    }
}
