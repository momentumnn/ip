package thonk.command;

import thonk.core.TaskManager;
import thonk.core.Ui;
import thonk.task.Todo;

public class TodoCommand extends Command {
    private final String taskName;
    public TodoCommand(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void execute(TaskManager tm) {
        Todo task = new Todo(this.taskName);
        tm.add(task);
        this.response = Ui.ADDED_TASK + task.getDescription();
    }
}
