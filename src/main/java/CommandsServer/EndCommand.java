package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import utils.StorageServer;
import utils.Utils;

public class EndCommand implements CommandServer {
    private StorageServer storage = StorageServer.getInstance();
    public EndCommand() {}

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        try {
            in.close();
            out.close();
            storage.updateClientEntry(false);
            storage.updateClient(null);
            Utils.title("Connection closed for client : " + storage.getClientAddress() + ":" + storage.getClientPort(), Utils.ANSI_RED_H);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return "Close the connection with the client";

    }
   
    
}
