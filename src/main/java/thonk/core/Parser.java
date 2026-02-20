package thonk.core;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import thonk.ThonkException;
import thonk.command.Command;
import thonk.command.DeadlineCommand;
import thonk.command.ErrorCommand;
import thonk.command.EventCommand;
import thonk.command.ListCommand;
import thonk.command.MarkCommand;
import thonk.command.TodoCommand;
import thonk.command.UnmarkCommand;
import thonk.task.Deadline;
import thonk.task.Event;
import thonk.task.Task;
import thonk.task.Todo;


/**
 * Represents a parsing interface
 */
public interface Parser {
    static final String DEADLINELIMITER = "/by";
    static final String EVENTLIMITER = "/from|/to";
    static final String MISSING_TASK = "Task is not in list";

    // Centralize user-facing messages to reduce magic strings
    static final String MISSING_ARGUMENTS_MESSAGE =
            "Missing arguments for command, please try again.";
    static final String INVALID_DEADLINE_FORMAT_MESSAGE =
            "Invalid deadline format. Use: deadline <task> /by <YYYY-MM-DD>";
    static final String DEADLINE_DESCRIPTION_EMPTY_MESSAGE =
            "Deadline description cannot be empty. Use: deadline <task> /by <YYYY-MM-DD>";
    static final String DEADLINE_DATE_EMPTY_MESSAGE =
            "Deadline date cannot be empty. Use: deadline <task> /by <YYYY-MM-DD>";
    static final String INVALID_ISO_DATE_MESSAGE =
            "Invalid date. Use ISO format: YYYY-MM-DD";
    static final String INVALID_EVENT_FORMAT_MESSAGE =
            "Invalid event format. Use: event \"<taskname>\" /from \"<start>\" /to \"<end>\"";
    static final String EVENT_NAME_EMPTY_MESSAGE =
            "Event name cannot be empty. Use: event \"<taskname>\" /from \"<start>\" /to \"<end>\"";
    static final String EVENT_START_EMPTY_MESSAGE =
            "Event start time cannot be empty. Use: event \"<taskname>\" /from \"<start>\" /to \"<end>\"";
    static final String EVENT_END_EMPTY_MESSAGE =
            "Event end time cannot be empty. Use: event \"<taskname>\" /from \"<start>\" /to \"<end>\"";

    /**
     * Parses the string input of text and converts it into a pair variable of command and task
     * @param input String input of the text
     * @param tm Taskmanager that the tasks are saved to, used to grab previous tasks
     * @return Pair<\Command, Task> of the input. Task is null for commands that don't target a specific task.
     */
    static Command parse(String input, TaskManager tm) {
        if (input == null || input.isBlank()) {
            return new ErrorCommand("Input cannot be empty");
        }
        if (tm == null) {
            throw new ThonkException("Task manager is not available");
        }
        String[] taskSplit = input.trim().split("\\s+", 2);
        String command = taskSplit[0].toUpperCase();
        String description = (taskSplit.length < 2) ? "" : taskSplit[1].trim();
        return switch (command) {
        case "TODO" -> getTodoCommand(requireArgs(description));
        case "DEADLINE" -> getDeadlineCommand(requireArgs(description));
        case "EVENT" -> getEventCommand(requireArgs(description));
        case "MARK" -> getMarkCommand(description, tm);
        case "UNMARK" -> getUnmarkCommand(description, tm);
        case "DELETE" -> getDeleteCommand(description);
        case "FIND" -> getFindCommand(description);
        case "BYE" -> getByeCommand();
        case "LIST" -> getListCommand(tm);
        case "UNKNOWN" -> getUnknownCommand();
        default -> throw new ThonkException("Unsupported command: " + command);
        };
    }
    private static Command getTodoCommand(String arg) {
        return new TodoCommand(arg);
    }
    private static Command getDeadlineCommand(String arg) {
        // Expect: "<desc> /by <date>"
        String[] parts = arg.split(DEADLINELIMITER, 2);
        if (parts.length < 2) {
            return new ErrorCommand(INVALID_DEADLINE_FORMAT_MESSAGE);
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isBlank()) {
            return new ErrorCommand(DEADLINE_DESCRIPTION_EMPTY_MESSAGE);
        }
        if (by.isBlank()) {
            return new ErrorCommand(DEADLINE_DATE_EMPTY_MESSAGE);
        }
        if (!isLocalDate(by)) {
            return new ErrorCommand(INVALID_ISO_DATE_MESSAGE);
        }
        // Let the Deadline constructor validate/parse the date (and throw if invalid)
        return new DeadlineCommand(description, by);
    }
    private static Command getEventCommand(String arg) {

        String trimmed = arg == null ? "" : arg.trim();

        String[] parts = trimmed.split("\\s*/from\\s*", 2);
        if (parts.length < 2) {
            return new ErrorCommand(INVALID_EVENT_FORMAT_MESSAGE);
        }

        String taskName = parts[0].trim();
        String rest = parts[1].trim();

        String[] timeParts = rest.split("\\s*/to\\s*", 2);
        if (timeParts.length < 2) {
            return new ErrorCommand(INVALID_EVENT_FORMAT_MESSAGE);
        }

        String start = timeParts[0].trim();
        String end = timeParts[1].trim();

        if (taskName.isBlank()) {
            return new ErrorCommand(EVENT_NAME_EMPTY_MESSAGE);
        }
        if (start.isBlank()) {
            return new ErrorCommand(EVENT_START_EMPTY_MESSAGE);
        }
        if (end.isBlank()) {
            return new ErrorCommand(EVENT_END_EMPTY_MESSAGE);
        }

        return new EventCommand(taskName, start, end);
    }
    private static Command getMarkCommand(String arg, TaskManager tm) {
        return new MarkCommand(findTask(arg, tm));
    }
    private static Command getUnmarkCommand(String arg, TaskManager tm) {
        return new UnmarkCommand(findTask(arg, tm));
    }
    private static Command getDeleteCommand(String arg) {
        return new TodoCommand(arg);
    }
    private static Command getFindCommand(String arg) {
        return new TodoCommand(arg);
    }
    private static Command getByeCommand() {
        return new TodoCommand("asdf");
    }
    private static Command getListCommand(TaskManager tm) {
        return new ListCommand(tm);
    }
    private static Command getUnknownCommand() {
        return new TodoCommand("arg");
    }


    private static boolean isLocalDate(String date) {
        try {
            LocalDate.parse(date); // ISO_LOCAL_DATE by default: YYYY-MM-DD
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static String requireArgs(String args) {
        if (args == null || args.isBlank()) {
            throw new ThonkException(MISSING_ARGUMENTS_MESSAGE);
        }
        return args.trim();
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
