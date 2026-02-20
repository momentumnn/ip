package thonk.command;

import thonk.core.TaskManager;

/**
 * Represents a command which exits the program.
 */
public class ByeCommand extends Command {
    public ByeCommand() {
        this.response = "bye";
    }

    @Override
    public void execute(TaskManager tm) {
    }
}
