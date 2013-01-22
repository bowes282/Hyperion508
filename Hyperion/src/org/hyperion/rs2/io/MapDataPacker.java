package org.hyperion.rs2.io;

import java.io.*;
import java.util.logging.Logger;

/**
 * Packs map data
 *
 * @author TeleNubby
 * @author Graham
 */
public class MapDataPacker {

    /**
     * Logging class.
     */
    private static final Logger logger = Logger.getLogger(MapDataPacker.class
            .getName());

    /**
     * Prevent an instance being made.
     */
    private MapDataPacker() {
    }

    /**
     * We actually pack the mapdata here.
     */
    public static void pack(String from, String to) {
        logger.info("Packing mapdata...");
        try {
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(to));
            int i2 = 0;
            for (int i = 0; i < 16384; i++) {
                if (new File(from + i + ".txt").exists()) {
                    final BufferedReader in = new BufferedReader(
                            new FileReader(from + i + ".txt"));
                    for (int j = 0; j < 4; j++) {
                        out.writeInt(Integer.parseInt(in.readLine()));
                    }
                    in.close();
                    i2++;
                } else {
                    for (int j = 0; j < 4; j++) {
                        out.writeInt(0);
                    }
                }
            }
            out.flush();
            out.close();
            logger.info("Packing mapdata is complete.");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
