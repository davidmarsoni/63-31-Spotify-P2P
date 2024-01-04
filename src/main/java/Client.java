
import java.net.*;
import java.util.*;

import CommandsClient.*;
import utils.*;

public class Client {
    static Socket clientSocket;
    static InetAddress serverAddress;
    static StorageClient storage = StorageClient.getInstance();
    static Map<String, Command> commands = storage.getCommands();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        Utils.renderStart(false);
    
        Command cmd = commands.get("init"); 
        cmd.execute(null);

        Utils.title("Client is ready", Colors.GREEN_H);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing client...");
            storage.save();
            System.out.println("Client closed");
        }));

        loop();
    }

    /**
     * Loop to get the user input
     */
    public static void loop() {
        // create the thread for handling the commands
        Thread commandThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String input = "";
                while (true) {
                    System.out.print("> ");
                    // wait for the user input and trim it
                    if (sc.hasNextLine()) {
                        input = sc.nextLine().trim();
                        String[] words = input.split(" ", 2);
                        String command = words[0];
                        String argument = words.length > 1 ? words[1] : null;
                        // System.out.println("Command: " + command + " Argument: " + argument);
                        Command cmd = commands.get(command);
                        if (cmd != null) {
                            cmd.execute(argument);
                        } else {
                            System.out.println("Command not found " +Utils.colorize(command,Colors.YELLOW));
                        }
                    }
                }
            }
        });
        commandThread.start();

        // create the thread for handling the listening from the server
        Thread listeningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                storage.setSrvSocket(ServerManagement.initializedServerSocket("eth0", storage.getPort(), 10, 180000, false));
                while (true) {
                    ServerManagement.handleNewConnection(storage.getSrvSocket(), storage,false);
                }
            }
        });
        listeningThread.start();
    }
}
