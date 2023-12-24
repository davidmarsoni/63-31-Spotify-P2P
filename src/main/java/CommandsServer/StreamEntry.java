package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import utils.Colors;
import utils.StorageClient;
import utils.Utils;

public class StreamEntry implements CommandServer{
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        out.println("connected to the server" + Utils.colorize(storage.getServerAddress(),Colors.DARK_PURPLE) + ":" + Utils.colorize(String.valueOf(storage.getServerPort()),Colors.DARK_PURPLE));
        
    }

    @Override
    public String help() {
        return "Stream a file or a playlist to a ohter user";
    }
    
}
