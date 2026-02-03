package thonk.core;

import java.util.ArrayList;

import thonk.Command;
import thonk.Pair;
import thonk.Task;
import thonk.ThonkException;

/**
 * The main driver class for the Thonk application
 * Thonk is a CLI-based task management tool, that allows users to track todos,
 * deadlines, and events through a simple text interface
 */
public class Thonk {
    private TaskManager taskManager;
    private UI ui;


    public static void main(String[] args) {
        new Thonk().run(args);
    }

    /**
     * Instantiate the full thonk program.
     * @param args takes in file names from user input.
     */
    public void run(String[] args) {
        start(args);
        echo();
        ui.goodbye();
    }

    private void start(String[] args) {
        ui = new UI();
        taskManager = initTaskManager(args);
        ui.banner();
    }

    private TaskManager initTaskManager(String[] args) {
        boolean isStorageFileSpecifiedByUser = args.length > 0;
        return isStorageFileSpecifiedByUser ? new TaskManager(args[0]) : new TaskManager();
    }

    private void echo() {
        while (true) {
            try {
                String input = ui.getNextLine();
                Pair<Command, Task> output = Parser.parse(input, taskManager);
                Task task = output.u();
                Command command = output.t();
                switch (command) {
                case BYE:
                    return;
                case LIST:
                    ui.list(taskManager.getTasks());
                    break;
                case MARK, UNMARK:
                    taskManager.mark(task, command.equals(Command.MARK));
                    ui.mark(task);
                    break;
                case TODO, DEADLINE, EVENT:
                    add(task);
                    break;
                case DELETE:
                    delete(task);
                    break;
                case FIND:
                    find(input.split(" ",2)[1]);
                    break;
                default:
                    throw new ThonkException("U entered something wong");
                }
            } catch (IllegalArgumentException e) {
                ui.print("Invalid command");
            } catch (ThonkException e) {
                ui.print(e.getMessage());
            }
        }
    }
    private void add(Task task) {
        taskManager.add(task);
        ui.add(task, taskManager.getTasks().size());
    }

    private void delete(Task task) {
        taskManager.delete(task);
        ui.delete(task);
        ui.list(taskManager.getTasks());
    }

    public void find(String text) {
        ArrayList<Task> matchingTasks = taskManager.find(text);
        ui.list(matchingTasks);
    }
}
