package thonk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task
 */
public class Deadline extends Task {
    private static final String dateFormat = "dd/MM/yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    protected LocalDate by;

    /**
     * Creates an instance of Deadline.
     * @param description Description of task
     * @param by
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = stringToDate(by);
    }

    /**
     * Creates an instance of Deadline with description, isDone and by when.
     * @param description Description of task.
     * @param isDone Whether task is done or not.
     * @param by When the task is due.
     */
    public Deadline(String description, boolean isDone, String by) {
        super(description, isDone);
        this.by = stringToDate(by);
    }
    private LocalDate stringToDate(String date) {
        return LocalDate.parse(date.trim());
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }

    /**
     * Returns a save text version of deadline.
     * Example: D;0;new task;18/12/2002
     */
    @Override
    public String toSave() {
        return "D;" + isDone + ";" + description + ";" + by;
    }
}
