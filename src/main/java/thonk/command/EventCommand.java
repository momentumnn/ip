package thonk.command;

import thonk.core.TaskManager;
import thonk.core.Ui;
import thonk.task.Event;

/**
 * Represents a command which creates a Event task in the task list.
 */
public class EventCommand extends Command {
    private final String taskName;
    private final String startTime;
    private final String endTime;
    /**
     * Returns an EventCommand with the specified taskName and date.
     *
     * @param taskName Name of event as a String.
     * @param startTime From date of event LocalDateTime.
     * @param endTime   LocalDateTime.
     */
    public EventCommand(String taskName, String startTime, String endTime) {
        this.taskName = taskName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void execute(TaskManager tm) {
        Event task = new Event(this.taskName, this.startTime, this.endTime);
        tm.add(task);
        this.response = Ui.ADDED_TASK + task.getDescription();
    }
}
