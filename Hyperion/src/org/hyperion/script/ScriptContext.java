package org.hyperion.script;

import org.hyperion.rs2.model.World;

/**
 * @date 1/20/13
 * @time 7:25 PM
 */
public class ScriptContext {

    /**
     * The server context.
     */
    private final World world;

    /**
     * Creates the script context.
     *
     * @param context The server context.
     */
    public ScriptContext(World world) {
        this.world = world;
    }

    /**
     * Adds a command listener.
     *
     * @param name The name of the listener.
     * @param event The listener.
     */
    public World getWorld() {
        return world;
    }
}
