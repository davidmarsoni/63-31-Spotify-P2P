package CommandsClient;

import utils.StorageClient;
import utils.Utils;

import java.io.*;

import javax.swing.text.Utilities;

public class ListMusics implements CommandClient{
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument) {
        if(storage.getClientSocket() == null) {
            return;
        }
        
        System.out.println("Try to get the list of music from " + storage.getServerAddress() + ":" + storage.getServerPort());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            //send the command to the server
            out.println("listMusics");
            Utils.title("List of music from " + storage.getServerAddress() + ":" + storage.getServerPort(), Utils.ANSI_BLUE_H);
            //listen the response from the server
            String response = "";
            
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
       
        return "List all the musics on the server";
    }

}
