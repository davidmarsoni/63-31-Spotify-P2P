package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Classes.ThreadData;
import CommandsServer.CommandServer;

public class ServerManagement {

    public static ServerSocket initializedServerSocket(int port) {
        return initializedServerSocket( port, 10, 180000, true);
    }

    public static ServerSocket initializedServerSocket(int port, int backlog, int timeout, boolean verbose) {
        try {
            InetAddress localAddress = InetAddress.getByName("127.0.0.1");

            ServerSocket srvSocket = new ServerSocket(port, backlog, localAddress);

            // set 3min timeout
            srvSocket.setSoTimeout(timeout);

            // print some server information
            if (verbose) {
                Utils.title("Server Started", Colors.GREEN_H);
                System.out.println("Default Timeout   : " + srvSocket.getSoTimeout());
                System.out.println("Used IpAddress    : " + srvSocket.getInetAddress().getHostAddress());
                System.out.println("Listening to Port : " + srvSocket.getLocalPort());
            }

            return srvSocket;
        } catch (Exception e) {
            if (verbose) {
                System.err.println("Error initializing server");
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void handleNewConnection(ServerSocket srvSocket, Storage storage, boolean verbose) {
        if (verbose) {
            Utils.title("Waiting for a client connection...", Colors.BLUE_H);
        }
        
        try {
            Socket currentServerSocket = storage.getSrvSocket().accept();
            new Thread(() -> {
                ThreadData threadData = new ThreadData(currentServerSocket);
                storage.addThreadData(threadData);
                storage.getCurrentThreadsData().setSocket(currentServerSocket);
                if (verbose) {
                    System.out.println(
                            "Connection accepted from " + storage.getCurrentSocket().getInetAddress().getHostAddress()
                                    + ":" + storage.getCurrentSocket().getPort());
                }
                // listen command from client
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(storage.getCurrentSocket().getInputStream()));
                    PrintWriter out = new PrintWriter(storage.getCurrentSocket().getOutputStream(), true);
                    while (true) {
                        if (verbose) {
                            System.out.println("Waiting for a command from client");
                        }
                        // get the command
                        String[] words = in.readLine().split(" ");
                        String command = words[0];
                        String argument = words.length > 1 ? words[1] : null;

                        // execute the command
                        // System.out.println("Command: " + command + " Argument: " + argument);
                        CommandServer cmd = storage.getServerCommands().get(command);
                        if (cmd != null) {
                            cmd.execute(argument, in, out);
                        } else {
                            // respond to the client
                            out.println("Command not found " + Utils.colorize(command, Colors.YELLOW));
                            out.println("end");
                        }

                    }
                } catch (Exception e) {
                    if (verbose) {
                        Utils.title("Client :" + storage.getCurrentSocket().getInetAddress().getHostAddress() + ":"
                                + storage.getCurrentSocket().getPort() + " disconnected", Colors.RED_H);
                    }
                }

            }).start();

        } catch (Exception e) {
            if(verbose){
                System.err.println("No client connection durrint this wait time");
            }
        }
    }
}
