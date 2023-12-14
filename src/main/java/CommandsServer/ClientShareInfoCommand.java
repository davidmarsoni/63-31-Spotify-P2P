package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import utils.StorageServer;
import utils.Utils;

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

            System.out.println("Client address for listening : " +Utils.ANSI_BLUE+ ip + Utils.ANSI_RESET+":" +Utils.ANSI_BLUE+ port + Utils.ANSI_RESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return "send the listening port and ip of the client to the server";
    }
    
}
