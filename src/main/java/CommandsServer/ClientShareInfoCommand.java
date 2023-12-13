package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import utils.StorageServer;

public class ClientShareInfoCommand implements CommandServer {
    private StorageServer storage = StorageServer.getInstance();

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        // get the ip and the port of the client TODO : do for the clients
        try {
            String[] data = in.readLine().split(" ");
            String ip = data[0];
            int port = Integer.parseInt(data[1]);
            
            storage.setClientAddress(ip);
            storage.setClientPort(port);
            //send the response to the client
            out.println("received");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return "allow the server to get the ip and listening port of the client";
    }
    
}
