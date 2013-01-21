package org.hyperion.rs2.event;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.packet.PacketListener;
import org.hyperion.script.Called;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author parabolika
 */
public class GameEvents {

    /**
     * The class logger
     */
    private static final Logger logger = Logger.getLogger(GameEvents.class.toString());

    /**
     * The world
     */
    private World world;

    /**
     * Creates the game events for the <code>world</code>
     *
     * @param world The world
     */
    public GameEvents(World world) {
        this.world = world;
    }

    /**
     * Sends the event
     *
     * @param eventName The name of the event
     * @param player    The client
     * @param packetRep The packet
     */
    public void sendEvent(String eventName, Player player, PacketListener packetRep) {
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
                logger.warning("Class " + packetRep.getClass().getName() + " is missing Callable annotation.");
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("player", player);
        params.put("packet", packetRep);

        world.getRubyEnvironment().callScripts(eventName, params);
    }

}
