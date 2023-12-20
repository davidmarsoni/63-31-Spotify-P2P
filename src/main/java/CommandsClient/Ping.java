package CommandsClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import utils.Colors;
import utils.StorageClient;
import utils.Utils;

public class Ping implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        int numberOfPing = 1;
        if (argument != null) {
            try {
                numberOfPing = Integer.parseInt(argument);
            } catch (Exception e) {
                System.out.println(Utils.colorize("Error : ", Colors.RED) + "The argument must be a number");
                numberOfPing = 1;
                return;
            }
            if(numberOfPing < 1){
                System.out.println(Utils.colorize("Error : ", Colors.RED) + "The argument must be a number greater than 0");
                return;
            }

            if(numberOfPing > 10){
                System.out.println(Utils.colorize("Error : ",Colors.RED) + "The argument must be a number less than 100");
                return;
            }
        }

        // check if the client is connected to the server
        if (storage.getClientSocket() == null) {
            return;
        }

        for (int i = 0; i < numberOfPing; i++) {

            // get the list of music from the server
            try {
                // inform the user
                System.out.println("Try to ping " + storage.getServerAddress() + ":" + storage.getServerPort());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(storage.getClientSocket().getInputStream()));
                PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

                // send the command to the server
                out.println("ping");
                System.out.println("Ping");
                // listen the response from the server
                String response = "";

                // wait for the end of the list
                while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                    System.out.println(response);
                }
            } catch (Exception e) {
                System.err.println("Error handling client connection");
                e.printStackTrace();
            }
        }

    }

    @Override
    public String help() {
        String help = "This command is used to ping the server\n";
        return help;
    }

}
