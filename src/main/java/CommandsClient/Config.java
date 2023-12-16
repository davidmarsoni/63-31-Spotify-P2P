package CommandsClient;
import utils.*;

/**
 * Config Command
 * This command is used to print the current configuration of the client (Server,Client,Shared entries and other informations needed)
 */
public class Config implements CommandClient {
    //get the instance of the storage to get the data
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        Utils.titleDesc("Current config", "Display the configuration of the client");
        Utils.p("Client : " +
                Utils.colorize(storage.getClientAddress(), Utils.ANSI_BLUE) +
                ":" +
                Utils.colorize(String.valueOf(storage.getClientPort()), Utils.ANSI_BLUE));
        Utils.p("Server : " +
                Utils.colorize(storage.getServerAddress(), Utils.ANSI_BLUE) +
                ":" +
                Utils.colorize(String.valueOf(storage.getServerPort()), Utils.ANSI_BLUE));

        // list all the entries shared by the client
        if (storage.getSharedEntries().isEmpty()) {
            Utils.p("No shared entries");
        } else {
            Utils.p("Shared entries : ");
            storage.listSharedEntries();
        }
    }

    @Override
    public String help() {
        return "Print the current configuration of the client";
    }

}
