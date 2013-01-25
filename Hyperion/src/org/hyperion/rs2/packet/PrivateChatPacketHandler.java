package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.packet.impl.DefaultPacket;
import org.hyperion.rs2.util.ChatUtils;

/**
 * Handles friends, ignores and PMs.
 *
 * @author Graham
 *
 */
public class PrivateChatPacketHandler implements PacketHandler {

    @Override
    public PacketListener handle(Player player, Packet packet) {
        final long name = packet.getLong();
        switch (packet.getOpcode()) {
            /**
             * Add friend
             */
            case 30:
                addFriend(player, name, packet);
                break;
            /**
             * Add ignore
             */
            case 61:
                addIgnore(player, name, packet);
                break;
            /**
             * Remove friend
             */
            case 132:
                removeFriend(player, name, packet);
                break;
            /**
             * Remove ignore
             */
            case 2:
                removeIgnore(player, name, packet);
                break;
            /**
             * Send private message
             */
            case 178:
                sendMessage(player, name, packet);
                break;
        }
        return new DefaultPacket();
    }

    /**
     * Sends the private message
     *
     * @param player The player sending the message
     * @param packet The packet
     */
    public void sendMessage(Player player, long name, Packet packet) {
        final int numChars = packet.getByte() & 0xFF;
        final String text = ChatUtils.decryptPlayerChat(packet, numChars);
        player.getPrivateMessage().sendMessage(player, name, text);
    }

    /**
     * Adds a new friend to friends list
     *
     * @param player The player
     * @param packet The packet
     */
    public void addFriend(Player player, long name, Packet packet) {
        player.getPrivateMessage().addFriend(name);
    }

    /**
     * Removes a friend from friends list
     *
     * @param player The player
     * @param packet The packet
     */
    public void removeFriend(Player player, long name, Packet packet) {
        player.getPrivateMessage().removeFriend(name);
    }

    /**
     * Adds ignored player
     *
     * @param player The player
     * @param packet The packet
     */
    public void addIgnore(Player player, long name, Packet packet) {
        player.getPrivateMessage().addIgnore(name);
    }

    /**
     * Removes ignored player
     *
     * @param player The player
     * @param packet The packet
     */
    public void removeIgnore(Player player, long name, Packet packet) {
        player.getPrivateMessage().removeIgnore(name);
    }
}
