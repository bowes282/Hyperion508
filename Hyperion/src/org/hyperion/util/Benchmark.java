package org.hyperion.util;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.World;

import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Benchmark {

    /**
     * Logging class.
     */
    private static final Logger logger = Logger.getLogger(Benchmark.class
            .getName());
    private static Deque<Long> memoryByteSamples = new LinkedList<Long>();
    private static Deque<Integer> networkByteSamples = new LinkedList<Integer>();
    private static long memoryBytes;
    private static long baseMemory;
    private static int networkBytes;

    public static void init() {
        baseMemory = Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();
    }

    static {
        World.getWorld().submit(new Event(1000) {
            @Override
            public void execute() {
                if (World.getWorld().getPlayers().size() == 0) {
                    baseMemory = Runtime.getRuntime().totalMemory()
                            - Runtime.getRuntime().freeMemory();
                }
                {
                    memoryBytes = Runtime.getRuntime().totalMemory()
                            - Runtime.getRuntime().freeMemory();
                    if (memoryByteSamples.size() >= 60) {
                        memoryByteSamples.removeFirst();
                    }
                    memoryByteSamples.addLast(memoryBytes);
                    int averageBytes = 0;
                    for (final Long integer : memoryByteSamples) {
                        averageBytes += integer;
                    }
                    averageBytes /= memoryByteSamples.size();
                    logger.info("RAM: Current: " + memoryBytes
                            + " bytes, Average (last minute): " + averageBytes
                            + " bytes");
                    final int players = World.getWorld().getPlayers().size();
                    if (players > 0) {
                        memoryBytes -= baseMemory;
                        averageBytes -= baseMemory;
                        memoryBytes /= players;
                        averageBytes /= players;
                        logger.info("RAM: Current: " + memoryBytes
                                + " bytes per player, Average (last minute): "
                                + averageBytes + " bytes per player");
                    }
                }
                synchronized (Benchmark.class) {
                    if (networkByteSamples.size() >= 60) {
                        networkByteSamples.removeFirst();
                    }
                    networkByteSamples.addLast(networkBytes);
                    int averageBytes = 0;
                    for (final Integer integer : networkByteSamples) {
                        averageBytes += integer;
                    }
                    averageBytes /= networkByteSamples.size();
                    logger.info("Networking: Current: " + networkBytes
                            + " bytes, Average (last minute): " + averageBytes
                            + " bytes");
                    final int players = World.getWorld().getPlayers().size();
                    if (players > 0) {
                        networkBytes /= players;
                        averageBytes /= players;
                        logger.info("Networking: Current: " + networkBytes
                                + " bytes per player, Average (last minute): "
                                + averageBytes + " bytes per player");
                    }
                    networkBytes = 0;
                }
            }
        });
    }

    public static void addNetworkBytes(int b) {
        synchronized (Benchmark.class) {
            networkBytes += b;
        }
    }
}
