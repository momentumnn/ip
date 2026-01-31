public class Thonk {
    private TaskManager taskManager;
    private UI ui;


    public static void main(String[] args) {
        new Thonk().run(args);
    }
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

    public void echo() {
        while (true) {
            try {
                String input = ui.getNextLine();
                Pair<Command, Task> output = Parser.parse(input, taskManager);
                String[] taskSplit = input.split(" ", 2);
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
    public void add(Task task) {
        taskManager.add(task);
        ui.add(task, taskManager.getTasks().size());
    }

    public void delete(Task task) {
        taskManager.delete(task);
        ui.delete(task);
        ui.list(taskManager.getTasks());
    }
}
