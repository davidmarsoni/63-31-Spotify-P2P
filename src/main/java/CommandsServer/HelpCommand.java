package CommandsServer;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;

import utils.Colors;
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
        commands = storage.getServerCommands(); // Get the list of all the commands available from the storage
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
        if(argument == null){
         // get ansi colors for readability
            String BLUE =Colors.BLUE;
            String WHITE = Colors.WHITE;

            // print the title
            Utils.title("Help");

            // print the table
            System.out.println(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", "Command"), WHITE)
                    + Utils.colorize(" | ", BLUE)
                    + String.format("%-68s", "Description"));
            System.out.println(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", "-------"), WHITE)
                    + Utils.colorize(" | ", BLUE)
                    + String.format("%-68s", "-----------"));
            Map<String, CommandServer> sortedCommands = new TreeMap<>(commands);

            for (Map.Entry<String, CommandServer> entry : sortedCommands.entrySet()) {
                String key = entry.getKey();
                CommandServer value = entry.getValue();
                String[] helpLines = value.help().split("\n");
                System.out.println(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", key), WHITE)
                        + Utils.colorize(" | ", BLUE) + String.format("%-68s", helpLines[0]));
                for (int i = 1; i < helpLines.length; i++) {
                    System.out.println(Utils.colorize("| ", BLUE) + Utils.colorize(String.format("%-12s", ""), WHITE)
                            + Utils.colorize(" | ", BLUE) + String.format("%-68s", helpLines[i]));
                    key = "";
                }
            }
            System.out.println("");

        } else {
            // get the command from the argument
            CommandServer cmd = commands.get(argument);
            // if the command exist, display the help of the command otherwise display an
            // error message
            if (cmd != null) {
                Utils.title("Help " + argument);
                System.out.println(cmd.help());
                System.out.println("");
            } else {
                System.out.println("Command not found " + Utils.colorize(argument,Colors.YELLOW));
            }
        }
    }
}