package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Entry;
import utils.Colors;
import utils.StorageServer;
import utils.Utils;

public class SendListEntryCommand implements CommandServer{
    private StorageServer storage = StorageServer.getInstance();

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        if(argument != null){
            //TODO : send the details of the file by it file name
        }

        storage.printLog("Sending list of music to client:");
        
        //for each entry send the data to the client
        out.println(Utils.colorize("| ",Colors.BLUE)+Utils.colorize(String.format("%-24s", "hosts IPs and port"),Colors.WHITE)+Utils.colorize(" | ",Colors.BLUE)+Utils.colorize(String.format("%-20s", "file name"),Colors.WHITE));
        out.println(Utils.colorize("| ",Colors.BLUE)+Utils.colorize(String.format("%-24s", "------------------"),Colors.WHITE)+Utils.colorize(" | ",Colors.BLUE)+Utils.colorize(String.format("%-20s", "---------"),Colors.WHITE));
        for (Entry entry : storage.getSharedEntries()) {
            if(entry.isAvailable()){
                out.println(Utils.colorize("| ",Colors.BLUE)+Utils.colorize(String.format("%-24s", entry.getClientAdress()+":"+entry.getClientPort()),Colors.WHITE)+Utils.colorize(" | ",Colors.BLUE)+Utils.colorize(String.format("%-20s", entry.getName()),Colors.WHITE));
            }
        }
        //send the end of the list
        storage.printLog("List of music sent");
        out.println("end");
    }

    @Override
    public String help() {
        return "Send the list of music to the client";
    }
    
}
