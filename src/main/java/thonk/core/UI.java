package thonk.core;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import thonk.Task;

/**
 * The class UI controls the system.in and system.out of Thonk.
 */
public class UI {
    private final Scanner in;
    private final PrintStream out;

    /**
     * Creates a new class of UI
     */
    public UI() {
        this(System.in, System.out);
    }

    private UI(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    /**
     * Returns the next line received from system.in
     * @return String of next line
     */
    public String getNextLine() {
        return in.nextLine();
    }

    /**
     * Prints the text loaded.
     * @param text text to be printed.
     */
    public void print(String text) {
        out.println(text);
    }

    /**
     * Prints banner
     */
    public void banner() {
        String divider = "_______________________________\n\n";
        String logo = """
                 _____ _                 _
                |_   _| |__   ___  _ __ | | __
                  | | | '_ \\ / _ \\| '_ \\| |/ /
                  | | | | | | (_) | | | |   <
                  |_| |_| |_|\\___/|_| |_|_|\\_\\
                """;
        out.println("Hello from\n" + logo + "what u want \n" + divider);
    }

    /**
     * Prints the current list of tasks within pastTasks
     * @param pastTasks ArrayList of tasks
     */
    public void list(ArrayList<Task> pastTasks) {
        if (pastTasks.isEmpty()) {
            out.println("There is nothing to do.");
            return;
        }
        int i = 1;
        for (Task task: pastTasks) {
            out.println(i + ". " + task);
            i++;
        }
    }

    /**
     * Prints goodbye text
     */
    public void goodbye() {
        out.println("Good bye");
    }

    /**
     * Prints default text after adding a task. Also states how many tasks there are.
     * @param task task that is created
     * @param size number of tasks in arrayList
     */
    public void add(Task task, int size) {
        out.println("Noted with thanks, \nadded " + task.getDescription() + " to ur list\nCurrently u have " + size
                + " of stuff");
    }
    /**
     * Prints default text after deleting a task.
     * @param task task that is deleted
     */
    public void delete(Task task) {
        out.println("Noted with thanks, \nsay bye bye to " + task.getDescription() + " from ur list");
    }

    /**
     * Prints default text after marking a task.
     * @param task task that is marked
     */
    public void mark(Task task) {
        if (task.getDone()) {
            out.println("ok slay its done\n" + task);
        } else {
            out.println("not marked\n" + task);
        }
    }
}
