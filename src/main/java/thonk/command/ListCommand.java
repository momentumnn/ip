package thonk.command;

import java.util.ArrayList;

import thonk.core.TaskManager;
import thonk.task.Task;

/**
 * Represents a command which lists all tasks in the task list.
 */
public class ListCommand extends Command {
    public ListCommand() {
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
        String output = "";
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
