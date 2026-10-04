package dook.io.migrator;

import java.util.List;

import dook.io.SerialisedData;

/**
 * Represents a migrator that updates the outdated serialised data.
 * Used in {@link dook.io.FileIO#readFile()}.
 */
public interface Migrator {

    /**
     * Modifies the serialised data.
     *
     * @param  content The serialised data in outdated format.
     * @return         The updated serialised data.
     */
    List<SerialisedData> migrate(List<SerialisedData> content);
}
