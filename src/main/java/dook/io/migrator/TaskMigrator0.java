package dook.io.migrator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dook.io.SerialisedData;
import dook.parser.DateTimeParser;
import dook.task.DeadlineTask;
import dook.task.EventTask;
import dook.task.ToDoTask;

/**
 * Migrates task serialised data from version 0 to version 1.
 */
public class TaskMigrator0 {
    private static final Class<?> DEFAULT_TASK_CLASS = ToDoTask.class;

    /**
     * Returns migrated serialised data.
     *
     * @param  content The serialised data in version 0 format.
     * @return         The migrated serialised data.
     */
    public static List<SerialisedData> migrate(List<SerialisedData> content) {
        List<SerialisedData> newContent = new ArrayList<>();
        for (SerialisedData data : content) {
            Class<?> classType = data.getClassType();
            List<?> details = data.getDetails();
            SerialisedData newData;

            if (classType == DeadlineTask.class) {
                newData = parseOldDeadlineTask(details);

            } else if (classType == EventTask.class) {
                newData = parseOldEventTask(details);

            } else {
                newData = parseOldGenericTask(details);
            }

            newContent.add(newData);
        }
        return newContent;
    }

    /**
     * Returns a migrated generic task from old serialised details.
     *
     * @param  details The old serialised details.
     * @return         The migrated serialised data.
     */
    private static SerialisedData parseOldGenericTask(List<?> details) {
        boolean isDone = Boolean.parseBoolean(String.valueOf(details.get(0)));
        String description = String.valueOf(details.get(1));

        return new SerialisedData(
            DEFAULT_TASK_CLASS,
            isDone,
            description
        );
    }

    /**
     * Returns a migrated deadline task from old serialised details.
     *
     * @param  details The old serialised details.
     * @return         The migrated serialised data.
     */
    private static SerialisedData parseOldDeadlineTask(List<?> details) {
        boolean isDone = Boolean.parseBoolean(String.valueOf(details.get(0)));
        String description = String.valueOf(details.get(1));
        String rawDeadline = String.valueOf(details.get(2));
        Optional<LocalDateTime> maybeDeadline =
            DateTimeParser.parseEndTime(rawDeadline);

        if (maybeDeadline.isPresent()) {
            return new SerialisedData(
                DeadlineTask.class,
                isDone,
                description,
                maybeDeadline.get()
            );
        }

        return new SerialisedData(
            DEFAULT_TASK_CLASS,
            isDone,
            description + " by " + rawDeadline
        );
    }

    /**
     * Returns a migrated event task from old serialised details.
     *
     * @param  details The old serialised details.
     * @return         The migrated serialised data.
     */
    private static SerialisedData parseOldEventTask(List<?> details) {
        boolean isDone = Boolean.parseBoolean(String.valueOf(details.get(0)));
        String description = String.valueOf(details.get(1));
        String rawStart = String.valueOf(details.get(2));
        String rawEnd = String.valueOf(details.get(3));

        SerialisedData defaultData =
            new SerialisedData(
                DEFAULT_TASK_CLASS,
                isDone,
                description + " from " + rawStart
                            + " to " + rawEnd
            );

        Optional<LocalDateTime> maybeStart = DateTimeParser.parseStartTime(rawStart);
        if (maybeStart.isEmpty()) {
            return defaultData;
        }

        LocalDateTime startDatetime = maybeStart.get();
        Optional<LocalDateTime> maybeEnd = DateTimeParser.parseEndTime(
            rawEnd,
            startDatetime.toLocalDate()
        );
        if (maybeEnd.isEmpty()) {
            return defaultData;
        }

        LocalDateTime endDatetime = maybeEnd.get();
        if (startDatetime.isAfter(endDatetime)) {
            return defaultData;
        }

        return new SerialisedData(
            EventTask.class,
            isDone,
            description,
            maybeStart.get(),
            maybeEnd.get()
        );
    }
}
