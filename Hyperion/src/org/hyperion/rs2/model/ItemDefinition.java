package org.hyperion.rs2.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.logging.Logger;
import org.hyperion.util.Buffers;

public class ItemDefinition {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(ItemDefinition.class.getName());
    /**
     * Max amount of items.
     */
    public static final int MAX_ITEMS = 11633;
    /**
     * The definition array.
     */
    private static final ItemDefinition[] definitions = new ItemDefinition[MAX_ITEMS];

    /**
     * Loads the ItemDefintions.
     *
     * @throws IOException
     */
    public static void load() throws IOException {
        logger.info("Loading Item Definitions...");
        final RandomAccessFile raf = new RandomAccessFile("./data/itemDefinitions.dat", "r");
        final FileChannel channel = raf.getChannel();
        final ByteBuffer buf = channel.map(MapMode.READ_ONLY, 0, channel.size());
        final int length = buf.getInt();
        try {
            if (length != MAX_ITEMS) {
                throw new IOException("MAX_ITEMS");
            }
            int size = 0;
            for (short i = 0; i < length; i++) {
                final short id = buf.getShort();
                if (id == -1) {
                    continue;
                }
                final short equipId = buf.getShort();
                final boolean noted = buf.get() == 1;
                final boolean stackable = buf.get() == 1;
                final ItemPrice price = new ItemPrice();
                price.normPrice = buf.getInt();
                price.minPrice = buf.getInt();
                price.maxPrice = buf.getInt();

                final int[] bonuses = new int[13];
                for (int j = 0; j < 13; j++) {
                    bonuses[j] = buf.getShort();
                }
                final String name = Buffers.readString(buf);
                final String examine = Buffers.readString(buf);
                definitions[i] = new ItemDefinition(i, name, examine, noted,
                        stackable, equipId, bonuses, price);
                size++;
            }
            logger.info("Loaded and defined " + size + " item definitions.");
        } finally {
            raf.close();
            channel.close();
        }
    }

    /**
     * Gets a definition for the specified id.
     *
     * @param id The id.
     * @return The definition.
     */
    public static ItemDefinition forId(int id) {
        return definitions[id];
    }
    /**
     * Id.
     */
    private final int id;
    /**
     * Name.
     */
    private final String name;
    /**
     * Description.
     */
    private final String examine;
    /**
     * Noted flag.
     */
    private final boolean noted;
    /**
     * Stackable flag.
     */
    private final boolean stackable;
    /**
     * The equip id.
     */
    private final int equipId;
    /**
     * The bonuses.
     */
    private final int[] bonus;
    /**
     * The price
     */
    private final ItemPrice price;

    /**
     * Creates the item definition.
     *
     * @param id The id.
     * @param name The name.
     * @param examine The description.
     * @param noted The noted flag.
     * @param noteable The noteable flag.
     * @param stackable The stackable flag.
     * @param parentId The non-noted id.
     * @param notedId The noted id.
     * @param members The members flag.
     * @param price The prices of the item
     */
    private ItemDefinition(int id, String name, String examine, boolean noted,
            boolean stackable, int equipId, int[] bonus, ItemPrice price) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.noted = noted;
        this.stackable = stackable;
        this.equipId = equipId;
        this.bonus = bonus;
        this.price = price;
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
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return examine;
    }

    /**
     * Gets the noted flag.
     *
     * @return The noted flag.
     */
    public boolean isNoted() {
        return noted;
    }

    /**
     * Gets the stackable flag.
     *
     * @return The stackable flag.
     */
    public boolean isStackable() {
        return stackable || noted;
    }

    /**
     * Gets the equip id.
     *
     * @return The equip id.
     */
    public int getEquipId() {
        return equipId;
    }

    /**
     * Gets the bonuses.
     *
     * @return The bonuses.
     */
    public int[] getBonuses() {
        return bonus;
    }

    /**
     * Gets the bonuses.
     *
     * @return The bonuses.
     */
    public int getBonus(int id) {
        return bonus[id];
    }

    /**
     * Gets the item prices
     *
     * @return The prices
     */
    public ItemPrice getPrice() {
        return price;
    }

    public static class ItemPrice {

        /**
         * The minimum price
         */
        private int minPrice;
        /**
         * The max price
         */
        private int maxPrice;
        /**
         * The normal price
         */
        private int normPrice;

        /**
         * Gets the minimum price
         *
         * @return minPrice The minimum price
         */
        public int getMinimumPrice() {
            return minPrice;
        }

        /**
         * Gets the maximum price
         *
         * @return maxPrice The maximum price
         */
        public int getMaximumPrice() {
            return maxPrice;
        }

        /**
         * Gets the normal price
         *
         * @return normPrice The normal price
         */
        public int getNormalPrice() {
            return normPrice;
        }
    }
}