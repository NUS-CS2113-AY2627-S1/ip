package foodielover;

/**
 * Signals an application-specific error within the Foodielover application.
 */
public class FoodieloverException extends Exception {
    /**
     * Constructs a new FoodieloverException with the specified error message.
     *
     * @param message Detailed description of the error.
     */
    public FoodieloverException(String message) {
        super(message);
    }
}
