package thonk.command;

import thonk.core.TaskManager;
import thonk.task.Task;

/**
 * Represents a command which marks a task as undone in the task list.
 */
public class UnmarkCommand extends Command {
    private final Task task;
    public UnmarkCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskManager tm) {
        tm.mark(this.task, false);
        this.response = "Unmarked " + this.task.getDescription();
    }
}
