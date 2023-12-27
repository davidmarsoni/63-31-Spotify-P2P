package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import CommandsServer.CommandServer;

//TODO : allow to pass a Logger class to the server management to be able to fit the server code inside a same class
public class ServerManagement {

    public static ServerSocket initializedServerSocket(int port) {
        return initializedServerSocket("eth0", port, 10, 180000, true);
    }

    public static ServerSocket initializedServerSocket(String interfaceName, int port) {
        return initializedServerSocket(interfaceName, port, 10, 180000, true);
    }

    public static ServerSocket initializedServerSocket(String interfaceName, int port, int backlog, int timeout,
            boolean verbose) {
        InetAddress localAddress = null;
        try {
            NetworkInterface ni = NetworkInterface.getByName(interfaceName);
            Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress ia = inetAddresses.nextElement();

                if (!ia.isLinkLocalAddress()) {
                    if (!ia.isLoopbackAddress()) {
                        if (verbose) {
                            System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
                        }

                        localAddress = ia;
                    }
                }
            }
            if (localAddress == null) {
                if (verbose) {
                    System.out.println("No non-local address found for interface " + interfaceName);
                }
            }

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
                storage.setClientSocket(currentServerSocket);
                if (verbose) {
                    System.out.println(
                            "Connection accepted from " + storage.getClientSocket().getInetAddress().getHostAddress()
                                    + ":" + storage.getClientSocket().getPort());
                }
                // listen command from client
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(storage.getClientSocket().getInputStream()));
                    PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
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
                        e.printStackTrace();
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