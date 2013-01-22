package org.hyperion.rs2.task;

import org.hyperion.rs2.GameEngine;

public interface Task {

    /**
     * Executes the task. The general contract of the execute method is that it
     * may take any action whatsoever.
     *
     * @param context The game engine this task is being executed in.
     */
    public void execute(GameEngine context);
}
