package org.hyperion.rs2.model;

public class Graphic {
    /**
     * The id.
     */
    private final int id;
    /**
     * The delay.
     */
    private final int delay;

    /**
     * Creates an graphic with no delay.
     *
     * @param id The id.
     * @return The new graphic object.
     */
    public static Graphic create(int id) {
        return create(id, 0);
    }

    /**
     * Creates a graphic with a delay
     *
     * @param id    The id.
     * @param delay The delay.
     * @return The new graphic object.
     */
    public static Graphic create(int id, int delay) {
        return new Graphic(id, delay);
    }

    /**
     * Creates a graphic.
     *
     * @param id    The id.
     * @param delay The delay.
     */
    private Graphic(int id, int delay) {
        this.id = id;
        this.delay = delay;
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
     * Gets the delay.
     *
     * @return The delay.
     */
    public int getDelay() {
        return delay;
    }
}
