package CommandsClient;

import utils.StorageClient;

/**
 * Exit command
 * This command exit the program and save the data of the client
 */
public class Exit implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();

    public Exit() {}
    @Override
    public void execute(String argument) {
        storage.save();
        //TODO : inform the server that the client is leaving the network and also rework the client and server save methods
        System.exit(0);
    }
    @Override
    public String help() {
        return "Exit the program";
    }
}
