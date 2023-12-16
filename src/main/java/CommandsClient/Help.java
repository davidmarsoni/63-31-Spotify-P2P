package CommandsClient;
import java.util.*;

import utils.StorageClient;
import utils.Utils;

/**
 * Help command
 * This command display the list of all the commands available or a specific command
 */
public class Help implements CommandClient {
    private StorageClient storage = StorageClient.getInstance(); // Storage of the client
    Map<String,CommandClient> commands; // List of all the commands available

    /**
     * Constructor of the command
     */
    public Help() {
        commands = storage.getCommands(); // Get the list of all the commands available from the storage
    }

    /**
     * Execute the command
     * If the argument is null, display the list of all the commands available
     * @param argument command to show the help
     */
    @Override
    public void execute(String argument) {
        // If the argument is null, display the list of all the commands available otherwise display the help of the command
        if(argument == null){
            // get ansi colors for readability
            String ANSI_RESET = Utils.ANSI_RESET;
            String ANSI_BLUE = Utils.ANSI_BLUE;
            // print the title
            Utils.title("Help");

            // print the table
            Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", "Command") + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", "Description"));
            Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", "-------") + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", "-----------"));
            Map<String, CommandClient> sortedCommands = new TreeMap<>(commands);

            for (Map.Entry<String, CommandClient> entry : sortedCommands.entrySet()) {
                String key = entry.getKey();
                CommandClient value = entry.getValue();
                String[] helpLines = value.help().split("\n");
                Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", key) + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", helpLines[0]));
                for (int i = 1; i < helpLines.length; i++) {
                    Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE +String.format("%-12s", "he") + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", helpLines[i]));
                    key = "";
                }      
            }
            Utils.p("");
            
        }else{
            // get the command from the argument
            CommandClient cmd = commands.get(argument);
            // if the command exist, display the help of the command otherwise display an error message
            if (cmd != null) {
                Utils.title("Help "+argument);
                Utils.p(cmd.help());
                Utils.p("");
            } else {
                Utils.p("Command not found "+Utils.ANSI_YELLOW+ argument+Utils.ANSI_RESET);
            }
        }
        
    }

    /**
     * Help of the command
     * @return the help of the command
     */
    @Override
    public String help() {
        return "Display the list of all the commands available";
    }
}