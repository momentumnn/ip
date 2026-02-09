package thonk.core;

import thonk.ThonkException;

/**
 * The main driver class for the Thonk application
 * Thonk is a CLI-based task management tool, that allows users to track todos,
 * deadlines, and events through a simple text interface
 */
public class Thonk {
    private UI ui;

    Thonk() {
        this.ui = new UI();
    }
    public static void main(String[] args) {
        new Thonk().run(args);
    }

    /**
     * Instantiate the full thonk program.
     * @param args takes in file names from user input.
     */
    public String run(String... args) {
        start(args);
        return ui.banner();
        //echo();
        //ui.goodbye();
    }

    private void start(String[] args) {
        ui = new UI();
        ui.banner();
    }

    private TaskManager initTaskManager(String[] args) {
        boolean isStorageFileSpecifiedByUser = args.length > 0;
        TaskManager taskManager;
        if (isStorageFileSpecifiedByUser) {
            taskManager = new TaskManager(args[0]);
        } else {
            taskManager = new TaskManager();
        }
        assert taskManager != null : "Task Manager is not instantiated";
        return taskManager;
    }

    private void echo() {
        while (true) {
            try {
                String input = ui.getNextLine();
                String response = ui.getResponse(input);
                if (response.isBlank()) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                ui.print("Invalid command");
            } catch (ThonkException e) {
                ui.print(e.getMessage());
            }
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return ui.getResponse(input);
    }
}
