package Commands;

import utils.Storage;
import java.io.*;

public class ListMusicsCommand implements Command{
    private Storage storage = Storage.getInstance();
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
            //listen the response from the server
            String response = "";
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
            }
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