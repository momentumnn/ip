package thonk.command;

import java.util.ArrayList;

import thonk.core.TaskManager;
import thonk.task.Task;

/**
 * Represents a command which finds a task in the task list.
 */
public class FindCommand extends Command {
    private final String name;
    public FindCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(TaskManager tm) {
        this.response = list(tm.find(this.name));
    }
    private String list(ArrayList<Task> pastTasks) {
        String output = "";
        if (pastTasks.isEmpty()) {
            output = "There are no past tasks";
        }
        int i = 1;
        for (Task task: pastTasks) {
            output = output.concat(i + ". " + task + "\n");
            i++;
        }
        return output;
    }

}
