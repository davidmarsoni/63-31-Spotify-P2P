package Commands;

/**
 * Interface for the commands
 */
public interface Command {
    /**
     * Execute the command
     * @param argument the argument of the command
     */
    public void execute(String argument);

    /**
     * Detailed help of the command
     */
    public String help();
}
