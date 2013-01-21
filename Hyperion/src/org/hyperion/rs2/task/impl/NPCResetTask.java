package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.task.Task;

public class NPCResetTask implements Task {

    /**
     * The npc to reset.
     */
    private final NPC npc;

    /**
     * Creates the reset task.
     *
     * @param npc The npc to reset.
     */
    public NPCResetTask(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void execute(GameEngine context) {
        npc.getUpdateFlags().reset();
        npc.setTeleporting(false);
        npc.reset();
    }
}
