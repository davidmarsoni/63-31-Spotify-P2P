package CommandsClient;

import utils.StorageClient;

/**
 * Exit command
 * This command exit the program
 */
public class Exit implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();

    public Exit() {}
    @Override
    public void execute(String argument) {
        storage.save();
        System.exit(0);
    }
    @Override
    public String help() {
        return "Exit the program";
    }
}
