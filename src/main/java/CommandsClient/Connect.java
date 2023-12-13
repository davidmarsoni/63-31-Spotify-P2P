package CommandsClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.StorageClient;
import utils.Utils;

public class Connect implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();
    private String serverName;
    private int serverPort;

    public Connect() {
    }

    @Override
    public void execute(String argument) {
        this.serverName = storage.getServerAddress();
        this.serverPort = storage.getServerPort();
        System.out.println("Try to establish a connection to " + serverName + ":" + serverPort);
        try {
            InetAddress serverAddress = InetAddress.getByName(serverName);
            Socket clientSocket = new Socket(serverAddress, serverPort);
            storage.setClientSocket(clientSocket);
            //send listening port and ip of the client to the server
            

            System.out.println("Connected to " + serverAddress);
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
        return "Connect to a server with " + Utils.ANSI_BLUE + storage.getServerAddress() + Utils.ANSI_RESET + ":" + Utils.ANSI_BLUE + storage.getServerPort() + Utils.ANSI_RESET + " as default (you can change it with the command "+Utils.ANSI_YELLOW+"init"+Utils.ANSI_RESET+")";
    }
}

