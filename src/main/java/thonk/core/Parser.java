package thonk.core;

import thonk.command.Command;
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
    static final String DEADLINELIMITER = "/by";
    static final String EVENTLIMITER = "/from|/to";
    /**
     * Parses the string input of text and converts it into a pair variable of command and task
     * @param input String input of the text
     * @param tm Taskmanager that the tasks are saved to, used to grab previous tasks
     * @return Pair<\Command, Task> of the input. Task is null for commands that don't target a specific task.
     */
    static Pair<Command, Task> parse(String input, TaskManager tm) {
        if (input == null || input.isBlank()) {
            throw new ThonkException("Input cannot be empty");
        }
        if (tm == null) {
            throw new ThonkException("Task manager is not available");
        }
        String[] taskSplit = input.trim().split("\\s+", 2);
        Command command = Command.fromString(taskSplit[0]);
        return switch (command) {
        case TODO -> new Pair<>(command, createTodo(requireArgs(command, taskSplit)));
        case DEADLINE -> new Pair<>(command, createDeadline(requireArgs(command, taskSplit)));
        case EVENT -> new Pair<>(command, createEvent(requireArgs(command, taskSplit)));
        case MARK, UNMARK, DELETE -> new Pair<>(command, findTask(input, tm));
        case LIST, BYE, FIND, UNKNOWN -> new Pair<>(command, null);
        default -> throw new ThonkException("Unsupported command: " + command);
        };
    }
    private static String requireArgs(Command command, String[] parts) {
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IndexOutOfBoundsException("Missing arguments for command: " + command);
        }
        return parts[1].trim();
    }
    private static Todo createTodo(String arg) {
        return new Todo(arg);
    }
    private static Deadline createDeadline(String arg) {
        String[] taskDetails = arg.split(DEADLINELIMITER);
        String taskToAdd = taskDetails[0].trim();
        String taskEndTime = taskDetails[1].trim();
        return new Deadline(taskToAdd, taskEndTime);
    }
    private static Event createEvent(String arg) {
        String[] taskDetails = arg.split(EVENTLIMITER, 3);
        String taskToAdd = taskDetails[0].trim();
        String taskStartTime = taskDetails[1];
        String taskEndTime = taskDetails[2];
        return new Event(taskToAdd, taskStartTime, taskEndTime);
    }
    private static Task findTask(String arg, TaskManager tm) {
        int max = tm.getTasks().size();
        String regex = "[1-" + max + "]";
        String[] taskIndex = arg.split(" ");
        if (!taskIndex[1].matches(regex)) {
            throw new ThonkException("out of bounds");
        }
        return tm.getTasks()
                .get(Integer.parseInt(arg.split(" ")[1]) - 1);
    }
}
