package org.hyperion.script;

import java.util.Map;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.packet.PacketListener;
import org.hyperion.script.util.Called;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author parabolika
 */
public class ScriptEvents {

    /**
     * The class logger
     */
    private static final Logger logger = Logger.getLogger(ScriptEvents.class.toString());
    /**
     * The world
     */
    private World world;

    /**
     * Creates the game events for the
     * <code>world</code>
     *
     * @param world The world
     */
    public ScriptEvents(World world) {
        this.world = world;
    }

    /**
     * Sends a custom event
     *
     * @param eventName The custom event name
     * @param params The parameters of the event
     */
    public void sendEvent(String eventName, Map<String, Object> params) {
        world.getRubyEnvironment().callScripts(eventName, params);
    }

    /**
     * Send a packet event
     *
     * @param eventName The name of the event
     * @param player The player to send the event to
     * @param packetRep The packet of the event
     */
    public void sendPacketEvent(String eventName, Player player, PacketListener packetRep) {
        /**
         * eventName can be null if this is a Packet event, and packetRep can be
         * null if this is a server tick event.
         */
        if (eventName == null) {
            if (packetRep.getClass().isAnnotationPresent(Called.class)) {
                eventName = packetRep.getClass().getAnnotation(Called.class).value();
            } else {
                /**
                 * All PacketListeners should contain an annotation
                 */
                logger.log(Level.WARNING, "Class {0} is missing Callable annotation.", packetRep.getClass().getName());
            }
        }
        world.getRubyEnvironment().callScripts(eventName, world.getRubyEnvironment()
                .setParams("player", player).setParams("packet", packetRep).getParams());
    }
}
