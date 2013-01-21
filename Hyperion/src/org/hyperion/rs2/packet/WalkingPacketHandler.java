package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * @author 'Mystic Flow
 */
public class WalkingPacketHandler implements PacketHandler {
    final int FOLLOWING = 138;

    @Override
    public PacketListener handle(Player player, Packet packet) {
        int size = packet.getLength();
        if (packet.getOpcode() == 119) {
            size -= 14;
        }
        player.getWalkingQueue().reset();
        player.getActionSender().sendCloseInterface();
        player.getActionQueue().clearNonWalkableActions();
        player.resetInteractingEntity();


        final int steps = (size - 5) / 2;
        final int firstX = packet.getLEShortA();
        final int firstY = packet.getShortA();
        //logger.info("firstX : " + firstX);
        //logger.info("firstY : " + firstY);
        final boolean runSteps = packet.getByteC() == 1;
        player.getWalkingQueue().setRunningQueue(runSteps);
        player.getWalkingQueue().addStep(firstX, firstY);
        for (int i = 0; i < steps; i++) {
            player.getWalkingQueue().addStep(firstX + packet.get(), firstY + packet.getByteS());
        }
        player.getWalkingQueue().finish();
        return new DefaultPacket();
    }
}
