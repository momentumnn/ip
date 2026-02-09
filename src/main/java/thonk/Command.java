package thonk;

/**
 * A class to store all commands
 */
public enum Command {
    TODO("todo", new String[] {"todo"}),
    DEADLINE("Thonk.Deadline", new String[] {"deadline", "/by"}),
    EVENT("Thonk.Event", new String[] {"event", "/from", "/to"}),
    MARK("Mark", new String[] {"mark"}),
    UNMARK("Unmark", new String[] {"unmark"}),
    LIST("List", new String[] {"list"}),
    DELETE("Delete", new String[] {"delete"}),
    BYE("Bye", new String[] {"bye"}),
    UNKNOWN("Wrong command", new String[] {}),
    FIND("Find task", new String[] {"find"});

    private final String[] keywords;
    Command(String description, String[] keywords) {
        this.keywords = keywords;
    }
    //public abstract void run(Thonk.core.TaskManager tm);
    //convert string to command

    /**
     * Converts string to command
     * @param input String input of task
     * @return Command. Null if input does not make sense
     */
    public static Command fromString(String input) {
        if (input == null || input.isBlank()) {
            return Command.UNKNOWN;
        }
        // Split input to get the first word (the actual command)
        String firstWord = input.split(" ")[0].toLowerCase();
        for (Command cmd : Command.values()) {
            for (String key : cmd.keywords) {
                if (key.equalsIgnoreCase(firstWord)) {
                    return cmd;
                }
            }
        }
        // Return null if no command matches
        return Command.UNKNOWN;
    }
}
