package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.action.impl.AttackAction;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class PlayerOptionPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            case 160:
            /*
             * Option 1.
			 */
                option1(player, packet);
                break;
            case 37:            /*
             * Option 2.
			 */
                option2(player, packet);
                break;
            case 227:            /*
             * Option 3.
			 */
                option3(player, packet);
                break;
        }
        return new DefaultPacket();
    }

    /**
     * Handles the first option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private void option1(final Player player, Packet packet) {
        final int id = packet.getLEShortA() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_PLAYERS) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Player Option 1", new Object[]{"id=" + id});

        final Player victim = (Player) World.getWorld().getPlayers().get(id);
        if (victim != null && player.getLocation().isWithinInteractionDistance(victim.getLocation())) {
            player.getActionQueue().addAction(new AttackAction(player, victim));
        }
    }

    /**
     * Handles the second option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private void option2(Player player, Packet packet) {
        int id = packet.getShort() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_PLAYERS) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Player Option 2", new Object[]{"id=" + id});

        Player target = (Player) World.getWorld().getPlayers().get(id);
        if (target != null) {
            player.setInteractingEntity(target);
        }

    }

    /**
     * Handles the third option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private void option3(final Player player, Packet packet) {
        int id = packet.getLEShortA() & 0xFFFF;
        if (id < 0 || id >= Constants.MAX_PLAYERS) {
            return;
        }
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Player Option 3", new Object[]{"id=" + id});

        final Player target = (Player) World.getWorld().getPlayers().get(id);
        player.getActionQueue().cancelQueuedActions();
        player.getActionQueue().addAction(new Action(player, 0) {

            @Override
            public QueuePolicy getQueuePolicy() {
                return QueuePolicy.NEVER;
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.FOLLOW; //Its a walking based packet, without this it would be discarted.
            }

            @Override
            public void execute() {
                this.setDelay(600);
            }

        });
    }
}
