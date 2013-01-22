package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

public class QuietPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        return new DefaultPacket();
    }
}
