package CommandsClient;

/**
 * Interface for the commands
 */
public interface CommandClient {
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
