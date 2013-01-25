package org.hyperion.rs2.model;

import java.util.ArrayList;
import java.util.List;
import org.hyperion.rs2.util.NameUtils;

/**
 * Handles everything dealing with private messaging
 *
 * @author Linux
 *
 */
public class PrivateMessage {

    /**
     * The player
     */
    private final Player player;
    /**
     * Increment this for every private message sent.
     */
    private int lastMessageIndex = 1;
    /**
     * Holds all of the players friends
     */
    private final List<Long> friends = new ArrayList<Long>(200);
    /**
     * Holds all of the players ignores
     */
    private final List<Long> ignores = new ArrayList<Long>(100);

    /**
     * The conductor
     *
     * @param player
     */
    public PrivateMessage(Player player) {
        this.player = player;
    }

    /**
     * Initialize the friend server and updates lists
     */
    public void initialize() {
        player.getActionSender().sendFriendServer(2);
        for (final Long friend : friends) {
            player.getActionSender().sendFriend(friend, getWorld(friend));
        }
    }

    /**
     * Adds a player to friends list
     *
     * @param player The player
     * @param name The player name
     */
    public void addFriend(long name) {
        if (friends.size() >= 200) {
            player.getActionSender().sendMessage("Your friends list is full.");
            return;
        }
        if (friends.contains(name)) {
            player.getActionSender().sendMessage(NameUtils.longToName(name)
                    + " is already on your friends list.");
            return;
        }
        friends.add(name);
        player.getActionSender().sendFriend(name, getWorld(name));
    }

    /**
     * Adds a player to the ignore list
     *
     * @param player The player
     * @param name The player name
     */
    public void addIgnore(long name) {
        if (ignores.size() >= 100) {
            player.getActionSender().sendMessage("Your ignore list is full.");
            return;
        }
        if (ignores.contains(name)) {
            player.getActionSender().sendMessage(NameUtils.longToName(name)
                    + " is already on your ignore list.");
            return;
        }
        ignores.add(name);
    }

    /**
     * Remove a friend from the list
     *
     * @param name
     */
    public void removeFriend(long name) {
        friends.remove(name);
    }

    /**
     * Removes a ignored from the list
     *
     * @param name
     */
    public void removeIgnore(long name) {
        ignores.remove(name);
    }

    /**
     * Check if player is registered
     */
    public void registered() {
        for (final Player p : World.getWorld().getPlayers()) {
            if (p != null) {
                p.getPrivateMessage().registered(player);
            }
        }
    }

    /**
     * Registers player if not registered
     *
     * @param p The Player
     */
    private void registered(Player p) {
        final long name = p.getNameAsLong();
        if (friends.contains(name)) {
            player.getActionSender().sendFriend(name, getWorld(name));
        }
    }

    /**
     * Checks if player has been unregistered
     */
    public void unregistered() {
        for (final Player p : World.getWorld().getPlayers()) {
            if (p != null) {
                p.getPrivateMessage().unregistered(player);
            }
        }
    }

    /**
     * Unregisters player if not unregistered
     *
     * @param p
     */
    private void unregistered(Player p) {
        final long name = p.getNameAsLong();
        if (friends.contains(name)) {
            player.getActionSender().sendFriend(name, 0);
        }
    }

    /**
     * Sends a private message
     *
     * @param from The player sending the message
     * @param to The player receiving the message
     * @param message The message
     */
    public void sendMessage(Player from, long to, String message) {
        for (final Player p : World.getWorld().getPlayers()) {
            if (p.getNameAsLong() == to) {
                p.getActionSender().sendReceivedPrivateMessage(from.getNameAsLong(), from.getRights().toInteger(), message);
                player.getActionSender().sendPrivateMessage(to, message);
            }
        }
    }

    /**
     * Get the world number of friend
     *
     * @param friend The friend
     * @return World number
     */
    private int getWorld(long friend) {
        for (final Player p : World.getWorld().getPlayers()) {
            if (p != null) {
                if (p.getNameAsLong() == friend) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Gets all the friends
     *
     * @return
     */
    public List<Long> getFriends() {
        return friends;
    }

    /**
     * Gets all the ignores
     *
     * @return
     */
    public List<Long> getIgnores() {
        return ignores;
    }

    /**
     * Gets the last message sent
     *
     * @return lastMessageIndex
     */
    public int getLastMessageIndex() {
        if (lastMessageIndex >= 16000000) {
            lastMessageIndex = 0;
        }
        return lastMessageIndex++;
    }
}