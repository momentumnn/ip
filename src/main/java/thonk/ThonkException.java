package thonk;

/**
 * The class ThonkException is thrown when there is an exception that happened within the program itself.
 */
public class ThonkException extends RuntimeException {
    public ThonkException(String message) {
        super(message);
    }
}
