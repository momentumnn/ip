package thonk.command;

import thonk.core.TaskManager;

public abstract class Command {
    protected String response;
    public abstract void execute(TaskManager tm );

    @Override
    public String toString() {
        return response;
    }
}