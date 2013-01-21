package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

import java.util.HashMap;
import java.util.Map;

/**
 * An action for burying bones
 *
 * @author Linux
 */
public class BuryingAction extends DestructionAction {

    /**
     * Represents types of bones.
     *
     * @author Linux
     */
    public static enum Bone {
        /**
         * Normal bones
         */
        NORMAL(526, 100),

        /**
         * Burnt bones
         */
        BURNT(528, 100),

        /**
         * Wolf bones
         */
        WOLF(2859, 100),

        /**
         * Monkey bones
         */
        MONKEY(3183, 125),

        /**
         * Bat bones
         */
        BAT(530, 125),

        /**
         * Big bones
         */
        BIG(532, 200),

        /**
         * Jogre bones
         */
        JOGRE(3125, 200),

        /**
         * Zogre bones
         */
        ZOGRE(4812, 250),

        /**
         * Shaikahan bones
         */
        SHAIKAHAN(3123, 300),

        /**
         * Baby dragon bones
         */
        BABY(534, 350),

        /**
         * Wyvern bones
         */
        WYVERN(6812, 400),

        /**
         * Dragon bones
         */
        DRAGON(536, 500),

        /**
         * Fayrg bones
         */
        FAYRG(4830, 525),

        /**
         * Raurg bones
         */
        RAURG(4832, 550),

        /**
         * Dagannoth bones
         */
        DAGANNOTH(6729, 650),

        /**
         * Ourg bones
         */
        OURG(4834, 750);

        /**
         * The bone id
         */
        private int id;

        /**
         * The bone experience
         */
        private double experience;

        /**
         * A map of object ids to bones.
         */
        private static Map<Integer, Bone> bones = new HashMap<Integer, Bone>();

        /**
         * Gets a bone by an object id.
         *
         * @param object The object id.
         * @return The bone, or <code>null</code> if the object is not a bone.
         */
        public static Bone forId(int object) {
            return bones.get(object);
        }

        /**
         * Populates the tree map.
         */
        static {
            for (final Bone bone : Bone.values()) {
                bones.put(bone.id, bone);
            }
        }

        /**
         * Represents a bone being buried
         *
         * @param id  The bone id
         * @param exp The exp received
         */
        private Bone(int id, double experience) {
            this.id = id;
            this.experience = experience;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the exp amount.
         *
         * @return The exp amount.
         */
        public double getExperience() {
            return experience;
        }
    }

    /**
     * The bone type.
     */
    private final Bone bone;

    /**
     * The delay.
     */
    private static final int DELAY = 1800;

    /**
     * Creates the burying action
     *
     * @param player The player
     * @param bone   The bone
     */
    public BuryingAction(Player player, Bone bone) {
        super(player);
        this.bone = bone;
    }

    @Override
    public long getDestructionDelay() {
        return DELAY;
    }

    @Override
    public void init() {
        final Player player = getPlayer();
        player.getActionSender().sendMessage("You dig a hole in the ground...");
        player.playAnimation(Animation.create(827));
        player.getInventory().remove(
                player.getInventory().getSlotById(bone.getId()),
                new Item(bone.getId()));
        player.getSkills().addExperience(Skills.PRAYER, bone.getExperience());
        player.getActionSender().sendMessage("You bury the bones...");
    }

    /**
     * Gets the experience earned
     *
     * @return The experience
     */
    public double getExperience() {
        return bone.getExperience();
    }
}
