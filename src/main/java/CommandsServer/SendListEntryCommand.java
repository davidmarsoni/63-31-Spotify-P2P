package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Entry;
import utils.StorageServer;
import utils.Utils;

public class SendListEntryCommand implements CommandServer{
    private StorageServer storage = StorageServer.getInstance();

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        Utils.p("Sending list of music to client:" + storage.getSrvSocket().getInetAddress()+":"+storage.getSrvSocket().getPort());
        //for each entry send the data to the client
        for (Entry entry : storage.getEntries()) {
            out.println(entry.toString());
        }
        //send the end of the list
        out.println("end");
        Utils.p("List of music sent");
    }

    @Override
    public String help() {
        return "Send the list of music to the client";
    }
    
}
