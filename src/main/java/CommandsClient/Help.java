package CommandsClient;

import java.util.*;

import utils.*;

/**
 * Help command
 * This command display the list of all the commands available or a specific
 * command
 */
public class Help implements CommandClient {
    private StorageClient storage = StorageClient.getInstance(); // Storage of the client
    Map<String, CommandClient> commands; // List of all the commands available

    /**
     * Constructor of the command
     */
    public Help() {
        commands = storage.getCommands(); // Get the list of all the commands available from the storage
    }

    /**
     * Execute the command
     * If the argument is null, display the list of all the commands available
     * 
     * @param argument command to show the help
     */
    @Override
    public void execute(String argument) {
        // If the argument is null, display the list of all the commands available
        // otherwise display the help of the command
        if (argument == null) {
            // get ansi colors for readability
            String BLUE = Utils.ANSI_BLUE;
            String WHITE = Utils.ANSI_WHITE;

            // print the title
            Utils.title("Help");

            // print the table
            Utils.p(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", "Command"), WHITE)
                    + Utils.colorize(" | ", BLUE)
                    + String.format("%-68s", "Description"));
            Utils.p(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", "-------"), WHITE)
                    + Utils.colorize(" | ", BLUE)
                    + String.format("%-68s", "-----------"));
            Map<String, CommandClient> sortedCommands = new TreeMap<>(commands);

            for (Map.Entry<String, CommandClient> entry : sortedCommands.entrySet()) {
                String key = entry.getKey();
                CommandClient value = entry.getValue();
                String[] helpLines = value.help().split("\n");
                Utils.p(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", key), WHITE)
                        + Utils.colorize(" | ", BLUE) + String.format("%-68s", helpLines[0]));
                for (int i = 1; i < helpLines.length; i++) {
                    Utils.p(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", ""), WHITE)
                            + Utils.colorize(" | ", BLUE) + String.format("%-68s", helpLines[i]));
                    key = "";
                }
            }
            Utils.p("");

        } else {
            // get the command from the argument
            CommandClient cmd = commands.get(argument);
            // if the command exist, display the help of the command otherwise display an
            // error message
            if (cmd != null) {
                Utils.title("Help " + argument);
                Utils.p(cmd.help());
                Utils.p("");
            } else {
                Utils.p("Command not found " + Utils.colorize(argument, Utils.ANSI_YELLOW));
            }
        }

    }

    /**
     * Help of the command
     * 
     * @return the help of the command
     */
    @Override
    public String help() {
        return "Display the list of all the commands available";
    }
}