package CommandsClient;

import java.io.PrintWriter;

import utils.Colors;
import utils.StorageClient;
import utils.Utils;

/**
 * This command is use to disconnect the client from the server
 */
public class Disconnect implements Command {
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        if (storage.getClientSocket(false) == null) {
            return;
        }

        System.out.println("Trying to disconnect from the connection: "
                + Utils.colorize(storage.getServerAddress(),Colors.PURPLE) + ":"
                + Utils.colorize(String.valueOf(storage.getServerPort()), Colors.PURPLE));
        try {
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
            // send the command to the server
            out.println("end");
            // reset the client socket
            storage.setClientSocket(null);

            System.out.println("Disconnected from the connection: "
                    + Utils.colorize(storage.getServerAddress(), Colors.PURPLE) + ":"
                    + Utils.colorize(String.valueOf(storage.getServerPort()), Colors.PURPLE));

            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        }

    }

    @Override
    public String help() {
        return "Disconnect the client from the server";
    }

}
