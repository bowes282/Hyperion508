package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.net.Packet;

import java.util.Collection;

/**
 * Whenever a Client walks into a new Region, this class will be executed.
 *
 * @author Bloodraider
 */
public class RegionPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        Region r = player.getRegion();

        Collection<GroundItem> items = r.getGroundItems();

        for (GroundItem g : items) {
            if (g.isAvailable() && g.isGlobal()) {
                player.getActionSender().sendGroundItemCreation(g);
            }
        }
        return new DefaultPacket();
    }
}
