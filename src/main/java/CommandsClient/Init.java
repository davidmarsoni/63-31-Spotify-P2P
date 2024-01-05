package CommandsClient;

import utils.*;

/**
 * Init Command
 * This command is used to configure the server address and port of the server
 */
// TODO see config of the listening port only if the client is not started
public class Init implements Command {
    // get the storage instance
    private StorageClient storage = StorageClient.getInstance();
    public Boolean startConfig = true;

    // create a scanner to get the user input
    /**
     * Execute the command
     * 
     * @param argument argument is not used
     */
    @Override
    public void execute(String argument) {
        // print the title
        if(startConfig == true){
            Utils.title("Initialization", Colors.BLUE_H);
            System.out.println(
                    "Choose your initialization method\n1 : init the client with a json file\n2 : enter information about the client manually");
            String choice = Utils.ask("Enter your desired init method", "1/2", "1", "2", true);

            switch (choice) {
                case "1":
                    fileInit();
                    break;
                case "2":
                    manualInit();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }else{
            manualInit();
        }
    }

    public void setStartConfig(Boolean startConfig) {
        this.startConfig = startConfig;
    }

    private void fileInit() {
        do {
            String path = Utils.ask("Set the complete path of your storage.json", storage.getSavePath(), ".*\\.json$",
                    storage.getSavePath(), true);

            path = path.replaceAll("\"", "");
            path = path.replaceAll("'", "");

            if (storage.setSavePath(path)) {
                System.out.println("File path saved.");
                storage.load();
                break;
            }
        } while (true);
    }

    private void manualInit() {

        // Ask for the server address and port and verify if it's valid
        String serverName = storage.getServerAddress();
        int serverPort = storage.getServerPort();

        // loop until the user enter a valid ip address
        serverName = Utils.ask("Server address", serverName, "^([0-9]{1,3}\\.){3}[0-9]{1,3}$", serverName,
                true);
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
        if (startConfig == true) {
            int listeningPort = storage.getPort();
            if (listeningPort == 0) {
                listeningPort = Utils.getAvailablePort();
            }
            do {
                String tmpClientPort = Utils.ask("Client Listening port", String.valueOf(listeningPort),
                        "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$",
                        String.valueOf(listeningPort), true);
                listeningPort = Integer.parseInt(tmpClientPort);
            } while (!storage.setPort(listeningPort));
        }

        System.out.println("");
        System.out.println("New server address and port saved");
        System.out.println("Server : " + Utils.colorize(storage.getServerAddress(), Colors.PURPLE) + ":"
                + Utils.colorize(String.valueOf(storage.getServerPort()), Colors.PURPLE));
        
        if(startConfig == true){
            System.out.println("Client : " + Utils.colorize(storage.getLocalAdressString(), Colors.PURPLE)
                    + ":"
                    + Utils.colorize(String.valueOf(storage.getPort()), Colors.PURPLE));
            System.out.println("");
        }
    }

    /**
     * Return the help of the command
     */
    @Override
    public String help() {
        return "Allow you to configure the server address and port";
    }

}
