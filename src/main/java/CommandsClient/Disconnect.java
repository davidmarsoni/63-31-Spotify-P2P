package CommandsClient;
import java.io.PrintWriter;

import utils.StorageClient;
import utils.Utils;

public class Disconnect implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument) {
        if(storage.getClientSocket(false)==null){
            return;
        }
        //TODO : inform the server that the client is leaving the network 

          System.out.println("Trying to disconnect from the connection: " + Utils.colorize(storage.getServerAddress(),Utils.ANSI_DARK_PURPLE) + ":" + Utils.colorize(String.valueOf(storage.getServerPort()),Utils.ANSI_DARK_PURPLE));
        try {
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
            //send the command to the server
            out.println("end");
            //reset the client socket
            storage.setClientSocket(null);

            System.out.println("Disconnected from the connection: " + Utils.colorize(storage.getServerAddress(),Utils.ANSI_DARK_PURPLE) + ":" + Utils.colorize(String.valueOf(storage.getServerPort()),Utils.ANSI_DARK_PURPLE));
            
        }catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        } 

    }

    @Override
    public String help() {
        return "Disconnect the client from the server";
    }
    
}
