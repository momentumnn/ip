package thonk.command;

import thonk.core.TaskManager;
import thonk.task.Deadline;
import thonk.task.Task;

/**
 * Represents a command which creates a Deadline task in the task list.
 */
public class MarkCommand extends Command {
    private final Task task;
    public MarkCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskManager tm) {
        tm.mark(this.task, true);
    }
}
