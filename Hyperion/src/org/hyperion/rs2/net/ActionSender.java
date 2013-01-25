package org.hyperion.rs2.net;

import org.apache.mina.core.future.IoFutureListener;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.impl.EquipmentContainerListener;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.container.impl.WeaponContainerListener;
import org.hyperion.rs2.net.Packet.Type;
import org.hyperion.rs2.util.ChatUtils;

/**
 * @author 'Mystic Flow
 */
public final class ActionSender {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the action sender.
     *
     * @param player The player.
     */
    public ActionSender(Player player) {
        this.player = player;
    }

    /**
     * Sends all the sidebar interfaces.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSidebarInterfaces() {
        final int[] icons = Constants.SIDEBAR_INTERFACES[0];
        final int[] interfaces = Constants.SIDEBAR_INTERFACES[1];
        for (int i = 0; i < icons.length; i++) {
            //sendSidebarInterface(icons[i], interfaces[i]);
        }
        //	sendSidebarInterface(6, player.getMagic().getSpellBook().toInteger());
        return this;
    }

    /**
     * Sends the login.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendLogin() {
        /*
         * Actives the player.
         */
        player.setActive(true);

        /*
         * Sends the game interfaces.
         */
        sendMapRegion();
        sendWelcomeScreen();
        sendSidebar();
        player.getPrivateMessage().initialize();

        /*
         * Sends player's data.
         */
        sendSkillLevels();
        sendEnergy();

        /*
         * Sends player options.
         */
        sendInteractionOption("Attack", 1, true);
        sendInteractionOption("Trade with", 3, false);
        sendInteractionOption("Follow", 2, false);

        /*
         * Adds the interface listeners.
         */
        final InterfaceContainerListener inventoryListener = new InterfaceContainerListener(player, Inventory.INTERFACE, 0, 93);
        player.getInventory().addListener(inventoryListener);
        final InterfaceContainerListener equipmentListener = new InterfaceContainerListener(player, Equipment.INTERFACE, 28, 94);
        player.getEquipment().addListener(equipmentListener);
        player.getEquipment().addListener(new EquipmentContainerListener(player));
        player.getEquipment().addListener(new WeaponContainerListener(player));

        World.getWorld().getScriptEvents().sendPacketEvent("login", player, null);
        return this;
    }

    /**
     * Sends the logout packet.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender logout() {
        player.getSession().write(new PacketBuilder(102).toPacket()).addListener(IoFutureListener.CLOSE);
        return this;
    }

    /**
     * Sends a message.
     *
     * @param string The message to send.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendMessage(String string) {
        player.write(new PacketBuilder(218, Type.VARIABLE).putRS2String(string).toPacket());
        return this;
    }

    public ActionSender sendTabs() {
        for (int i = 16; i <= 21; i++) {
            player.getActionSender().sendInterfaceConfig(player.isHD() ? 746 : 548, i, false);
        }
        for (int i = 32; i <= 38; i++) {
            player.getActionSender().sendInterfaceConfig(player.isHD() ? 746 : 548, i, false);
        }
        player.getActionSender().sendInterfaceConfig(player.isHD() ? 746 : 548, 14, false);
        player.getActionSender().sendInterfaceConfig(player.isHD() ? 746 : 548, 31, false);
        player.getActionSender().sendInterfaceConfig(player.isHD() ? 746 : 548, 63, false);
        player.getActionSender().sendInterfaceConfig(player.isHD() ? 746 : 548, 72, false);
        return this;
    }

    /**
     * Sends all the sidebars.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSidebar() {
        sendTab(6, 745);
        sendTab(7, 754);
        sendTab(11, 751); // Chat options
        sendTab(68, 752); // Chatbox
        sendTab(64, 748); // HP bar
        sendTab(65, 749); // Prayer bar
        sendTab(66, 750); // Energy bar
        sendTab(67, 747);
        sendConfig(1160, -1);
        sendTab(8, 137); // Playername on chat
        sendTab(73, 92); // Attack tab
        sendTab(74, 320); // Skill tab
        sendTab(75, 274); //  Quest tab
        sendTab(76, 149); // Inventory tab
        sendTab(77, 387); // Equipment tab
        sendTab(78, 271); // Prayer tab
        sendTab(79, 192); // Magic tab
        sendTab(81, 550); // Friend tab
        sendTab(82, 551); // Ignore tab
        sendTab(83, 589); // Clan tab
        sendTab(84, 261); // Setting tab
        sendTab(85, 464); // Emote tab
        sendTab(86, 187); // Music tab
        sendTab(87, 182); // Logout tab
        return this;
    }

    /**
     * Sends a tab.
     *
     * @param tabId The tab id.
     * @param childId The child id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendTab(int tabId, int childId) {
        if (player.isHD()) {
            sendGameInterface(1, childId == 137 ? 752 : 746, tabId, childId);
        } else {
            sendGameInterface(1, childId == 137 ? 752 : 548, tabId, childId);
        }
        return this;
    }

    /**
     * Sends a game interface.
     *
     * @param showId The show id.
     * @param windowId The window id.
     * @param interfaceId The interface id.
     * @param childId The child id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendGameInterface(int showId, int windowId, int interfaceId, int childId) {
        PacketBuilder bldr = new PacketBuilder(93);
        bldr.putShort(childId);
        bldr.putByteA(showId).putShort(windowId).putShort(interfaceId);
        player.getSession().write(bldr.toPacket());
        return this;
    }

    /**
     * Sends the map region.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendMapRegion() {
        PacketBuilder bldr = new PacketBuilder(142, Type.VARIABLE_SHORT);
        boolean forceSend = true;
        if ((((player.getLocation().getRegionX() / 8) == 48) || ((player.getLocation().getRegionX() / 8) == 49)) && ((player.getLocation().getRegionY() / 8) == 48)) {
            forceSend = false;
        }
        if (((player.getLocation().getRegionX() / 8) == 48) && ((player.getLocation().getRegionY() / 8) == 148)) {
            forceSend = false;
        }
        bldr.putShortA(player.getLocation().getRegionX());
        bldr.putLEShortA(player.getLocation().getLocalY());
        bldr.putShortA(player.getLocation().getLocalX());
        for (int xCalc = (player.getLocation().getRegionX() - 6) / 8; xCalc <= ((player.getLocation().getRegionX() + 6) / 8); xCalc++) {
            for (int yCalc = (player.getLocation().getRegionY() - 6) / 8; yCalc <= ((player.getLocation().getRegionY() + 6) / 8); yCalc++) {
                int region = yCalc + (xCalc << 8); // 1786653352
                if (forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
                    final int[] mapData = World.getWorld().getMapData(region);
                    bldr.putInt(mapData[0]);
                    bldr.putInt(mapData[1]);
                    bldr.putInt(mapData[2]);
                    bldr.putInt(mapData[3]);
                }
            }
        }
        bldr.putByteC(player.getLocation().getZ());
        bldr.putShort((player.getLocation().getRegionY()));
        player.getSession().write(bldr.toPacket());
        player.setLastKnownRegion(player.getLocation());
        return this;
    }

    /**
     * Sends a game pane.
     *
     * @param pane The pane id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendGamePane(int pane) {
        PacketBuilder bldr = new PacketBuilder(239).putShort(pane).putByteA((byte) 0);
        player.getSession().write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a config.
     *
     * @param id The id.
     * @param value The value.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendConfig(int id, int value) {
        if (value < 128) {
            sendConfigOne(id, value);
        } else {
            sendConfigTwo(id, value);
        }
        return this;
    }

    /**
     * Sends a configuration id along with a value #1.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendConfigOne(int id, int value) {
        player.write(new PacketBuilder(100).putShortA(id).putByteA((byte) value).toPacket());
        return this;
    }

    /**
     * Sends a configuration id along with a value #2.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendConfigTwo(int id, int value) {
        player.write(new PacketBuilder(161).putShort(id).putInt1(value).toPacket());
        return this;
    }

    /**
     * Sends the player's engery.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendEnergy() {
        player.getSession().write(new PacketBuilder(99)
                .put((byte) player.getRunEnergy()).toPacket());
        return this;
    }

    /**
     * Sends the player's skills.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSkillLevels() {
        for (int i = 0; i < 23; i++) {
            sendSkillLevel(i);
        }
        return this;
    }

    /**
     * Sends the skill level.
     *
     * @param skill The skill.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSkillLevel(int skill) {
        PacketBuilder bldr = new PacketBuilder(217);
        bldr.putByteC((byte) player.getSkills().getLevel(skill));
        bldr.putInt2((int) player.getSkills().getExperience(skill));
        bldr.putByteC((byte) skill);
        player.getSession().write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a game interface.
     *
     * @param id The Interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendGameInterface(int id) {
        sendCloseInterface();
        sendGameInterface(0, 548, 8, id);
        return this;
    }

    /**
     * Sends an interface.
     *
     * @param id The Interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterface(int id) {
        sendCloseInterface();
        if (player.isHD()) {
            sendGameInterface(0, 746, 4, id); // 3 norm, 4 makes bank work, 6 makes help work
            sendGameInterface(0, 746, 8, id);
        } else {
            sendGameInterface(0, 548, 8, id);
        }
        return this;
    }

    /**
     * Closes an interface.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendCloseInterface() {
        if (player.isHD()) {
            sendCloseInterface(746, 3);
            sendCloseInterface(746, 4);
            sendCloseInterface(746, 6);
            sendCloseInterface(746, 8);
        } else {
            sendCloseInterface(548, 8);
        }
        return this;
    }

    /**
     * Closes an interface.
     *
     * @param windowId The window id.
     * @param position The position.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendCloseInterface(int windowId, int position) {
        player.write(new PacketBuilder(246).putInt(windowId << 16 | position).toPacket());
        return this;
    }

    /**
     * Closes an interface.
     *
     * @param childId The child id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInventoryInterface(int childId) {
        if (player.isHD()) {
            sendInterfaceConfig(746, 71, false);
            sendGameInterface(0, 746, 71, childId);
        } else {
            sendInterfaceConfig(548, 71, false);
            sendGameInterface(0, 548, 71, childId);
        }
        return this;
    }

    /**
     * Sends a black run script.
     *
     * @param id The id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendBlankRunScript(int id) {
        player.write(new PacketBuilder(144, Type.VARIABLE_SHORT).putShort(0).putRS2String("").putInt(id).toPacket());
        return this;
    }

    /**
     * Sends a debug message
     *
     * @param message The message
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendDebugMessage(String message) {
        return player.isDebugging() ? sendMessage("<col=ff0000>" + message) : this;
    }

    /**
     * Sends a debug message.
     *
     * @param opCode The opcode
     * @param description The description of the message
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendDebugPacket(int opCode, String description, Object[] params) {
        String paramString = "";
        for (Object object : params) {
            paramString += object.toString() + "    ";
        }
        return sendDebugMessage("------------------------------------------------------------------------------------------")
                .sendDebugMessage("[opcode=" + opCode + " desc=" + description + "]")
                .sendDebugMessage("------------------------------------------------------------------------------------------")
                .sendDebugMessage("Params: [" + paramString + "]")
                .sendDebugMessage("------------------------------------------------------------------------------------------");
    }

    /**
     * Sends right click options.
     *
     * @param set The set.
     * @param inter The interface.
     * @param off The off.
     * @param len The len.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendRightClickOptions(int set, int inter, int off, int len) {
        player.write(new PacketBuilder(191).putLEShort(len).putInt1(set).putShortA(0).putInt(inter).putLEShort(off).toPacket());
        return this;
    }

    public void sendWelcomeScreen() {
        sendGamePane(549);
        sendGameInterface(1, 549, 2, 378);
        sendGameInterface(1, 549, 3, 15);
        sendInterfaceString("BLACK FLAG", 378, 115);
        // TODO last connection
        sendInterfaceString("You are connected from: " + player.getSession().getRemoteAddress(), 378, 116);
        sendInterfaceString("0", 378, 39);
        sendInterfaceString("0", 378, 96);
        sendInterfaceString("0 unread messages", 378, 37);
        sendInterfaceString(" staff will NEVER email you. We use the Message Centre on the website instead.", 378, 38);
        sendInterfaceString("0 days of member credit", 378, 94);
        sendInterfaceString("You have 0 days of member credit remaining. Please click here to extend your credit.", 378, 93);
        sendInterfaceString("You do not have a bank pin. Please visit a bank if you would like one.", 378, 62);
        sendInterfaceString("You have not yet set any recovery questions.", 378, 56);
        sendInterfaceString("Message of the Week", 15, 0);
        sendInterfaceString("Remember to keep your account secure: set a bank PIN, set recover questions and NEVER give away your password.", 15, 4);
    }

    public void sendHdLogin() {
        sendGameInterface(1, 549, 0, 746);
        sendGameInterface(1, 746, 13, 748); //energy orb
        sendGameInterface(1, 746, 14, 749); //energy orb
        sendGameInterface(1, 746, 15, 750); //energy orb
        //sendGameInterface(1, 746, 16, 747); //summing orb
        sendGameInterface(1, 746, 18, 751); //things below chatbox 
        sendGameInterface(1, 752, 8, 137); //chatbox
        sendGameInterface(1, 746, 65, 752); //chatbox 752
        sendGameInterface(1, 549, 0, 746); // Main interface
        sendGameInterface(1, 746, 87, 92); // Attack tab
        sendGameInterface(1, 746, 88, 320); // Skill tab
        sendGameInterface(1, 746, 89, 274); // Quest tab
        sendGameInterface(1, 746, 90, 149); // Inventory tab
        sendGameInterface(1, 746, 91, 387); // Equipment tab
        sendGameInterface(1, 746, 92, 271); // Prayer tab
        sendGameInterface(1, 746, 93, 193); // Magic tab
        //sendGameInterface(1, 746, 94, 662); // Summoning tab
        sendGameInterface(1, 746, 95, 550); // Friend tab
        sendGameInterface(1, 746, 96, 551); // Ignore tab
        sendGameInterface(1, 746, 97, 589); // Clan tab
        sendGameInterface(1, 746, 98, 261); // Setting tab
        sendGameInterface(1, 746, 99, 464); // Emote tab
        sendGameInterface(1, 746, 100, 187); // Music tab
        sendGameInterface(1, 746, 101, 182); // Logout tab
        sendGameInterface(1, 752, 8, 137); // Chatbox 
        sendGameInterface(1, 746, 65, 752); // Chatbox 752
        sendGameInterface(1, 746, 18, 751); // Settings below chatbox
        sendGameInterface(1, 746, 13, 748); // HP orb
        sendGameInterface(1, 746, 14, 749); // Prayer orb
        sendGameInterface(1, 746, 15, 750); // Energy orb
        //sendInterface(1, 746, 12, 747); // Summoning orb
        player.getEquipment().fireItemsChanged();
        sendWelcomeScreen();
    }

    /**
     * Sends a packet to update a group of items.
     *
     * @param interfaceId The interface id.
     * @param items The items.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItems(int interfaceId, int childId, int type, Item[] items) {
        PacketBuilder packet = new PacketBuilder(255, Type.VARIABLE_SHORT);
        packet.putShort(interfaceId).putShort(childId).putShort(type).putShort(items.length);
        for (final Item item : items) {
            int id, count;
            if (item != null) {
                id = item.getId();
                count = item.getCount();
            } else {
                id = -1;
                count = 0;
            }
            if (count > 254) {
                packet.putByteS((byte) 255);
                packet.putInt2(count);
            } else {
                packet.putByteS((byte) count);
            }
            packet.putLEShort(id + 1);
        }
        player.getSession().write(packet.toPacket());
        return this;
    }

    /**
     * Sends a packet to update multiple (but not all) items.
     *
     * @param interfaceId The interface id.
     * @param slots The slots.
     * @param items The item array.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItems(int interfaceId, int childId, int type, int[] slots, Item[] items) {
        PacketBuilder bldr = new PacketBuilder(255, Type.VARIABLE_SHORT);
        bldr.putShort(interfaceId).putShort(childId).putShort(type).putShort(items.length);
        for (final int slot : slots) {
            final Item item = items[slot];
            bldr.putSmart(slot);
            if (item != null) {
                final int id = item.getId();
                bldr.putShort(id + 1);
                if (id + 1 != 0) {
                    final int count = item.getCount();
                    if (count > 254) {
                        bldr.put((byte) 255);
                        bldr.putInt(count);
                    } else {
                        bldr.put((byte) count);
                    }
                }
            }
        }
        player.getSession().write(bldr.toPacket());
        return this;

    }

    /**
     * Sends a model in an interface.
     *
     * @param interfaceid The interface id.
     * @param itemid The zoom.
     * @param itemsize The model id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceModel(int interfaceid, int child,
            int itemsize, int itemid) {
        final PacketBuilder bldr = new PacketBuilder(253);
        final int inter = (interfaceid << 16) + child;
        bldr.putInt(itemsize);
        bldr.putShortA(itemid);
        bldr.putInt2(inter);
        bldr.putLEShort(0);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Replaces an ingame string with some server chosen value.
     *
     * @param string The string to write.
     * @param interfaceId The interfaceId to write the string on.
     * @param childId Where on the interface to write the string.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceString(String string, int interfaceId, int childId) {
        int sSize = string.length() + 5;
        PacketBuilder bldr = new PacketBuilder(179)
                .put((byte) (sSize / 256))
                .put((byte) (sSize % 256))
                .putRS2String(string)
                .putShort(childId)
                .putShort(interfaceId);
        player.getSession().write(bldr.toPacket());
        return this;
    }

    /**
     * Sends interface config.
     *
     * @param interfaceId The interface id.
     * @param childId The child id.
     * @param active Is the interface active?
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceConfig(int interfaceId, int childId, boolean active) {
        PacketBuilder bldr = new PacketBuilder(59);
        bldr.putByteC(active ? 1 : 0).putShort(childId).putShort(interfaceId);
        player.getSession().write(bldr.toPacket());
        return this;
    }

    /**
     * Display an interface on the chatbox.
     *
     * @param childId The interface to display on the chatbox.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendChatboxInterface(int childId) {
        sendGameInterface(1, 752, 8, childId);
        return this;
    }

    /**
     * Sends a friend to the friend list.
     *
     * @param name The name (encoded as a long).
     * @param world The world id.
     */
    public ActionSender sendFriend(long name, int world) {
        final PacketBuilder packet = new PacketBuilder(154, Type.VARIABLE)
                .putLong(name).putShort(world).put((byte) 1);
        if (world != 0) {
            if (world == world) {
                packet.putRS2String("ONLINE");
            } else {
                packet.putRS2String("ScapeRune " + world);
            }
        }
        player.getSession().write(packet.toPacket());
        return this;
    }

    /**
     * Friend server friends list load status. Loading = 0 Connecting = 1 OK = 2
     *
     * @param status Value to set.
     */
    public ActionSender sendFriendServer(int status) {
        player.write(new PacketBuilder(115).put((byte) status).toPacket());
        return this;
    }

    public ActionSender sendPrivateMessage(long name, String message) {
        byte[] bytes = new byte[message.length()];
        ChatUtils.encryptPlayerChat(bytes, 0, 0, message.length(), message.getBytes());
        player.getSession().write(new PacketBuilder(89, Type.VARIABLE).putLong(name)
                .put((byte) message.length()).put(bytes).toPacket());
        return this;
    }

    public ActionSender sendReceivedPrivateMessage(long name, int rights, String message) {
        int messageCounter = player.getPrivateMessage().getLastMessageIndex();
        byte[] bytes = new byte[message.length() + 1];
        bytes[0] = (byte) message.length();
        ChatUtils.encryptPlayerChat(bytes, 0, 1, message.length(), message.getBytes());
        player.getSession().write(new PacketBuilder(178, Type.VARIABLE)
                .putLong(name)
                .putShort(1)
                .put(new byte[]{(byte) ((messageCounter << 16) & 0xFF), (byte) ((messageCounter << 8) & 0xFF), (byte) (messageCounter & 0xFF)})
                .put((byte) rights)
                .put(bytes).toPacket());
        return this;
    }

    /**
     * Sends the player an option.
     *
     * @param slot The slot to place the option in the menu.
     * @param top Flag which indicates the item should be placed at the top.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInteractionOption(String option, int slot, boolean top) {
        PacketBuilder bldr = new PacketBuilder(252, Type.VARIABLE);
        bldr.putByteC(top ? (byte) 1 : (byte) 0);
        bldr.putRS2String(option);
        bldr.putByteC((byte) slot);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Creates a new GroundItem for this Player.
     *
     * @param g The GroundItem to add.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendGroundItemCreation(GroundItem g) {
        sendCoords(g.getLocation());
        PacketBuilder packetBuilder = new PacketBuilder(25);
        packetBuilder.putLEShortA(g.getItem().getCount());
        packetBuilder.put((byte) 0);
        packetBuilder.putLEShortA(g.getItem().getId());
        player.getSession().write(packetBuilder.toPacket());
        return this;
    }

    /**
     * Sends coordinates.
     *
     * @param location The location
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendCoords(Location location) {
        PacketBuilder packetBuilder = new PacketBuilder(177);
        int regionX = this.player.getLastKnownRegion().getRegionX();
        int regionY = this.player.getLastKnownRegion().getRegionY();
        packetBuilder.put((byte) (location.getY() - ((regionY - 6) * 8)));
        packetBuilder.putByteS((byte) (location.getX() - ((regionX - 6) * 8)));
        player.getSession().write(packetBuilder.toPacket());
        return this;
    }

    /**
     * Removes an old GroundItem.
     *
     * @param g The GroundItem to remove.
     * @return The ActionSender instance, for chaining.
     */
    public ActionSender sendGroundItemRemoval(GroundItem g) {
        sendCoords(g.getLocation());
        PacketBuilder packetBuilder = new PacketBuilder(201);
        packetBuilder.put((byte) 0);
        packetBuilder.putShort(g.getItem().getId());
        player.getSession().write(packetBuilder.toPacket());
        return this;
    }
}
