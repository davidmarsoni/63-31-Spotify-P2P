package CommandsClient;

import java.io.*;
import java.net.*;
import utils.*;

/**
 * Connect Command
 * This command is used to connect to the server with the current configuration
 * (server address and port)
 */
public class Connect implements Command {
    // get the instance of the storage to get the data
    private StorageClient storage = StorageClient.getInstance();

    // server address and port just to have a shorter name
    private String serverName;
    private int serverPort;

    public Connect() {
    }

    @Override
    public void execute(String argument) {
        // get the server address and port from the storage
        this.serverName = storage.getServerAddress();
        this.serverPort = storage.getServerPort();
        // inform the user that the client is trying to connect to the server
        System.out.println("Try to establish a connection to " + serverName + ":" + serverPort);

        // try to connect to the server
        try {
            InetAddress serverAddress = InetAddress.getByName(serverName);
            Socket clientSocket = new Socket(serverAddress, serverPort);

            // set the client socket in the storage
            storage.setClientSocket(clientSocket);

            // set info to the server on with port the client is listening
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
            out.println("sendInfo");
            out.println(storage.getLocalAdress().getHostAddress() + " " + storage.getPort());

            System.out.println("Connected to " + serverAddress.getHostAddress() + ":" + serverPort);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverName);
        } catch (ConnectException e) {
            System.err.println("Connection refused by server: " + serverName);
        } catch (IOException e) {
            System.err.println("I/O error for the connection to: " + serverName);
        }
    }

    @Override
    public String help() {
        return "Connect to the current server in parametter " +
                Utils.colorize(storage.getServerAddress(),Colors.DARK_PURPLE) +
                ":" +
                Utils.colorize(String.valueOf(storage.getServerPort()), Colors.DARK_PURPLE) +
                " (you can change it with the command " + Utils.colorize("init", Colors.YELLOW) + ")";
    }
}
