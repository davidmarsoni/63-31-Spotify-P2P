package Commands;
import java.util.*;

import utils.Storage;
import utils.Utils;

/**
 * Help command
 * This command display the list of all the commands available
 */
public class HelpClientCommand implements Command {
    private Storage storage = Storage.getInstance();
    Map<String,Command> commands;
    public HelpClientCommand() {
        commands = storage.getCommands();
    }

    @Override
    public void execute(String argument) {
        if(argument == null){
            String ANSI_RESET = "\u001B[0m";
            String ANSI_BLUE = "\u001B[34m";
            Utils.titleDesc("Help");
            Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", "Command") + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", "Description"));
            Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", "-------") + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", "-----------"));
            Map<String, Command> sortedCommands = new TreeMap<>(commands);
            for (Map.Entry<String, Command> entry : sortedCommands.entrySet()) {
                String key = entry.getKey();
                Command value = entry.getValue();
                String[] helpLines = value.help().split("\n");
                for (String line : helpLines) {
                    Utils.p(ANSI_BLUE + "| " + Utils.ANSI_WHITE + String.format("%-12s", key) + ANSI_BLUE +" | "+ ANSI_RESET  + String.format("%-68s", line));
                }          
            }
            
        }else{
            Command cmd = commands.get(argument);
            if (cmd != null) {
                Utils.p(cmd.help());
            } else {
                Utils.p("Command not found "+Utils.ANSI_BLUE+ argument+Utils.ANSI_RESET);
            }
        }
    }

    @Override
    public String help() {
        return "Display the list of all the commands available";
    }
}