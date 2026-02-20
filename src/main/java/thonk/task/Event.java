package thonk.task;

import java.time.LocalDate;

/**
 * Represents a Event task, extended from Task.
 */
public class Event extends Task {
    protected LocalDate startTime;
    protected LocalDate endTime;

    /**
     * Creates an instance of Event.
     * @param description Description of task
     * @param startTime Start time of event, in dd-mm-yyyy
     * @param endTime End time of event, in dd-mm-yyyy
     */
    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = stringToDate(startTime);
        this.endTime = stringToDate(endTime);
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
        this.startTime = stringToDate(startTime);
        this.endTime = stringToDate(endTime);
    }
    private LocalDate stringToDate(String date) {
        return LocalDate.parse(date.trim());
    }
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startTime + " to " + endTime + ")";
    }

    /**
     * Returns a save text version of event.
     * @return Example: D;0;new task;18/12/2002
     */
    @Override
    public String toSave(String splitChar) {
        return "E" + splitChar + isDone + splitChar + description + splitChar + startTime + splitChar + endTime;
    }
}
