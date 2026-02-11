package thonk.core;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import thonk.Command;
import thonk.Pair;
import thonk.Task;
import thonk.ThonkException;

/**
 * The class UI controls the system.in and system.out of Thonk.
 */
public class UI {
    private final Scanner in;
    private final PrintStream out;
    private final TaskManager taskManager;

    /**
     * Creates a new class of UI
     */
    public UI() {
        this(System.in, System.out, new TaskManager());
    }

    private UI(InputStream in, PrintStream out, TaskManager taskManager) {
        this.in = new Scanner(in);
        this.out = out;
        this.taskManager = taskManager;
    }

    /**
     * Returns the next line received from system.in
     * @return String of next line
     */
    public String getNextLine() {
        return in.nextLine();
    }
    public String getResponse(String input) {
        try {
            input = input.trim();
            Pair<Command, Task> output = Parser.parse(input, taskManager);
            Task task = output.u();
            Command command = output.t();
            switch (command) {
            case BYE:
                return "";
            case LIST:
                return this.list(taskManager.getTasks());
            case MARK, UNMARK:
                taskManager.mark(task, command.equals(Command.MARK));
                return this.mark(task);
            case TODO, DEADLINE, EVENT:
                checkTaskExists(task, taskManager);
                taskManager.add(task);
                return this.add(task, taskManager.getTasks().size());
            case DELETE:
                taskManager.delete(task);
                return this.delete(task) + this.list(taskManager.getTasks());
            case FIND:
                String matchingText = input.split(" ")[1];
                ArrayList<Task> matchingTasks = taskManager.find(matchingText);
                return this.list(matchingTasks);
            default:
                throw new ThonkException("U entered something wong");
            }
        } catch (Exception e) {
            return e.getMessage();
        }

    }
    /**
     * Prints the text loaded.
     * @param text text to be printed.
     */
    public String print(String text) {
        out.println(text);
        return text;
    }

    /**
     * Prints banner
     */
    public String banner() {
        String divider = "_______________________________\n\n";
        String logo = """
                 _____ _                 _
                |_   _| |__   ___  _ __ | | __
                  | | | '_ \\ / _ \\| '_ \\| |/ /
                  | | | | | | (_) | | | |   <
                  |_| |_| |_|\\___/|_| |_|_|\\_\\
                """;
        String text = "Hello from\n" + logo + "what u want \n" + divider;
        return this.print(text);
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
        return this.print(output);
    }

    /**
     * Prints default text after adding a task. Also states how many tasks there are.
     * @param task task that is created
     * @param size number of tasks in arrayList
     */
    public String add(Task task, int size) {
        return this.print("Noted with thanks, \nadded " + task.getDescription() + " to ur list\nCurrently u have "
                + size + " of stuff");
    }
    /**
     * Prints default text after deleting a task.
     * @param task task that is deleted
     */
    public String delete(Task task) {
        return this.print("Noted with thanks, \nsay bye bye to " + task.getDescription() + " from ur list");
    }

    /**
     * Prints default text after marking a task.
     * @param task task that is marked
     */
    public String mark(Task task) {
        if (task.getDone()) {
            return this.print("ok slay its done\n" + task);
        } else {
            return this.print("not marked\n" + task);
        }
    }
    private void checkTaskExists(Task task, TaskManager tm) {
        boolean taskExists = !tm.find(task.getDescription()).isEmpty();
        if (taskExists) {
            throw new ThonkException("Task " + task.getDescription() + " exists");
        }
    }
}
