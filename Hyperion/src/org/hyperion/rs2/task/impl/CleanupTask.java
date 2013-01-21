package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.task.Task;

public class CleanupTask implements Task {

    @Override
    public void execute(GameEngine context) {
        context.submitWork(new Runnable() {
            @Override
            public void run() {
                System.gc();
                System.runFinalization();
            }
        });
    }
}
