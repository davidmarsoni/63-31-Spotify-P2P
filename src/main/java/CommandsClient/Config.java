package CommandsClient;
import utils.*;

/**
 * Config Command
 * This command is used to print the current configuration of the client (Server,Client,Shared entries and other informations needed)
 */
public class Config implements Command {
    //get the instance of the storage to get the data
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        Utils.titleDesc("Current config", "Display the configuration of the client");
        System.out.println("Me     : " +
                Utils.colorize(storage.getLocalAdressString(), Colors.PURPLE) +
                ":" +
                Utils.colorize(String.valueOf(storage.getPort()), Colors.PURPLE));
        System.out.println("Server : " +
                Utils.colorize(storage.getServerAddress(), Colors.PURPLE) +
                ":" +
                Utils.colorize(String.valueOf(storage.getServerPort()), Colors.PURPLE));
        //get current saved path
        System.out.println("Save path : " +
                Utils.colorize(storage.getSavePath(), Colors.BLUE));
        // list all the entries shared by the client
        if (storage.getSharedEntries().isEmpty()) {
            System.out.println("No shared entries");
        } else {
            System.out.println("Shared entries : ");
            storage.listSharedEntries();
        }
    }

    @Override
    public String help() {
        return "Print the current configuration of the client";
    }

}
