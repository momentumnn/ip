package thonk.core;

import thonk.Command;
import thonk.Deadline;
import thonk.Event;
import thonk.IncompleteCommandException;
import thonk.Pair;
import thonk.Task;
import thonk.ThonkException;
import thonk.Todo;

/**
 * Represents a parsing interface
 */
public interface Parser {

    /**
     * Parses the string input of text and converts it into a pair variable of command and task
     * @param input String input of the text
     * @param tm Taskmanager that the tasks are saved to, used to grab previous tasks
     * @return Pair<\Command, Task> of the input. Returns null Command if list, bye or unknown
     */
    static Pair<Command, Task> parse(String input, TaskManager tm) {
        String[] taskSplit = input.split(" ", 2);
        Command command = Command.fromString(taskSplit[0]);
        Task task = null;
        String[] taskDetails;
        String taskToAdd;
        String taskStartTime;
        String taskEndTime;
        switch (command) {
        case TODO:
            try {
                taskToAdd = taskSplit[1].trim();
            } catch (IndexOutOfBoundsException e) {
                throw new IncompleteCommandException(e.getMessage());
            }
            task = new Todo(taskToAdd);
            break;
        case DEADLINE:
            taskDetails = taskSplit[1].split("/by");
            taskToAdd = taskDetails[0].trim();
            taskEndTime = taskDetails[1].trim();
            task = new Deadline(taskToAdd, taskEndTime);
            break;
        case EVENT:
            taskDetails = taskSplit[1].split("/from|/to");
            taskToAdd = taskDetails[0].trim();
            taskStartTime = taskDetails[1];
            taskEndTime = taskDetails[2];
            task = new Event(taskToAdd, taskStartTime, taskEndTime);
            break;
        case MARK, UNMARK, DELETE:
            int max = tm.getTasks().size();
            String regex = "[1-" + max + "]";
            String[] taskS = input.split(" ");
            if (!taskS[1].matches(regex)) {
                throw new ThonkException("out of bounds");
            }
            task = tm.getTasks().get(Integer.parseInt(input.split(" ")[1]) - 1);
            break;
        case LIST, BYE, FIND, UNKNOWN:
            break;
        default:
            break;
        }
        return new Pair<>(command, task);

    }
}
