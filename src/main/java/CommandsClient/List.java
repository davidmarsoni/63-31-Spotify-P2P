package CommandsClient;

import utils.*;
import java.io.*;

/**
 * ListMusics command
 * This command is used to list all Entry available on the server
 */
public class List implements Command{
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument) {

        if(argument != null) {
        
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
                PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

                String[] splitData = argument.split(" ");

                if(splitData.length > 2) {
                    System.out.println("this command only accept one or 2 arguments see " + Utils.colorize("help list", Colors.YELLOW) + " for more details");
                    return;
                }
                //send the command to the server
                out.println("list "+argument);

                //listen the response from the server
                String response = "";
               
                String name = null;
                String clientAddress = null;
                int clientPort = 0;
                String[] splitArgument = null;
    
                // Check if argument starts with a quote (single or double)
                if (argument.startsWith("\"") || argument.startsWith("'")) {
                    // Find the closing quote
                    int endQuoteIndex = argument.indexOf(argument.charAt(0), 1);
                    if (endQuoteIndex != -1) {
                        // Extract the name and remove the quotes
                        name = argument.substring(1, endQuoteIndex);
                        // Split the rest of the argument
                        splitArgument = argument.substring(endQuoteIndex + 1).trim().split(" ");
                    }
                } else {
                    // If no quotes, split the argument as before
                    splitArgument = argument.split(" ");
                    name = splitArgument[0];
                }
    
                if (splitArgument != null && splitArgument.length == 2) {
                    String[] addressPort = splitArgument[1].split(":");
                    clientAddress = addressPort[0];
                    clientPort = Integer.parseInt(addressPort[1]);
                }

                //send info to the client and it host if it is specified
                System.out.println("Try to get detailed information about " + Utils.colorize(name, Colors.GREEN) +(clientAddress != null ? " on this host " + Utils.colorize(clientAddress, Colors.PURPLE) + ":" + Utils.colorize(String.valueOf(clientPort), Colors.PURPLE) : " from the server " + Utils.colorize(storage.getServerAddress(), Colors.PURPLE) + ":" + Utils.colorize(String.valueOf(storage.getServerPort()), Colors.PURPLE)));
                System.out.println();
                while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                    System.out.println(response);
                }
                System.out.println("");

            //listen the response from the server
            }catch (Exception e) {
                System.err.println("Error handling client connection");
                e.printStackTrace();
            }
            return;
        }

        //check if the client is connected to the server
        if(storage.getClientSocket(true) == null) {
            return;
        }
        
        //inform the user 
        System.out.println("Try to get the list of music from " + storage.getServerAddress() + ":" + storage.getServerPort());

        // get the list of music from the server
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            //send the command to the server
            out.println("list");
            Utils.title("List of music from " + storage.getServerAddress() + ":" + storage.getServerPort(), Colors.BLUE_H);
            //listen the response from the server
            String response = "";
            
            //wait for the end of the list
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
            }
            System.out.println("");
            System.out.println("End of the list");
                
        }catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        } 
    }

    @Override
    public String help() {
        String help = "List all the musics available on the server\n";
        help += "Usage: " + Utils.colorize("list", Colors.YELLOW) + "\n";
        help += "Usage: " + Utils.colorize("list", Colors.YELLOW) + " " + Utils.colorize("<music/playlist name>", Colors.GREEN) + " to get all the information about the first music/playlist with this name on the server\n";

        help += "Usage: " + Utils.colorize("list", Colors.YELLOW) + " " + Utils.colorize("<music/playlist name>", Colors.GREEN) + " " + Utils.colorize("<host:port>", Colors.GREEN) + "\n";

        return help;
    }

}
