package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

public class InterfaceContainerListener implements ContainerListener {
    /**
     * The player.
     */
    private final Player player;
    /**
     * The interface id.
     */
    private final int interfaceId;
    /**
     * The child id.
     */
    private final int childId;
    /**
     * The type.
     */
    private final int type;

    /**
     * Creates the container listener.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     */
    public InterfaceContainerListener(Player player, int interfaceId,
                                      int childId, int type) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.childId = childId;
        this.type = type;
    }

    @Override
    public void itemChanged(Container container, int slot) {
        player.getActionSender().sendUpdateItems(interfaceId, childId, type, container.toArray());
    }

    @Override
    public void itemsChanged(Container container) {
        player.getActionSender().sendUpdateItems(interfaceId, childId, type, container.toArray());
    }

    @Override
    public void itemsChanged(Container container, int[] slots) {
        player.getActionSender().sendUpdateItems(interfaceId, childId, type, slots, container.toArray());
    }
}
