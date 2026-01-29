import java.time.LocalDate;

public class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, String by) {
        super(description);
        this.by = stringToDate(by);
    }
    public Deadline(String description, boolean isDone, String by) {
        super(description, isDone);
        this.by = stringToDate(by);
    }
    private LocalDate stringToDate(String date) {
        return LocalDate.parse(date.trim());
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
