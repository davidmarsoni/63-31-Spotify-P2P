package Commands;
import utils.StorageClient;
import utils.Utils;

public class InitCommand implements Command {
    // get the storage instance
    private StorageClient storage = StorageClient.getInstance();
    // create a scanner to get the user input
    /**
     * Execute the command
     * @param argument argument is not used
     */
    @Override
    public void execute(String argument) {
        // print the title
        Utils.titleDesc("Init", "Configure the server address and port");

        // Ask for the server address and port and verify if it's valid
        String serverName = storage.getServerAddress();
        int serverPort = storage.getServerPort();

        // loop until the user enter a valid ip address
        serverName = Utils.ask("Server address", serverName, "^([0-9]{1,3}\\.){3}[0-9]{1,3}$", serverName, true);
        serverPort = Integer.parseInt(Utils.ask("Server port", String.valueOf(serverPort), "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$", String.valueOf(serverPort), true));
        storage.setServerAddress(serverName);
        storage.setServerPort(serverPort);

        Utils.p("");
        Utils.p ("New server address and port saved");
        Utils.p ("Server : "+Utils.ANSI_BLUE+serverName+Utils.ANSI_RESET+":"+Utils.ANSI_BLUE+serverPort+Utils.ANSI_RESET);
        Utils.p("");
    }

    /**
     * Return the help of the command
     */
    @Override
    public String help() {
        return "Allow you to configure the server address and port";
    }

}
