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
        System.out.println("["+storage.getSrvSocket().getInetAddress().getHostAddress()+":"+storage.getSrvSocket().getPort()+"] Sending list of music to client:" );
        
        //for each entry send the data to the client
        out.println(Utils.ANSI_BLUE+"| "+Utils.ANSI_RESET+Utils.ANSI_WHITE+String.format("%-24s", "peers IPs and port")+Utils.ANSI_BLUE+"| "+Utils.ANSI_RESET+Utils.ANSI_WHITE+String.format("%-20s", "file name"));
        out.println(Utils.ANSI_BLUE+"| "+Utils.ANSI_RESET+Utils.ANSI_WHITE+String.format("%-24s", "------------------")+Utils.ANSI_BLUE+"| "+Utils.ANSI_RESET+Utils.ANSI_WHITE+String.format("%-20s", "---------"));
        for (Entry entry : storage.getEntries()) {
            out.println(Utils.ANSI_BLUE+"| "+Utils.ANSI_RESET+Utils.ANSI_WHITE+String.format("%-24s", entry.getClientAdress()+":"+entry.getClientPort())+Utils.ANSI_BLUE+"| "+Utils.ANSI_RESET+Utils.ANSI_WHITE+String.format("%-20s", entry.getName()));
        }
        //send the end of the list
        System.out.println("List of music sent");
        out.println("end");
    }

    @Override
    public String help() {
        return "Send the list of music to the client";
    }
    
}
