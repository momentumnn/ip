package thonk.core;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import thonk.ThonkException;
import thonk.command.Command;

/**
 * The class UI controls the system.in and system.out of Thonk.
 */
public class Ui {
    public static final String ADDED_TASK = "Added task: ";
    public static final String DELETED_TASK = "Noted with thanks, \nsay bye bye to ";
    private final Scanner in;
    private final PrintStream out;
    private final TaskManager taskManager;
    /**
     * Creates a new class of UI
     */
    public Ui() {
        this(System.in, System.out, new TaskManager());
    }

    private Ui(InputStream in, PrintStream out, TaskManager taskManager) {
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
            Command output = Parser.parse(input, taskManager);
            output.execute(taskManager);
            return output.toString();
        } catch (IndexOutOfBoundsException e) {
            return "Too many arguments.";
        } catch (ThonkException e) {
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
}
