import java.io.*;
import java.net.*;
import java.util.*;

import CommandsServer.CommandServer;
import utils.*;

public class Server {

    static Scanner sc = new Scanner(System.in);
    static StorageServer storage = StorageServer.getInstance();
    static Map<String, CommandServer> commands = storage.getServerCommands();

    public static void main(String[] args) {
        Utils.renderStart(true);
        storage.setSrvSocket(ServerManagement.initializedServerSocket(storage.getPort()));
        loop();
    }

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
                    System.out.println(
                        "Connection accepted from " + storage.getClientSocket().getInetAddress().getHostAddress() + ":" + storage.getClientSocket().getPort());
                    // listen command from client
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(storage.getClientSocket().getInputStream()));
                        PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
                        while (true) {
                            System.out.println("Waiting for a command from client");
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
                                storage.printLog(
                                        "Command not found " + Utils.colorize(command, Colors.YELLOW));
                                // respond to the client
                                out.println("Command not found " + Utils.colorize(command, Colors.YELLOW));
                                out.println("end");
                            }
                            
                        }

                    } catch (Exception e) {
                        Utils.title("Client :"+storage.getCurrentSocket().getInetAddress().getHostAddress()+":"+ storage.getCurrentSocket().getPort() + " disconnected",Colors.RED_H);
                        e.printStackTrace();
                        storage.updateClientEntry(false);
                    }

                }).start();

            } catch (Exception e) {
                System.err.println("No client connection durrint this wait time");
            }
        }
    }
}
