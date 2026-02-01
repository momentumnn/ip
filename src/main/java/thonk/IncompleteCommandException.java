package thonk;

public class IncompleteCommandException extends ThonkException {
    public IncompleteCommandException(String message) {
        super("Incomplete Thonk.Command, try again: " + message);
    }
}
