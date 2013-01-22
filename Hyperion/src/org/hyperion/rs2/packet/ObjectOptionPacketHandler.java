package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.action.impl.MiningAction;
import org.hyperion.rs2.action.impl.MiningAction.Node;
import org.hyperion.rs2.action.impl.ProspectingAction;
import org.hyperion.rs2.action.impl.WoodcuttingAction;
import org.hyperion.rs2.action.impl.WoodcuttingAction.Tree;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

public class ObjectOptionPacketHandler implements PacketHandler {

    /**
     * Option 1 opcode.
     */
    private static final int OPTION_1 = 158;
    /**
     * Option 2 opcode.
     */
    private static final int OPTION_2 = 228;

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            case OPTION_1:
                handleOption1(player, packet);
                break;
            case OPTION_2:
                handleOption2(player, packet);
                break;
        }
        return new DefaultPacket();
    }

    /**
     * Handles the option 1 packet.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption1(Player player, Packet packet) {
        final int x = packet.getLEShort() & 0xFFFF;
        final int id = packet.getShort() & 0xFFFF;
        final int y = packet.getLEShortA() & 0xFFFF;

        final Location loc = Location.create(x, y, player.getLocation().getZ());
        // woodcutting
        final Tree tree = Tree.forId(id);
        if (tree != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new WoodcuttingAction(player, loc, tree));
        }
        // mining
        final Node node = Node.forId(id);
        if (node != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new MiningAction(player, loc, node));
        }
    }

    /**
     * Handles the option 2 packet.
     *
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption2(Player player, Packet packet) {
        final int y = packet.getShortA() & 0xFFFF;
        final int id = packet.getShort() & 0xFFFF;
        final int x = packet.getShortA() & 0xFFFF;

        final Location loc = Location.create(x, y, player.getLocation().getZ());
        final Node node = Node.forId(id);
        if (node != null && player.getLocation().isWithinInteractionDistance(loc)) {
            player.getActionQueue().addAction(new ProspectingAction(player, loc, node));
            return;
        }
        player.getActionSender().sendMessage("X = " + x + " Y = " + y + " ID = " + id);
    }
}
