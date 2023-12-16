package CommandsServer;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;

import utils.StorageServer;
import utils.Utils;

/**
 * Help command
 * This command display the list of all the commands available or a specific command
 * This command is only use for debug purpose so it is not possible to write this command on the server console
 */
public class HelpCommand implements CommandServer {
    private StorageServer storage = StorageServer.getInstance(); // Storage of the client
    Map<String,CommandServer> commands; // List of all the commands available

    /**
     * Constructor of the command
     */
    public HelpCommand() {
        commands = storage.getCommands(); // Get the list of all the commands available from the storage
    }



    /**
     * Help of the command
     * @return the help of the command
     */
    @Override
    public String help() {
        return "Display the list of all the commands available";
    }

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
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
            Map<String, CommandServer> sortedCommands = new TreeMap<>(commands);
            for (Map.Entry<String, CommandServer> entry : sortedCommands.entrySet()) {
                String key = entry.getKey();
                CommandServer value = entry.getValue();
                String[] helpLines = value.help().split("\n");
                for (String line : helpLines) {
                    Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", key) + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", line));
                }          
            }
            Utils.p("");
            
        }else{
            // get the command from the argument
            CommandServer cmd = commands.get(argument);
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
}