package thonk;

/**
 * Represents a Event task, extended from Task.
 */
public class Event extends Task {
    protected String startTime;
    protected String endTime;

    /**
     * Creates an instance of Event.
     * @param description Description of task
     * @param startTime Start time of event, in dd-mm-yyyy
     * @param endTime End time of event, in dd-mm-yyyy
     */
    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }
    /**
     * Creates an instance of Event.
     * @param description Description of task
     * @param isDone Whether event is done yet.
     * @param startTime Start time of event, in dd-mm-yyyy
     * @param endTime End time of event, in dd-mm-yyyy
     */
    public Event(String description, boolean isDone, String startTime, String endTime) {
        super(description, isDone);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startTime + " to " + endTime + ")";
    }

    /**
     * Returns a save text version of event.
     * Example: D;0;new task;18/12/2002
     */
    @Override
    public String toSave() {
        return "E;" + isDone + ";" + description + ";" + startTime + ";" + endTime;
    }
}
