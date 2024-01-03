package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Client;
import utils.Colors;
import utils.LogsServer;
import utils.StorageServer;
import utils.Utils;

public class ClientShareInfoCommand implements CommandServer {
    private StorageServer storage = StorageServer.getInstance();

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        try {
            String[] data = in.readLine().split(" ");
            String ip = data[0];
            int port = Integer.parseInt(data[1]);

            storage.setCurrentClientAddress(ip);
            storage.setCurrentClientPort(port);
            Client client = new Client(ip, port);
            storage.updateClient(client);

            LogsServer.info("Client address for listening : " + Utils.colorize(ip, Colors.PURPLE) + ":"
                    + Utils.colorize(String.valueOf(port), Colors.PURPLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "send the listening port and ip of the client to the server";
    }

}
