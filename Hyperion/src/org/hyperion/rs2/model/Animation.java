package org.hyperion.rs2.model;

public class Animation {

    /**
     * The id.
     */
    private final int id;
    /**
     * The delay.
     */
    private final int delay;

    /**
     * Creates an animation without a delay.
     *
     * @param id The animation id.
     * @return
     */
    public static Animation create(int id) {
        return create(id, 0);
    }

    /**
     * Creates an animation with a delay.
     *
     * @param id The animation id.
     * @param delay The animation delay.
     * @return
     */
    public static Animation create(int id, int delay) {
        return new Animation(id, delay);
    }

    /**
     * Create an animation.
     *
     * @param id The animation id.
     * @param delay The animation delay.
     */
    private Animation(int id, int delay) {
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
