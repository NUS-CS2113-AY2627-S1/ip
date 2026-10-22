package wallis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    protected String by;
    protected LocalDate byDate;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.byDate = parseDate(by);
    }

    private LocalDate parseDate(String dateStr) {
        try {
            // Try parsing standard yyyy-MM-dd format from user input
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                // Try parsing the formatted date when loading from the save file
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException ex) {
                // Fallback to null if it's just a random string like "Sunday"
                return null;
            }
        }
    }

    @Override
    public String toString() {
        String dateString = (byDate != null) 
            ? byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) 
            : by;
        return "[D]" + super.toString() + " (by: " + dateString + ")";
    }
}