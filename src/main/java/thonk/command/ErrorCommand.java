package thonk.command;

import thonk.core.TaskManager;
/**
 * Represents a command that encapsulates an error message.
 * This command is returned when user input cannot be parsed or when
 * validation fails. When executed, it simply returns the error message
 * without modifying the task list.
 */
public class ErrorCommand extends Command {
    public ErrorCommand(String response) {
        this.response = response;
    }

    @Override
    public void execute(TaskManager tm) {
    }
}
