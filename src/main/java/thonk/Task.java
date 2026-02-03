package thonk;

/**
 * Represents an abstract class Task for subclasses to follow
 * Currently used by Deadline, Event, Todo
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a Task with description and defaults isDone to false
     * @param description Task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Creates a Task with description and isDone value
     * @param description Task description
     * @param isDone boolean whether task is done
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }
    public abstract String toSave();

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }
    public String getDescription() {
        return this.description;
    }
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean getDone() {
        return isDone;
    }
}
