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
    final String DATE_REGEX = "(19|20)\\d{2}([/\\-])(0[1-9]|1[0-2]|[1-9])([/\\-])(0[1-9]|[12][0-9]|3[01])";

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
        switch (command) {
        case TODO:
            if (taskSplit.length == 1 || taskSplit[1].isBlank()) {
                throw new IncompleteCommandException("todo");
            }
            task = new Todo(taskSplit[1]);
            break;
        case DEADLINE:
            taskDetails = taskSplit[1].split("/by");
            if (taskDetails.length == 1) {
                throw new IncompleteCommandException("deadline");
            }
            if (!taskDetails[1].trim().matches(DATE_REGEX)) {
                throw new ThonkException("Please make sure it is in YYYY-MM-DD format");
            }
            task = new Deadline(taskDetails[0], taskDetails[1]);
            break;
        case EVENT:
            taskDetails = taskSplit[1].split("/from|/to");
            if (taskDetails.length != 3) {
                throw new IncompleteCommandException("event");
            }
            task = new Event(taskDetails[0], taskDetails[1], taskDetails[2]);
            break;
        case MARK, UNMARK, DELETE:
            int max = tm.getTasks().size();
            String regex = "[1-" + max + "]";
            String[] taskS = input.split(" ");
            if (taskS.length == 1) {
                throw new ThonkException("Include task number please");
            }
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
