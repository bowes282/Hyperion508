package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;

public class DefaultPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(DefaultPacketHandler.class.getName());

    @Override
    public PacketListener handle(Player player, Packet packet) {
        logger.info("Packet : [opcode=" + packet.getOpcode() + " length=" + packet.getLength() + " payload=" + packet.getPayload() + "]");
        return new DefaultPacket();
    }
}
