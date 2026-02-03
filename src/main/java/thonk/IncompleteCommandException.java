package thonk;

/**
 * The class IncompleteCommandException is thrown when the user inputs a wrong command.
 * A wrong command is when it does not match the commands provided.
 */
public class IncompleteCommandException extends ThonkException {
    public IncompleteCommandException(String message) {
        super("Incomplete command, try again: " + message);
    }
}
