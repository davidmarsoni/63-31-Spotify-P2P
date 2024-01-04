import java.io.*;
import java.net.*;
import java.util.*;

import CommandsServer.CommandServer;
import java.util.logging.Level;

import Classes.ThreadData;
import CommandsClient.Command;
import CommandsClient.InitServer;
import utils.*;

public class Server {

    static Scanner sc = new Scanner(System.in);
    static StorageServer storage = StorageServer.getInstance();
    static Map<String, CommandServer> commands = storage.getServerCommands();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Utils.renderStart(true);
        storage.setSrvSocket(ServerManagement.initializedServerSocket(storage.getPort()));

        Command init = new InitServer();
        init.execute(null);

        new Thread(() -> {
            while (true) {
                try {
                    //each 10 min save the data
                    Thread.sleep(600000);
                    storage.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // When the server is closed, save the data
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing server...");
            storage.save();
            System.out.println("Server closed");
        }));


        loop();

       
    }

    

    //correct client socket  for multi thread
    public static void loop() {
        commands.get("help").execute(null, null, null);
        while (true) {
            Utils.title("Waiting for a client connection...", Colors.BLUE_H);

            try {
                Socket srvSocket = storage.getSrvSocket().accept();

                new Thread(() -> {
                    ThreadData threadData = new ThreadData(srvSocket);
                    storage.addThreadData(threadData);
                    storage.setClientSocket(srvSocket);
                    LogsServer.log(Level.INFO, "Connection accepted from " + storage.getClientSocket().getInetAddress().getHostAddress() + ":" + storage.getClientSocket().getPort(),false);
                    // listen command from client
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(storage.getClientSocket().getInputStream()));
                        PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
                        while (true) {
                            if (Thread.currentThread().isInterrupted()) {
                                return;
                            }
                            // get the command
                            String[] words = in.readLine().split(" ");
                            String command = words[0];
                            String argument = words.length > 1 ? words[1] : null;

                            // execute the command
                            // System.out.println("Command: " + command + " Argument: " + argument);
                            CommandServer cmd = commands.get(command);
                            if (cmd != null) {
                                cmd.execute(argument, in, out);
                            } else {
                                LogsServer.log(Level.WARNING,"Command not found " + Utils.colorize(command, Colors.YELLOW));
                                // respond to the client
                                out.println("Command not found " + Utils.colorize(command, Colors.YELLOW));
                                out.println("end");
                            }
                            
                        }

                    } catch (Exception e) {
                        LogsServer.log(Level.SEVERE, Utils.colorize("Error handling client connection", Colors.RED_H),false);
                        //e.printStackTrace();
                        storage.updateClientEntry(false);
                    }

                }).start();

            } catch (Exception e) {
                System.err.println("No client connection durrint this wait time");
            }
        }
    }
}
