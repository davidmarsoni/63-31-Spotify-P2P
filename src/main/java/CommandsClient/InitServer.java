package CommandsClient;

import utils.*;

/**
 * Init Command
 * This command is used to configure the server address and port of the server
 */
public class InitServer implements Command {
    // get the storage instance
    private StorageServer storage = StorageServer.getInstance();

    // create a scanner to get the user input
    /**
     * Execute the command
     * 
     * @param argument argument is not used
     */
    @Override
    public void execute(String argument) {
        // print the title
        Utils.title("Initialization", Colors.BLUE_H);
        System.out.println("Choose your initialization method\n1 : init the server with a json file\n2 : enter information about the server manually");
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
    }

    private void fileInit() {
        do {
            String path = Utils.ask("Set the complete path of your storage.json", storage.getSavePath(), ".*\\.json$", storage.getSavePath(), true);
            if (storage.setSavePath(path)) {
                System.out.println("File path saved.");
                storage.load();
                break;
            }
        } while (true);
    }

    private void manualInit() {

        // ask for the listening port of the server
        int port = storage.getPort();
        do {
            String tmpServerPort = Utils.ask("Server Listening port", String.valueOf(port),
                    "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$",
                    String.valueOf(port), true);
            port = Integer.parseInt(tmpServerPort);
        } while (!storage.setPort(port));

        System.out.println("");
        System.out.println("New server address and port saved");
        System.out.println("Server : " + Utils.colorize(storage.getLocalAdress().getHostAddress(), Colors.PURPLE) + ":"
                + Utils.colorize(String.valueOf(storage.getPort()), Colors.PURPLE));
        System.out.println("");
    }

    /**
     * Return the help of the command
     */
    @Override
    public String help() {
        return "Allow you to configure the server Port";
    }

}
