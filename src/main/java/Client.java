//TODO https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

import java.net.*;
import java.util.*;

import CommandsClient.*;
import utils.*;

public class Client {
    static Socket clientSocket;
    static InetAddress serverAddress;
    static StorageClient storage = StorageClient.getInstance();
    static Map<String, CommandClient> commands = storage.getCommands();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Utils.renderStart(false);
        // load the data
        loop();
    }

    /**
     * Loop to get the user input
     */
    public static void loop() {
        // create 2 threads one for handling the commands and the other for handling
        // listening from the server

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

                        CommandClient cmd = commands.get(command);
                        if (cmd != null) {
                            cmd.execute(argument);
                        } else {
                            System.out.println("Command not found " +Utils.colorize(command,Utils.ANSI_YELLOW));
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
                while (true) {
                    // TODO : handle the listening from the server so first connection and then a new tread foreach client listening with command and argument
                    // it will be like the server command system
                }
            }
        });
        listeningThread.start();
    }
}
