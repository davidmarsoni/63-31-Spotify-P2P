package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.logging.Level;

import Classes.Client;
import utils.Colors;
import utils.LogsServer;
import utils.StorageServer;
import utils.Utils;

public class EndCommand implements CommandServer {
    private StorageServer storage = StorageServer.getInstance();
    public EndCommand() {}

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        try {
            Client client = new Client(storage.getCurrentClientAddress(), storage.getCurrentClientPort());
            in.close();
            out.close();
            client.setAvailable(false);
            storage.updateClient(client);
            LogsServer.log(Level.INFO,Utils.colorize("Connection closed for client : " + client.getClientAdress().getHostAddress() + ":" + client.getClientPort(), Colors.RED_H),false);
            storage.removeCurrentThreadData();
            //stop the current thread
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Close the connection with the client";

    }
   
    
}
