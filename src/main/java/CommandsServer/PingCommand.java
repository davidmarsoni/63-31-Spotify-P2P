package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import utils.LogsServer;

public class PingCommand implements CommandServer {
    @Override
    public String help() {
        String help = "This command is used to reply to a ping from a client\n";
        return help;
    }

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        LogsServer.info("Ping received from client");
        out.println("Pong");
        LogsServer.info("Pong send to client");
        out.println("end");
    }
  
}
