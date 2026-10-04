package dook.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents serialised object data with a class and a list of details.
 */
@SuppressWarnings("unchecked")
public class SerialisedData {
    private static final String DELIMITER = "|";
    private Class<?> classType;
    private List<?> details;

    /**
     * Constructs a new SerialisedData with the given class and details.
     *
     * @param classType The class of the serialised object.
     * @param args  The details of the serialised object.
     */
    public SerialisedData(Class<?> classType, Object... args) {
        this.classType = classType;
        details = new ArrayList<>(Arrays.asList(args));
    }

    /**
     * Constructs a new SerialisedData from a serialised string array.
     *
     * @param  args The serialised string array.
     * @throws ClassNotFoundException If the class cannot be found.
     */
    public SerialisedData(String... args) throws ClassNotFoundException {
        this(
            Class.forName(args[0]),
            (Object[]) Arrays.copyOfRange(args, 1, args.length)
        );
    }

    /**
     * Adds additional details to this serialised data.
     *
     * @param args The details to add.
     */
    public void add(Object... args) {
        ((List) details).addAll(Arrays.asList(args));
    }

    /**
     * Returns the class of the serialised object.
     *
     * @return The class of the serialised object.
     */
    public Class<?> getClassType() {
        return classType;
    }

    /**
     * Returns an immutable copy of the details.
     *
     * @return An immutable copy of the details.
     */
    public List<?> getDetails() {
        return List.copyOf(details);
    }

    /**
     * Returns the serialised string representation.
     *
     * @return The serialised string representation.
     */
    @Override
    public String toString() {
        String detailsString = details.stream()
                                      .map(String::valueOf)
                                      .collect(Collectors.joining(DELIMITER));
        return classType.getName() + DELIMITER + detailsString;
    }
}
