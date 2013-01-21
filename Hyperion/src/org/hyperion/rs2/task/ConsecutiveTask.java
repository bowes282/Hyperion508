package org.hyperion.rs2.task;

import org.hyperion.rs2.GameEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConsecutiveTask implements Task {

    /**
     * The tasks.
     */
    private final Collection<Task> tasks;

    /**
     * Creates the consecutive task.
     *
     * @param tasks The child tasks to execute.
     */
    public ConsecutiveTask(Task... tasks) {
        final List<Task> taskList = new ArrayList<Task>();
        for (final Task task : tasks) {
            taskList.add(task);
        }
        this.tasks = Collections.unmodifiableCollection(taskList);
    }

    @Override
    public void execute(GameEngine context) {
        for (final Task task : tasks) {
            task.execute(context);
        }
    }
}
