package thonk.command;

import thonk.core.TaskManager;
/**
 * Base type for all executable user commands in Thonk.
 * <p>
 * A {@code Command} encapsulates a single user intention (e.g., add, delete, list) and can be executed
 * against a {@link TaskManager}. Implementations typically set {@link #response} during execution so the
 * caller (UI/main loop) can display feedback to the user.
 * </p>
 */
public abstract class Command {
    /**
     * User-facing message produced by the command after execution.
     * <p>
     * Subclasses should populate this value inside {@link #execute(TaskManager)}.
     * </p>
     */
    protected String response;
    public abstract void execute(TaskManager tm);
    /**
     * Returns the command's user-facing response.
     * <p>
     * Intended for displaying command results without exposing internal command state.
     * </p>
     *
     * @return response string (may be {@code null} if the command did not set one)
     */
    @Override
    public String toString() {
        return response;
    }
}
