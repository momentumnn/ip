public class IncompleteCommandException extends ThonkException {
    public IncompleteCommandException(String message) {
        super("Incomplete Command, try again: " + message);
    }
}
