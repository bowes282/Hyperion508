package org.hyperion.rs2.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hyperion.data.Persistable;
import org.hyperion.rs2.action.ActionQueue;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.tickable.impl.DeathTick;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.rs2.util.NameUtils;

/**
 * @author Graham
 */
public class Player extends Entity implements Persistable {

    /**
     * Represents the rights of a player.
     *
     * @author Graham Edgecombe
     */
    public enum Rights {

        /**
         * A standard account.
         */
        PLAYER(0),
        /**
         * A player-moderator account.
         */
        MODERATOR(1),
        /**
         * An administrator account.
         */
        ADMINISTRATOR(2);
        /**
         * The integer representing this rights level.
         */
        private int value;

        /**
         * Creates a rights level.
         *
         * @param value The integer representing this rights level.
         */
        private Rights(int value) {
            this.value = value;
        }

        /**
         * Gets an integer representing this rights level.
         *
         * @return An integer representing this rights level.
         */
        public int toInteger() {
            return value;
        }

        /**
         * Gets rights by a specific integer.
         *
         * @param value The integer returned by {@link #toInteger()}.
         * @return The rights level.
         */
        public static Rights getRights(int value) {
            if (value == 1) {
                return MODERATOR;
            } else if (value == 2) {
                return ADMINISTRATOR;
            } else {
                return PLAYER;
            }
        }
    }

    /*
     * Attributes specific to our session.
     */
    /**
     * The
     * <code>IoSession</code>.
     */
    private final IoSession session;
    /**
     * The action sender.
     */
    private final ActionSender actionSender = new ActionSender(this);
    /**
     * A queue of pending chat messages.
     */
    private final Queue<ChatMessage> chatMessages = new LinkedList<ChatMessage>();
    /**
     * The private messaging
     */
    private final PrivateMessage privateMessage = new PrivateMessage(this);
    /**
     * A queue of actions.
     */
    private final ActionQueue actionQueue = new ActionQueue();
    /**
     * The current chat message.
     */
    private ChatMessage currentChatMessage;
    /**
     * Active flag: if the player is not active certain changes (e.g. items)
     * should not send packets as that indicates the player is still loading.
     */
    private boolean active = false;
    /**
     * The interface state.
     */
    private final InterfaceState interfaceState = new InterfaceState(this);
    /**
     * A queue of packets that are pending.
     */
    private final Queue<Packet> pendingPackets = new LinkedList<Packet>();
    /**
     * The request manager which manages trading and duelling requests.
     */
    private final RequestManager requestManager = new RequestManager(this);    /*
     * Core login details.
     */

    /**
     * The name.
     */
    private String name;
    /**
     * The name expressed as a long.
     */
    private long nameLong;
    /**
     * The password.
     */
    private String password;
    /**
     * The rights level.
     */
    private Rights rights = Rights.PLAYER;
    /**
     * The members flag.
     */
    private boolean members = true;
    /**
     * Player's friends
     */
    private List<Long> friends;
    /**
     * Player's ignore
     */
    private List<Long> ignores;
    /**
     * The player using HD.
     */
    private final boolean isHD;
    /*
     * Attributes.
     */
    /**
     * The player's appearance information.
     */
    private final Appearance appearance = new Appearance();
    /**
     * The player's equipment.
     */
    private final Container equipment = new Container(Container.Type.STANDARD, Equipment.SIZE);
    /**
     * The player's skill levels.
     */
    private final Skills skills = new Skills(this);
    /**
     * The player's inventory.
     */
    private final Container inventory = new Container(Container.Type.STANDARD, Inventory.SIZE);
    /**
     * The player's bank.
     */
    private final Container bank = new Container(Container.Type.ALWAYS_STACK, Bank.SIZE);
    /**
     * The player's settings.
     */
    private final Settings settings = new Settings();
    /**
     * The player's bonuses
     */
    private final Bonuses bonuses = new Bonuses(this);
    /*
     * Cached details.
     */
    /**
     * The cached update block.
     */
    private Packet cachedUpdateBlock;

    /*
     * Player information.
     */
    /**
     * The total amount of player energy
     */
    private double runEnergy = 100;
    /**
     * The total amount of special attack.
     */
    private int specialAmount = 1000;
    /**
     * The debug mode
     */
    private boolean isDebugging;

    /**
     * Creates a player based on the details object.
     *
     * @param details The details object.
     */
    public Player(PlayerDetails details) {
        super();
        session = details.getSession();
        name = details.getName();
        nameLong = NameUtils.nameToLong(name);
        password = details.getPassword();
        isHD = details.isHD();
        getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        setTeleporting(true);
    }

    /**
     * Heals the player if needed
     *
     * @param hitpoints The amount to heal
     */
    public void heal(int hitpoints) {
        final int current = getSkills().getLevel(Skills.HITPOINTS);
        if (current + hitpoints > getSkills().getLevelForExperience(Skills.HITPOINTS)) {
            getSkills().setLevel(Skills.HITPOINTS, getSkills().getLevelForExperience(Skills.HITPOINTS));
        } else {
            getSkills().setLevel(Skills.HITPOINTS, (current + hitpoints));
        }
    }

    /**
     * Gets the request manager.
     *
     * @return The request manager.
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Gets the player's name expressed as a long.
     *
     * @return The player's name expressed as a long.
     */
    public long getNameAsLong() {
        return nameLong;
    }

    /**
     * Gets the player's settings.
     *
     * @return The player's settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Gets the player's bonuses.
     *
     * @return The bonuses.
     */
    public Bonuses getBonuses() {
        return bonuses;
    }

    /**
     * Writes a packet to the
     * <code>IoSession</code>. If the player is not yet active, the packets are
     * queued.
     *
     * @param packet The packet.
     */
    public void write(Packet packet) {
        synchronized (this) {
            if (!active) {
                pendingPackets.add(packet);
            } else {
                for (final Packet pendingPacket : pendingPackets) {
                    session.write(pendingPacket);
                }
                pendingPackets.clear();
                session.write(packet);
            }
        }
    }

    /**
     * Gets the player's bank.
     *
     * @return The player's bank.
     */
    public Container getBank() {
        return bank;
    }

    /**
     * Gets the interface state.
     *
     * @return The interface state.
     */
    public InterfaceState getInterfaceState() {
        return interfaceState;
    }

    /**
     * Checks if there is a cached update block for this cycle.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean hasCachedUpdateBlock() {
        return cachedUpdateBlock != null;
    }

    /**
     * Sets the cached update block for this cycle.
     *
     * @param cachedUpdateBlock The cached update block.
     */
    public void setCachedUpdateBlock(Packet cachedUpdateBlock) {
        this.cachedUpdateBlock = cachedUpdateBlock;
    }

    /**
     * Gets the cached update block.
     *
     * @return The cached update block.
     */
    public Packet getCachedUpdateBlock() {
        return cachedUpdateBlock;
    }

    /**
     * Resets the cached update block.
     */
    public void resetCachedUpdateBlock() {
        cachedUpdateBlock = null;
    }

    /**
     * Gets the current chat message.
     *
     * @return The current chat message.
     */
    public ChatMessage getCurrentChatMessage() {
        return currentChatMessage;
    }

    /**
     * Sets the current chat message.
     *
     * @param currentChatMessage The current chat message to set.
     */
    public void setCurrentChatMessage(ChatMessage currentChatMessage) {
        this.currentChatMessage = currentChatMessage;
    }

    /**
     * Gets the queue of pending chat messages.
     *
     * @return The queue of pending chat messages.
     */
    public Queue<ChatMessage> getChatMessageQueue() {
        return chatMessages;
    }

    public PrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    /**
     * Gets the player's appearance.
     *
     * @return The player's appearance.
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Gets the player's equipment.
     *
     * @return The player's equipment.
     */
    public Container getEquipment() {
        return equipment;
    }

    /**
     * Gets the player's skills.
     *
     * @return The player's skills.
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * Gets the action sender.
     *
     * @return The action sender.
     */
    public ActionSender getActionSender() {
        return actionSender;
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's password.
     *
     * @return The player's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the player's password.
     *
     * @param pass The password.
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Gets the
     * <code>IoSession</code>.
     *
     * @return The player's <code>IoSession</code>.
     */
    public IoSession getSession() {
        return session;
    }

    /**
     * Sets the rights.
     *
     * @param rights The rights level to set.
     */
    public void setRights(Rights rights) {
        this.rights = rights;
    }

    /**
     * Gets the rights.
     *
     * @return The player's rights.
     */
    public Rights getRights() {
        return rights;
    }

    /**
     * Checks if this player has a member's account.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isMembers() {
        return members;
    }

    /**
     * Sets the members flag.
     *
     * @param members The members flag.
     */
    public void setMembers(boolean members) {
        this.members = members;
    }

    /**
     * Gets the HD client.
     *
     * @return The HD.
     */
    public boolean isHD() {
        return isHD;
    }

    @Override
    public String toString() {
        return Player.class.getName() + " [name=" + name + " rights=" + rights
                + " members=" + members + " index=" + getIndex() + "]";
    }

    /**
     * Sets the active flag.
     *
     * @param active The active flag.
     */
    public void setActive(boolean active) {
        synchronized (this) {
            this.active = active;
        }
    }

    /**
     * Gets the active flag.
     *
     * @return The active flag.
     */
    public boolean isActive() {
        synchronized (this) {
            return active;
        }
    }

    /**
     * Gets the action queue.
     *
     * @return The action queue.
     */
    public ActionQueue getActionQueue() {
        return actionQueue;
    }

    /**
     * Gets the inventory.
     *
     * @return The inventory.
     */
    public Container getInventory() {
        return inventory;
    }

    /**
     * Gets the run energy.
     *
     * @return The energy.
     */
    public double getRunEnergy() {
        return runEnergy;
    }

    /**
     * Sets the energy.
     *
     * @param runEnergy The energy.
     */
    public void setRunEnergy(double runEnergy) {
        this.runEnergy = runEnergy;
        getActionSender().sendEnergy();
    }

    /**
     * Gets the special amount.
     *
     * @return The amount.
     */
    public int getSpecialAmount() {
        return specialAmount;
    }

    /**
     * Sets the special amount.
     *
     * @param specialAmount The amount.
     */
    public void setSpecialAmount(int specialAmount) {
        this.specialAmount = specialAmount;
    }

    /**
     * If the player is in debug mode
     *
     * @return isDebugging The debug mode
     */
    public boolean isDebugging() {
        return isDebugging;
    }

    /**
     * Sets the player's debug mode
     *
     * @param state The state
     */
    public boolean isDebugging(boolean state) {
        return this.isDebugging = state;
    }

    /**
     * Manages updateflags and HP modification when a hit occurs.
     *
     * @param source The Entity dealing the blow.
     */
    public void inflictDamage(Hit inc, Entity source) {
        if (!getUpdateFlags().get(UpdateFlag.HIT)) {
            getDamage().setHit1(inc);
            getUpdateFlags().flag(UpdateFlag.HIT);
        } else {
            if (!getUpdateFlags().get(UpdateFlag.HIT_2)) {
                getDamage().setHit2(inc);
                getUpdateFlags().flag(UpdateFlag.HIT_2);
            }
        }
        skills.detractLevel(Skills.HITPOINTS, inc.getDamage());
        if (source instanceof Entity && source != null) {
            setInCombat(true);
            setAggressorState(false);
            if (isAutoRetaliating()) {
                face(source.getLocation());
                // getActionQueue().addAction(new AttackAction(this, source)); TODO: umm
            }
        }
        if (skills.getLevel(Skills.HITPOINTS) <= 0) {
            if (!isDead()) {
                World.getWorld().submit(new DeathTick(this));
            }
            setDead(true);
        }
    }

    public void inflictDamage(Hit inc) {
        this.inflictDamage(inc, null);
    }

    @Override
    public void deserialize(IoBuffer buf) {
        this.name = IoBufferUtils.getRS2String(buf);
        this.nameLong = NameUtils.nameToLong(this.name);
        this.password = IoBufferUtils.getRS2String(buf);
        this.rights = Player.Rights.getRights(buf.getUnsigned());
        this.members = buf.getUnsigned() == 1 ? true : false;
        try {
            this.friends = (List<Long>) buf.getObject();
            this.ignores = (List<Long>) buf.getObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLocation(Location.create(buf.getUnsignedShort(), buf.getUnsignedShort(), buf.getUnsigned()));
        final int[] look = new int[13];
        for (int i = 0; i < 13; i++) {
            look[i] = buf.getUnsigned();
        }
        this.appearance.setLook(look);
        for (int i = 0; i < Equipment.SIZE; i++) {
            final int id = buf.getUnsignedShort();
            if (id != 65535) {
                final int amt = buf.getInt();
                final Item item = new Item(id, amt);
                this.equipment.set(i, item);
            }
        }
        for (int i = 0; i < Skills.SKILL_COUNT; i++) {
            this.skills.setSkill(i, buf.getUnsigned(), buf.getDouble());
        }
        for (int i = 0; i < Inventory.SIZE; i++) {
            final int id = buf.getUnsignedShort();
            if (id != 65535) {
                final int amt = buf.getInt();
                final Item item = new Item(id, amt);
                this.inventory.set(i, item);
            }
        }
        if (buf.hasRemaining()) { // backwards compat
            for (int i = 0; i < Bank.SIZE; i++) {
                final int id = buf.getUnsignedShort();
                if (id != 65535) {
                    final int amt = buf.getInt();
                    final Item item = new Item(id, amt);
                    this.bank.set(i, item);
                }
            }
        }
    }

    @Override
    public void serialize(IoBuffer buf) {
        IoBufferUtils.putRS2String(buf, NameUtils.formatName(name));
        IoBufferUtils.putRS2String(buf, password);
        buf.put((byte) rights.toInteger());
        buf.put((byte) (members ? 1 : 0));
        buf.putShort((short) getLocation().getX());
        buf.putShort((short) getLocation().getY());
        buf.put((byte) getLocation().getZ());
        buf.putObject(getPrivateMessage().getFriends());
        buf.putObject(getPrivateMessage().getIgnores());

        final int[] look = appearance.getLook();
        for (int i = 0; i < 13; i++) {
            buf.put((byte) look[i]);
        }
        for (int i = 0; i < Equipment.SIZE; i++) {
            final Item item = equipment.get(i);
            if (item == null) {
                buf.putShort((short) 65535);
            } else {
                buf.putShort((short) item.getId());
                buf.putInt(item.getCount());
            }
        }
        for (int i = 0; i < Skills.SKILL_COUNT; i++) {
            buf.put((byte) skills.getLevel(i));
            buf.putDouble(skills.getExperience(i));
        }
        for (int i = 0; i < Inventory.SIZE; i++) {
            final Item item = inventory.get(i);
            if (item == null) {
                buf.putShort((short) 65535);
            } else {
                buf.putShort((short) item.getId());
                buf.putInt(item.getCount());
            }
        }
        for (int i = 0; i < Bank.SIZE; i++) {
            final Item item = bank.get(i);
            if (item == null) {
                buf.putShort((short) 65535);
            } else {
                buf.putShort((short) item.getId());
                buf.putInt(item.getCount());
            }
        }
    }

    @Override
    public void addToRegion(Region region) {
        region.addPlayer(this);
    }

    @Override
    public void removeFromRegion(Region region) {
        region.removePlayer(this);
    }

    @Override
    public int getClientIndex() {
        return getIndex() + 32768;
    }

    @Override
    public void inflictDamage(int damage, HitType type) {
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isNPC() {
        return false;
    }
}
