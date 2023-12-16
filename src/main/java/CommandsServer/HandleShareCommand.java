package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class HandleShareCommand implements CommandServer {
    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        HandleShareUnShare handleShareUnShare = new HandleShareUnShare("share");
        handleShareUnShare.execute(argument, in, out);
    }

    @Override
    public String help() {
        return "Handle the complete share of a file or a playlist to the sever from a client";
    }

}
