package org.hyperion.rs2.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Loads mapdata from a binary file
 *
 * @author Graham
 */
public class MapDataLoader {
    /**
     * Logging class.
     */
    private static final Logger logger = Logger.getLogger(MapDataLoader.class
            .getName());

    /**
     * Prevent an instance being created.
     */
    private MapDataLoader() {
    }

    /**
     * Loads mapdata into the specified map.
     * <p/>
     * The map should have a key of <code>Integer</code> and value of
     * <code>int[]</code>.
     *
     * @param mapData The map.
     * @throws IOException
     */
    public static void load(Map<Integer, int[]> mapData) throws IOException {
        logger.info("Reading mapdata...");
        final DataInputStream in = new DataInputStream(new FileInputStream(
                "data/mapdata.dat"));
        int useableMapdata = 0;
        for (int i = 0; i < 16384; i++) {
            final int[] parts = new int[4];
            for (int j = 0; j < 4; j++) {
                parts[j] = in.readInt();
            }
            if (parts[0] != 0 && parts[1] != 0 && parts[2] != 0
                    && parts[3] != 0) {
                useableMapdata++;
            }
            mapData.put(i, parts);
        }
        logger.info("Loaded " + useableMapdata + " useable mapdata.");
    }

}
