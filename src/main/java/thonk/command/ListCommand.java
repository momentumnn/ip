package thonk.command;

import java.util.ArrayList;

import thonk.core.TaskManager;
import thonk.task.Task;

/**
 * Represents a command which creates a Deadline task in the task list.
 */
public class ListCommand extends Command {
    private final TaskManager tm;
    public ListCommand(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void execute(TaskManager tm) {
        this.response = list(tm.getTasks());
    }
    /**
     * Prints the current list of tasks within pastTasks
     * @param pastTasks ArrayList of tasks
     */
    public String list(ArrayList<Task> pastTasks) {
        String output = new String();
        if (pastTasks.isEmpty()) {
            output = "There are no past tasks";
        }
        assert !pastTasks.isEmpty();
        int i = 1;
        for (Task task: pastTasks) {
            output = output.concat(i + ". " + task + "\n");
            i++;
        }
        return output;
    }

}
