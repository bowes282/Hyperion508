package org.hyperion.rs2.model;

public class ChatMessage {
    /**
     * The colour.
     */
    private final int color;
    /**
     * The effects.
     */
    private final int effects;
    /**
     * The packed chat text.
     */
    private final String text;

    /**
     * Creates a new chat message.
     *
     * @param color   The message colour.
     * @param effects The message effects.
     * @param text    The packed chat text.
     */
    public ChatMessage(int color, int effects, String text) {
        this.color = color;
        this.effects = effects;
        this.text = text;
    }

    /**
     * Gets the message colour.
     *
     * @return The message colour.
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets the message effects.
     *
     * @return The message effects.
     */
    public int getEffects() {
        return effects;
    }

    /**
     * Gets the packed message text.
     *
     * @return The packed message text.
     */
    public String getText() {
        return text;
    }
}
