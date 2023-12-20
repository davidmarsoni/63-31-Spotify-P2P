package CommandsClient;

import utils.*;

/**
 * Init Command
 * This command is used to configure the server address and port of the server
 */
public class Init implements CommandClient {
    // get the storage instance
    private StorageClient storage = StorageClient.getInstance();

    // create a scanner to get the user input
    /**
     * Execute the command
     * 
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
        // I know that there is better way to do it with if else but i would like to use
        // the Utils.ask method and note devlop a new one just for this
        String tmpServerPort = Utils.ask("Server port", String.valueOf(serverPort),
                "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$",
                String.valueOf(serverPort), true);

        // set the server address and port in the storage
        serverPort = Integer.parseInt(tmpServerPort);
        storage.setServerAddress(serverName);
        storage.setServerPort(serverPort);


        // ask for the listening port of the client
        int clientPort = storage.getClientPort();
        String tmpClientPort = Utils.ask("Client Listening port", String.valueOf(clientPort),
                "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$",
                String.valueOf(clientPort), true);
        clientPort = Integer.parseInt(tmpClientPort);
        storage.setClientPort(clientPort);
       
        System.out.println("");
        System.out.println("New server address and port saved");
        System.out.println("Server : " + Utils.colorize(storage.getServerAddress(),Colors.DARK_PURPLE) + ":"
                + Utils.colorize(String.valueOf(storage.getServerPort()), Colors.DARK_PURPLE));
        System.out.println("Client : " + Utils.colorize(storage.getClientAddress(),Colors.DARK_PURPLE) + ":"
                + Utils.colorize(String.valueOf(storage.getClientPort()), Colors.DARK_PURPLE));
        System.out.println("");
    }

    /**
     * Return the help of the command
     */
    @Override
    public String help() {
        return "Allow you to configure the server address and port";
    }

}
