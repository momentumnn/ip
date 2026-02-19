package thonk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a {@code Deadline} task with a due date.
 * <p>
 * The due date is stored as a {@link LocalDate}. Input date strings are validated before parsing.
 * </p>
 */
public class Deadline extends Task {
    private static final String dateFormat = "dd/MM/yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    protected LocalDate by;
    final String dateRegex = "(19|20)\\d{2}([/\\-])(0[1-9]|1[0-2]|[1-9])([/\\-])(0[1-9]|[12][0-9]|3[01])";

    /**
     * Creates a new {@code Deadline} task with the given description and due date.
     *
     * @param description Description of the task.
     * @param by Due date string for the task (expected in {@code YYYY-MM-DD} format).
     * @throws ThonkException If {@code by} does not match the expected format.
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
        if (date.matches(dateRegex)) {
            return LocalDate.parse(date.trim());
        }
        throw new ThonkException("Please make sure it is in YYYY-MM-DD format");
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
    public String toSave(String splitChar) {
        return "D" + splitChar + isDone + splitChar + description + splitChar + by;
    }
}
