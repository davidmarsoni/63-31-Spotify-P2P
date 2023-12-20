package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import utils.StorageServer;

public class PingCommand implements CommandServer {
    private StorageServer storage = StorageServer.getInstance();
    @Override
    public String help() {
        String help = "This command is used to reply to a ping from a client\n";
        return help;
    }

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        storage.printLog("Ping received from client");
        out.println("Pong");
        storage.printLog("Pong send to client");
        out.println("end");
    }
  
}
