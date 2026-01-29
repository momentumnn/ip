import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private static final String dateFormat = "dd/MM/yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
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
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }
}
