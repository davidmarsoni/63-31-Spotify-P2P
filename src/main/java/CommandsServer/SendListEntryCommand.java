package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Entry;
import Classes.MusicFile;
import Classes.PlayList;
import utils.Colors;
import utils.LogsServer;
import utils.StorageServer;
import utils.Utils;

public class SendListEntryCommand implements CommandServer{
    private StorageServer storage = StorageServer.getInstance();

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        if(argument != null){
            String name = null;
            String clientAddress = null;
            int clientPort = 0;
            String[] splitArgument = null;

            // Check if argument starts with a quote (single or double)
            if (argument.startsWith("\"") || argument.startsWith("'")) {
                // Find the closing quote
                int endQuoteIndex = argument.indexOf(argument.charAt(0), 1);
                if (endQuoteIndex != -1) {
                    // Extract the name and remove the quotes
                    name = argument.substring(1, endQuoteIndex);
                    // Split the rest of the argument
                    splitArgument = argument.substring(endQuoteIndex + 1).trim().split(" ");
                }
            } else {
                // If no quotes, split the argument as before
                splitArgument = argument.split(" ");
                name = splitArgument[0];
            }

            if (splitArgument != null && splitArgument.length == 2) {
                String[] addressPort = splitArgument[1].split(":");
                clientAddress = addressPort[0];
                clientPort = Integer.parseInt(addressPort[1]);
            }

            LogsServer.info("Sending details of the "+ (name.contains(".mp3") ? "music file " : "playlist ") + Utils.colorize(name, Colors.GREEN) + " to client");

            Entry entry = null;
            try {
                entry = clientAddress != null ? 
                    storage.findEntryByNameAndClientAdressAndPort(name, clientAddress, clientPort) : 
                    storage.findEntryByName(name);
            } catch (Exception e) {
                LogsServer.severe(clientAddress + ":" + clientPort + " is not a valid address:port");
            }

            if (entry == null) {
                out.println("The " + (name.contains(".mp3") ? "music file " : "playlist ") + Utils.colorize(name, Colors.GREEN) + " doesn't exist in the list of music on the server");
                out.println("end");
                return;
            }

            out.println(entry instanceof MusicFile ? ((MusicFile) entry).toString(true) : ((PlayList) entry).toString(true));
            out.println("end");
            return;
        }
        LogsServer.info("Sending list of music to client:");
        
        //for each entry send the data to the client
        out.println(Utils.colorize("| ",Colors.BLUE)+Utils.colorize(String.format("%-24s", "hosts IPs and port"),Colors.WHITE)+Utils.colorize(" | ",Colors.BLUE)+Utils.colorize(String.format("%-20s", "file name"),Colors.WHITE));
        out.println(Utils.colorize("| ",Colors.BLUE)+Utils.colorize(String.format("%-24s", "------------------"),Colors.WHITE)+Utils.colorize(" | ",Colors.BLUE)+Utils.colorize(String.format("%-20s", "---------"),Colors.WHITE));
        for (Entry entry : storage.getSharedEntries()) {
            if(entry.isAvailable()){
                out.println(Utils.colorize("| ",Colors.BLUE)+Utils.colorize(String.format("%-24s", entry.getClientAdress()+":"+entry.getClientPort()),Colors.WHITE)+Utils.colorize(" | ",Colors.BLUE)+Utils.colorize(String.format("%-20s", entry.getName()),Colors.WHITE));
            }
        }
        //send the end of the list
        LogsServer.info("List of all the available music sent to client");
        out.println("end");
    }

    @Override
    public String help() {
        return "Send the list of music to the client";
    }
    
}
