package kevie.exceptions;

import kevie.UserInterface;

/**
 * Exception thrown when the save file scanning fails.
 */
public class FileCorruptionException extends KevieException{
    /**
     * Creates a Save file scanning exception.
     *
     * @param type Reason of why exception is thrown
     * @param ui User interface to print messages in
     */
    public FileCorruptionException(String type, UserInterface ui) {
        super("Save file is corrupted", type, ui);
    }
}
