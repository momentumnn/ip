package thonk.core;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import thonk.ThonkException;
import thonk.command.ByeCommand;
import thonk.command.Command;
import thonk.command.DeadlineCommand;
import thonk.command.DeleteCommand;
import thonk.command.ErrorCommand;
import thonk.command.EventCommand;
import thonk.command.FindCommand;
import thonk.command.ListCommand;
import thonk.command.MarkCommand;
import thonk.command.TodoCommand;
import thonk.command.UnmarkCommand;
import thonk.task.Task;
/**
 * Parses user input strings into executable {@link Command} objects.
 * <p>
 * The parser is responsible for:
 * </p>
 * <ul>
 *   <li>Extracting the command word (first token) and the remaining arguments.</li>
 *   <li>Validating required arguments and simple command-specific formats.</li>
 *   <li>Creating the corresponding {@link Command} instance to be executed later.</li>
 * </ul>
 *
 * <h2>Input format</h2>
 * <ul>
 *   <li>{@code todo <description>}</li>
 *   <li>{@code deadline <description> /by <YYYY-MM-DD>}</li>
 *   <li>{@code event <name> /from <start> /to <end>}</li>
 *   <li>{@code mark <index>} / {@code unmark <index>}</li>
 *   <li>{@code list}, {@code bye}</li>
 * </ul>
 *
 * <h2>Error handling</h2>
 * <ul>
 *   <li>If the input is {@code null} or blank, {@link #parse(String, TaskManager)} returns an {@link ErrorCommand}.
 *   </li>
 *   <li>If required arguments are missing, {@link #parse(String, TaskManager)} throws {@link ThonkException}.</li>
 *   <li>If a command-specific format is invalid (e.g. missing {@code /by}), parsing returns an {@link ErrorCommand}
 *       with a user-friendly message.</li>
 *   <li>If an index is malformed or out of range for {@code mark}/{@code unmark}, parsing
 *   throws {@link ThonkException}.</li>
 * </ul>
 */

public interface Parser {
    String DEADLINE_DELIMITER = "/by";
    String INPUT_EMPTY_MESSAGE = "Input cannot be empty";
    String OUT_OF_BOUNDS_MESSAGE = "out of bounds";
    String DEADLINE_FORMAT = "Use: deadline <task> /by <YYYY-MM-DD>";
    String EVENT_FORMAT = "Use: event <task> /from <start> /to <end>";
    // Centralize user-facing messages to reduce magic strings
    String MISSING_ARGUMENTS_MESSAGE =
            "Missing arguments for command, please try again.";
    String INVALID_DEADLINE_FORMAT_MESSAGE =
            "Invalid deadline format. " + DEADLINE_FORMAT;
    String DEADLINE_DESCRIPTION_EMPTY_MESSAGE =
            "Deadline description cannot be empty. " + DEADLINE_FORMAT;
    String DEADLINE_DATE_EMPTY_MESSAGE =
            "Deadline date cannot be empty. " + DEADLINE_FORMAT;
    String INVALID_ISO_DATE_MESSAGE =
            "Invalid date. Use ISO format: YYYY-MM-DD";
    String INVALID_EVENT_FORMAT_MESSAGE =
            "Invalid event format. " + EVENT_FORMAT;
    String EVENT_NAME_EMPTY_MESSAGE =
            "Event name cannot be empty. " + EVENT_FORMAT;
    String EVENT_START_EMPTY_MESSAGE =
            "Event start time cannot be empty. " + EVENT_FORMAT;
    String EVENT_END_EMPTY_MESSAGE =
            "Event end time cannot be empty. " + EVENT_FORMAT;
    String TASK_ALREADY_EXISTS_MESSAGE = "Task already exists.";
    String UNKNOWN_COMMAND_MESSAGE = "Unknown command.";

    String FROM_TOKEN = "/from";
    String TO_TOKEN = "/to";

    /**
     * Parses the string input of text and converts it into a pair variable of command and task
     * @param input String input of the text
     * @param tm Taskmanager that the tasks are saved to, used to grab previous tasks
     * @return Pair<\Command, Task> of the input. Task is null for commands that don't target a specific task.
     */
    static Command parse(String input, TaskManager tm) {
        if (input == null || input.isBlank()) {
            return new ErrorCommand(INPUT_EMPTY_MESSAGE);
        }
        if (tm == null) {
            throw new ThonkException("Task manager is not available");
        }

        String[] split = splitCommandWordAndArgs(input);
        String commandWord = split[0].toUpperCase();
        String args = split[1];

        return switch (commandWord) {
        case "TODO" -> getTodoCommand(requireArgs(args), tm);
        case "DEADLINE" -> getDeadlineCommand(requireArgs(args), tm);
        case "EVENT" -> getEventCommand(requireArgs(args), tm);
        case "MARK" -> getMarkCommand(requireArgs(args), tm);
        case "UNMARK" -> getUnmarkCommand(requireArgs(args), tm);
        case "DELETE" -> getDeleteCommand(requireArgs(args), tm);
        case "FIND" -> getFindCommand(requireArgs(args));
        case "BYE" -> getByeCommand();
        case "LIST" -> getListCommand();
        case "UNKNOWN" -> getUnknownCommand();
        default -> throw new ThonkException("Unsupported command: " + commandWord);
        };
    }

    private static String[] splitCommandWordAndArgs(String input) {
        String[] taskSplit = input.trim().split("\\s+", 2);
        String commandWord = taskSplit[0];
        String args = (taskSplit.length < 2) ? "" : taskSplit[1].trim();
        return new String[] { commandWord, args };
    }

    private static Command getTodoCommand(String arg, TaskManager tm) {
        if (checkTaskExists(arg, tm)) {
            return new ErrorCommand(TASK_ALREADY_EXISTS_MESSAGE);
        }
        return new TodoCommand(arg);
    }

    private static Command getDeadlineCommand(String arg, TaskManager tm) {
        // Expect: "<desc> /by <date>"
        String[] parts = arg.split(DEADLINE_DELIMITER, 2);
        if (parts.length < 2) {
            return new ErrorCommand(INVALID_DEADLINE_FORMAT_MESSAGE);
        }

        String description = parts[0].trim();
        String by = parts[1].trim();
        Command error = validateDeadline(description, by);
        if (error != null) {
            return error;
        }
        if (checkTaskExists(arg, tm)) {
            return new ErrorCommand(TASK_ALREADY_EXISTS_MESSAGE);
        }

        return new DeadlineCommand(description, by);
    }
    private static Command validateDeadline(String desc, String by) {
        if (desc.isBlank()) {
            return new ErrorCommand(DEADLINE_DESCRIPTION_EMPTY_MESSAGE);
        }
        if (by.isBlank()) {
            return new ErrorCommand(DEADLINE_DATE_EMPTY_MESSAGE);
        }
        if (isNotLocalDate(by)) {
            return new ErrorCommand(INVALID_ISO_DATE_MESSAGE);
        }
        return null;
    }

    private static Command getEventCommand(String arg, TaskManager tm) {
        // Level 1: Orchestration
        String[] parts = extractEventParts(arg.trim());

        if (parts == null) {
            return new ErrorCommand(INVALID_EVENT_FORMAT_MESSAGE);
        }

        String name = parts[0];
        String start = parts[1];
        String end = parts[2];
        if (checkTaskExists(arg, tm)) {
            return new ErrorCommand(TASK_ALREADY_EXISTS_MESSAGE);
        }
        return validateAndBuildEvent(name, start, end);
    }

    private static String[] extractEventParts(String rawInput) {
        // Level 2: Low-level String Extraction
        String[] fromSplit = rawInput.split("\\s*" + FROM_TOKEN + "\\s*", 2);
        if (fromSplit.length < 2) {
            return null;
        }

        String[] toSplit = fromSplit[1].split("\\s*" + TO_TOKEN + "\\s*", 2);
        if (toSplit.length < 2) {
            return null;
        }
        return new String[] {
                fromSplit[0].trim(),
                toSplit[0].trim(),
                toSplit[1].trim()
        };
    }

    private static Command validateAndBuildEvent(String name, String start, String end) {
        // Level 3: Domain Validation Logic
        if (name.isBlank()) {
            return new ErrorCommand(EVENT_NAME_EMPTY_MESSAGE);
        }
        if (start.isBlank()) {
            return new ErrorCommand(EVENT_START_EMPTY_MESSAGE);
        }
        if (end.isBlank()) {
            return new ErrorCommand(EVENT_END_EMPTY_MESSAGE);
        }
        if (isNotLocalDate(start) || isNotLocalDate(end)) {
            return new ErrorCommand(INVALID_ISO_DATE_MESSAGE);
        }
        return new EventCommand(name, start, end);
    }
    private static Command getMarkCommand(String arg, TaskManager tm) {
        return new MarkCommand(findTask(arg, tm));
    }

    private static Command getUnmarkCommand(String arg, TaskManager tm) {
        return new UnmarkCommand(findTask(arg, tm));
    }

    private static Command getDeleteCommand(String arg, TaskManager tm) {
        return new DeleteCommand(findTask(arg, tm));
    }

    private static Command getFindCommand(String arg) {
        return new FindCommand(arg);
    }

    private static Command getByeCommand() {
        return new ByeCommand();
    }

    private static Command getListCommand() {
        return new ListCommand();
    }

    private static Command getUnknownCommand() {
        return new ErrorCommand(UNKNOWN_COMMAND_MESSAGE);
    }

    private static boolean isNotLocalDate(String date) {
        try {
            LocalDate.parse(date); // ISO_LOCAL_DATE by default: YYYY-MM-DD
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private static String requireArgs(String args) {
        if (args == null || args.isBlank()) {
            throw new ThonkException(MISSING_ARGUMENTS_MESSAGE);
        }
        return args.trim();
    }

    private static Task findTask(String arg, TaskManager tm) {
        final int index;
        try {
            index = Integer.parseInt(arg.trim());
        } catch (NumberFormatException e) {
            throw new ThonkException(OUT_OF_BOUNDS_MESSAGE);
        }

        if (index < 1 || index > tm.getTasks().size()) {
            throw new ThonkException(OUT_OF_BOUNDS_MESSAGE);
        }

        return tm.getTasks().get(index - 1);
    }
    private static boolean checkTaskExists(String task, TaskManager tm) {
        boolean taskExists = tm.isExistingTask(task);
        return taskExists;
    }

}
