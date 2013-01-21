package org.hyperion.rs2.packet;

import org.hyperion.rs2.packet.impl.DefaultPacket;

import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.ChatUtils;

/**
 * @author 'Mystic Flow
 */
public class ChatPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        final int color = packet.getByte();
        final int effects = packet.getByte();
        final int numChars = packet.getByte();
        final String text = ChatUtils.decryptPlayerChat(packet, numChars);

        player.getChatMessageQueue().add(new ChatMessage(color, effects, text));
        return new DefaultPacket();
    }
}
