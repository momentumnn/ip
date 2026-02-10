package thonk;

/**
 * Represents a Deadline task, extended from Task.
 */
public class Todo extends Task {
    /**
     * Creates an instance of Todo.
     * @param description Description of Todo task
     */
    public Todo(String description) {
        super(description);
    }
    /**
     * Creates an instance of Todo, with isDone value.
     * @param description Description of Todo task
     * @param isDone Whether todo is done yet.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns a save text version of todo.
     * Example: D;0;new task;18/12/2002
     */
    @Override
    public String toSave(String splitChar) {
        return "T" + splitChar + isDone + splitChar + description;
    }
}

