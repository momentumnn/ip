public class IncompleteCommand extends RuntimeException {
  public IncompleteCommand(String message) {
    super("Incomplete Command, try again: " + message);
  }
}
