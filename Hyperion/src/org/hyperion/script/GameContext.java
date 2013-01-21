package org.hyperion.script;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.World;

/**
 * @date 1/20/13
 * @time 7:25 PM
 */
public class GameContext {

    /**
     * The server context.
     */
    private final World context;

    /**
     * Creates the plugin context.
     *
     * @param context The server context.
     */
    public GameContext(World context) {
        this.context = context;
    }

    /**
     * Adds a command listener.
     *
     * @param name  The name of the listener.
     * @param event The listener.
     */
    public void addEvent(String name, Event event) {
        World.getWorld().getGameEventManager().parse(name, event);
    }

}
