package thonk.command;

import thonk.core.TaskManager;
import thonk.core.Ui;
import thonk.task.Deadline;

/**
 * Represents a command which creates a Deadline task in the task list.
 */
public class DeadlineCommand extends Command {
    private final String taskName;
    private final String deadline;
    /**
     * Constructs a {@code DeadlineCommand}.
     *
     * @param taskName  description/title of the task
     * @param deadline  deadline value (e.g. a date string) for the task
     */
    public DeadlineCommand(String taskName, String deadline) {
        this.taskName = taskName;
        this.deadline = deadline;
    }

    @Override
    public void execute(TaskManager tm) {
        Deadline task = new Deadline(this.taskName, this.deadline);
        tm.add(task);
        this.response = Ui.ADDED_TASK + task.getDescription();
    }
}
