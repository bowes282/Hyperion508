package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

public class EquipmentContainerListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the container listener.
     *
     * @param player The player.
     */
    public EquipmentContainerListener(Player player) {
        this.player = player;
    }

    @Override
    public void itemChanged(Container container, int slot) {
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        player.getBonuses().itemChanged();
    }

    @Override
    public void itemsChanged(Container container) {
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        player.getBonuses().itemChanged();
    }

    @Override
    public void itemsChanged(Container container, int[] slots) {
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        player.getBonuses().itemChanged();
    }
}
