package org.hyperion.rs2.model.container;

public interface ContainerListener {
    /**
     * Called when an item is changed.
     *
     * @param container The container.
     * @param slot      The slot that was changed.
     */
    public void itemChanged(Container container, int slot);

    /**
     * Called when a group of items are changed.
     *
     * @param container The container.
     * @param slots     The slots that were changed.
     */
    public void itemsChanged(Container container, int[] slots);

    /**
     * Called when all the items change.
     *
     * @param container The container.
     */
    public void itemsChanged(Container container);
}
