package thonk.core;

import thonk.Command;
import thonk.Deadline;
import thonk.Event;
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
            return new Pair<>(command, createTodo(taskSplit[1]));
        case DEADLINE:
            return new Pair<>(command, createDeadline(taskSplit[1]));
        case EVENT:
            return new Pair<>(command, createEvent(taskSplit[1]));
        case MARK, UNMARK, DELETE:
            return new Pair<>(command, findTask(input, tm));
        case LIST, BYE, FIND, UNKNOWN:
            break;
        default:
            assert false : command;
            break;
        }
        return new Pair<>(command, task);

    }

    private static Todo createTodo(String arg) {
        return new Todo(arg);
    }
    private static Deadline createDeadline(String arg) {
        String[] taskDetails = arg.split("/by");
        String taskToAdd = taskDetails[0].trim();
        String taskEndTime = taskDetails[1].trim();
        return new Deadline(taskToAdd, taskEndTime);
    }
    private static Event createEvent(String arg) {
        String[] taskDetails = arg.split("/from|/to");
        String taskToAdd = taskDetails[0].trim();
        String taskStartTime = taskDetails[1];
        String taskEndTime = taskDetails[2];
        return new Event(taskToAdd, taskStartTime, taskEndTime);
    }
    private static Task findTask(String arg, TaskManager tm) {
        int max = tm.getTasks().size();
        String regex = "[1-" + max + "]";
        String[] taskS = arg.split(" ");
        if (!taskS[1].matches(regex)) {
            throw new ThonkException("out of bounds");
        }
        return tm.getTasks().get(Integer.parseInt(arg.split(" ")[1]) - 1);
    }
}
