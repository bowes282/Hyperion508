package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * @author Graham
 */
public interface PacketHandler {

    /**
     * Handles a single packet.
     *
     * @param player The player.
     * @param packet The packet.
     */
    public PacketListener handle(Player player, Packet packet);
}
