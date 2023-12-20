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
        System.out.println("Exiting the program ...");
        storage.save(); 
        System.out.println("The data of the client has been saved");

        //run the disconnect command to inform the server that the client is leaving the network
        Disconnect disconnect = new Disconnect();
        disconnect.execute(argument);
        System.exit(0);
    }
    @Override
    public String help() {
        return "Exit the program";
    }
}
