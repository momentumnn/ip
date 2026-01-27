public class IncompleteCommandException extends RuntimeException {
  public IncompleteCommandException(String message) {
    super("Incomplete Command, try again: " + message);
  }
}
