package CommandsClient;

import java.io.IOException;

import utils.StorageClient;

public class Disconnect implements CommandClient{
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        try {
            //send to the server that the client is disconnecting
            
            storage.getClientSocket().close();
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Disconnect from the server";
    }
    
}
