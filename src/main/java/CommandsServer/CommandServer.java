package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface CommandServer {
    public void execute(String argument,BufferedReader in,PrintWriter out);

    public String help();
}
