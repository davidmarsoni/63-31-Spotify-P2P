package CommandsClient;

import utils.*;
import java.io.*;

/**
 * ListMusics command
 * This command is used to list all Entry available on the server
 */
public class List implements CommandClient{
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument) {
        //check if the client is connected to the server
        if(storage.getClientSocket() == null) {
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
            Utils.title("List of music from " + storage.getServerAddress() + ":" + storage.getServerPort(), Utils.ANSI_BLUE_H);
            //listen the response from the server
            String response = "";
            
            //wait for the end of the list
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
            }
            Utils.p("");
            Utils.p("End of the list");
                
        }catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        } 
    }

    @Override
    public String help() {
       
        return "List all the musics available on the server";
    }

}
