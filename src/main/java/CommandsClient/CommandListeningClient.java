package CommandsClient;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface CommandListeningClient {
    public void execute(String argument,BufferedReader in,PrintWriter out);
    public String help();
}
