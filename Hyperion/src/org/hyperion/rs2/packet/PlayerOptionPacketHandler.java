package org.hyperion.rs2.packet;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.packet.impl.DefaultPacket;
import org.hyperion.rs2.packet.impl.PlayerOptionPacket.PlayerOptionAttack;
import org.hyperion.rs2.packet.impl.PlayerOptionPacket.PlayerOptionThree;
import org.hyperion.rs2.packet.impl.PlayerOptionPacket.PlayerOptionTwo;

public class PlayerOptionPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            case 160:
                /*
                 * Option 1.
                 */
                return attack(player, packet);
            case 37:
                /*
                 * Option 2.
                 */
                return option2(player, packet);
            case 227:
                /*
                 * Option 3.
                 */
                return option3(player, packet);
        }
        return new DefaultPacket();
    }

    /**
     * Handles the first option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private PacketListener attack(final Player player, Packet packet) {
        final int id = packet.getLEShort() & 0xFFFF;
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Player Option 1", new Object[]{"id=" + id});

        final Player target = (Player) World.getWorld().getPlayers().get(id);
        return new PlayerOptionAttack(target);
    }

    /**
     * Handles the second option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private PacketListener option2(Player player, Packet packet) {
        int id = packet.getShort() & 0xFFFF;
        player.getActionSender().sendDebugPacket(packet.getOpcode(), "Player Option 2", new Object[]{"id=" + id});

        Player target = (Player) World.getWorld().getPlayers().get(id);
        if (target != null) {
            player.setInteractingEntity(target);
        }
        return new PlayerOptionTwo(target);
    }

    /**
     * Handles the third option on a player option menu.
     *
     * @param player
     * @param packet
     */
    private PacketListener option3(final Player player, Packet packet) {
        int id = packet.getLEShortA() & 0xFFFF;
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
        return new PlayerOptionThree(target);
    }
}
