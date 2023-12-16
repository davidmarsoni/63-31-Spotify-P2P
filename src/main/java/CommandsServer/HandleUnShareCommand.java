package CommandsServer;

import java.io.*;


public class HandleUnShareCommand implements CommandServer {
    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        HandleShareUnShare handleShareUnShare = new HandleShareUnShare("unshare");
        handleShareUnShare.execute(argument, in, out);
    }

    @Override
    public String help() {
       return "Handle the complete unshare of a file or a playlist to the sever from a client";
    }
    
}
