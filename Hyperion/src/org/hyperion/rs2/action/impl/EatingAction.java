package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * An action for eating food and drinking
 *
 * @author Korsakoff
 */
public class EatingAction extends DestructionAction {

    /**
     * Represents types of bones.
     *
     * @author Korsakoff
     */
    public static enum Food {

        /**
         * Anchovies
         */
        ANCHOVIE(319, 1),
        /**
         * Shrimp
         */
        SHRIMP(315, 3),
        /**
         * Chicken
         */
        CHICKEN(2140, 3),
        /**
         * Meat
         */
        MEAT(2142, 3),
        /**
         * Cake
         */
        CAKE(1891, 4, 1893),
        /**
         * Bread
         */
        BREAD(2309, 5),
        /**
         * Herring
         */
        HERRING(347, 5),
        /**
         * Trout
         */
        TROUT(333, 7),
        /**
         * Cod
         */
        COD(339, 7),
        /**
         * Pike
         */
        PIKE(351, 8),
        /**
         * Salmon
         */
        SALMON(329, 9),
        /**
         * Tuna
         */
        TUNA(361, 10),
        /**
         * Lobster
         */
        LOBSTER(379, 12),
        /**
         * Bass
         */
        BASS(365, 13),
        /**
         * Swordfish
         */
        SWORDFISH(373, 14),
        /**
         * Monkfish
         */
        MONKFISH(7946, 16),
        /**
         * Shark
         */
        SHARK(385, 20),
        /**
         * Sea Turtle
         */
        TURTLE(397, 21),
        /**
         * Manta Ray
         */
        MANTA(391, 22);
        /**
         * The food id
         */
        private int id;
        /**
         * The healing health
         */
        private int heal;
        /**
         * The new food id if needed
         */
        private int newId;
        /**
         * A map of object ids to foods.
         */
        private static Map<Integer, Food> foods = new HashMap<Integer, Food>();

        /**
         * Gets a food by an object id.
         *
         * @param object The object id.
         * @return The food, or <code>null</code> if the object is not a food.
         */
        public static Food forId(int object) {
            return foods.get(object);
        }

        /**
         * Populates the tree map.
         */
        static {
            for (final Food food : Food.values()) {
                foods.put(food.id, food);
            }
        }

        /**
         * Represents a food being eaten
         *
         * @param id The food id
         * @param heal The healing health received
         */
        private Food(int id, int heal) {
            this.id = id;
            this.heal = heal;
        }

        /**
         * Represents a part of a food item being eaten (example: cake)
         *
         * @param id The food id
         * @param heal The heal amount
         * @param newId The new food id
         */
        private Food(int id, int heal, int newId) {
            this.id = id;
            this.heal = heal;
            this.newId = newId;
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
        public int getHeal() {
            return heal;
        }

        /**
         * Gets the new food id
         *
         * @return The new food id.
         */
        public int getNewId() {
            return newId;
        }
    }
    /**
     * The food type.
     */
    private final Food food;
    /**
     * The delay.
     */
    private static final int DELAY = 1800;

    /**
     * Creates the action.
     *
     * @param player The player.
     * @param food The food.
     */
    public EatingAction(Player player, Food food) {
        super(player);
        this.food = food;
    }

    @Override
    public long getDestructionDelay() {
        return DELAY;
    }

    @Override
    public void init() {
        final Player player = getPlayer();
        player.getActionSender().sendMessage("You eat the " + ItemDefinition.forId(food.getId()).getName().toLowerCase());
        player.playAnimation(Animation.create(829));
        if (food.getNewId() == 0) {
            player.getInventory().remove(player.getInventory().getSlotById(food.getId()), new Item(food.getId()));
        } else {
            player.getInventory().remove(player.getInventory().getSlotById(food.getId()), new Item(food.getId()));
            player.getInventory().add(new Item(food.getNewId()));
        }
        player.heal(food.getHeal());
    }
}