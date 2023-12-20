package CommandsClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import utils.Colors;
import utils.StorageClient;
import utils.Utils;

public class Test implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument) {
        if(storage.getClientSocket()==null){
            return;
        }
        //send a test message to the server
        System.out.println("Test command");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);
            //send the command to the server
            out.println("test");
            //listen the response from the server
            String response = "";
            while ((response = in.readLine()) != null && !response.equalsIgnoreCase("end")) {
                System.out.println(response);
            }
        }catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        } 

    }

    @Override
    public String help() {
        return "Test command need to be "+Colors.RED_H+"removed"+Colors.RESET+" from the final version";
    }
    
}
