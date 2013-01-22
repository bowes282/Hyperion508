package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.action.impl.PickupItemAction;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.net.Packet;

import java.util.Collection;

/**
 * Whenever a Client clicks on a GroundObject, this class will be executed. If
 * the target GroundItem is found, a PickupItemAction instance will be created.
 *
 * @author Bloodraider
 */
public final class PickupItemPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        int itemY = packet.getShortA() & 0xFFFF;
        int itemX = packet.getShort() & 0xFFFF;
        int itemId = packet.getLEShortA() & 0xFFFF;

        Region region = player.getRegion();

        Collection<GroundItem> items = region.getGroundItems();

        GroundItem groundItem = null;
        for (GroundItem item : items) {
            Location loc = item.getLocation();
            int x = loc.getX(), y = loc.getY(), id = item.getItem().getId();

            if (x == itemX && y == itemY && id == itemId) {
                groundItem = item;
                break;
            }
        }

        if (groundItem == null) {
            return new DefaultPacket();
        }

        player.getActionQueue().addAction(new PickupItemAction(player, groundItem));
        return new DefaultPacket();
    }
}