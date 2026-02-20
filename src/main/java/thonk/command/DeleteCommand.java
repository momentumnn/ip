package thonk.command;

import thonk.core.TaskManager;
import thonk.core.Ui;
import thonk.task.Task;

/**
 * Represents a command which deletes a task in the task list.
 */
public class DeleteCommand extends Command {
    private final Task task;
    public DeleteCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskManager tm) {
        tm.delete(this.task);
        this.response = Ui.DELETED_TASK + task.getDescription();
    }
}
