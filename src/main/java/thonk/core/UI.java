package thonk.core;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import thonk.Task;

public class UI {
    private final Scanner in;
    private final PrintStream out;

    public UI() {
        this(System.in, System.out);
    }

    public UI(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }
    public String getNextLine() {
        return in.nextLine();
    }
    public void print(String text) {
        out.println(text);
    }
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
    public void goodbye() {
        out.println("Good bye");
    }
    public void add(Task task, int size) {
        out.println("Noted with thanks, \nadded " + task.getDescription() + " to ur list\nCurrently u have " + size
                + " of stuff");
    }
    public void delete(Task task) {
        out.println("Noted with thanks, \nsay bye bye to " + task.getDescription() + " from ur list");
    }
    public void mark(Task task) {
        if (task.getDone()) {
            out.println("ok slay its done\n" + task);
        } else {
            out.println("not marked\n" + task);
        }
    }
}
