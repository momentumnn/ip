import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Thonk {
    private TaskManager taskManager;
    private UI ui;


    public static void main(String[] args) throws IOException {
        new Thonk().run(args);
    }
    public void run(String[] args) throws IOException {
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

    public void echo() throws IOException {
        Task newTask;
        while (true) {
            try {
                // full command
                String task = ui.getNextLine();
                // stripped command, left with everything after command.
                // e.g "deadline buy bread" into "buy bread"
                String[] taskSplit = task.split(" ", 2);
                Command command = Command.valueOf(taskSplit[0].toUpperCase());
                String[] taskDetails;
                switch (command) {
                case BYE:
                    return;
                case LIST:
                    ui.list(taskManager.getTasks());
                    break;
                case MARK, UNMARK:
                    mark(task);
                    break;
                case TODO:
                    if (taskSplit.length == 1 || taskSplit[1].isBlank()) {
                        throw new IncompleteCommandException("todo");
                    }
                    newTask = new Todo(taskSplit[1]);
                    add(newTask);
                    break;
                case DEADLINE:
                    taskDetails = taskSplit[1].split("/by");
                    String dateRegex = "(19|20)\\d{2}([/\\-])(0[1-9]|1[1,2]|[1-9])([/\\-])(0[1-9]|[12][0-9]|3[01])";
                    if (taskDetails.length == 1) {
                        throw new IncompleteCommandException("deadline");
                    }
                    if (!taskDetails[1].trim().matches(dateRegex)) {
                        throw new ThonkException("Please make sure it is in YYYY-MM-DD format");
                    }
                    newTask = new Deadline(taskDetails[0], taskDetails[1]);
                    add(newTask);
                    break;
                case EVENT:
                    taskDetails = taskSplit[1].split("/from|/to");
                    if (taskDetails.length != 3) {
                        throw new IncompleteCommandException("event");
                    }
                    newTask = new Event(taskDetails[0], taskDetails[1], taskDetails[2]);
                    add(newTask);
                    break;
                case DELETE:
                    String taskIndex = task.split(" ")[1];
                    newTask = getTask(taskIndex);
                    delete(newTask);
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
    private Task getTask(String index) {
        return taskManager.getTasks().get(Integer.parseInt(index) - 1);
    }


    public void add(Task task) {
        taskManager.add(task);
        ui.add(task, taskManager.getTasks());
    }

    public void delete(Task task) {
        taskManager.delete(task);
        ui.delete(task);
        ui.list(taskManager.getTasks());
    }

    public void mark(String task) {
        ArrayList<Task> pastTasks = taskManager.getTasks();
        int max = pastTasks.size();
        String regex = "[1-" + max + "]";
        String[] taskSplit = task.split(" ");
        if (taskSplit.length == 1) {
            throw new ThonkException("Include task number la wlao");
        }
        if (!taskSplit[1].matches(regex)) {
            throw new ThonkException("out of bounds");
        }
        Task currentTask = pastTasks.get(Integer.parseInt(task.split(" ")[1]) - 1);
        currentTask.setDone(taskSplit[0].equals("mark"));
        if (currentTask.getDone()) {
            System.out.println("ok slay its done\n" + currentTask);
        } else {
            System.out.println("not marked\n" + currentTask);
        }
        System.out.println();
    }
}
