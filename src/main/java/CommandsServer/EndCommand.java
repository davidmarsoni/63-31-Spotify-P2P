package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Client;
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
            Utils.title("Connection closed for client : " + client.getClientAdress().getHostAddress() + ":" + client.getClientPort(), Utils.ANSI_RED_H);
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
