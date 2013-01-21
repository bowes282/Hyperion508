package org.hyperion;

import org.hyperion.rs2.RS2Server;
import org.hyperion.rs2.model.World;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The start point of the server.
 *
 * @author Graham
 */
public class Server {

    /**
     * Logging class.
     */
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    /**
     * The entry point of the application.
     *
     * @param args The command line arguments.
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public static void main(String[] args) {
        logger.info("Starting Hyperion" + RS2Server.VERSION + "...");
        World.getWorld();
        try {
            new RS2Server().bind(RS2Server.PORT).start();
        } catch (final Exception ex) {
            logger.log(Level.SEVERE, "Error starting Hyperion.", ex);
            System.exit(1);
        }
    }
}
