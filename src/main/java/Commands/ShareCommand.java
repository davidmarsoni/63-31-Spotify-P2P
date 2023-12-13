package Commands;

import utils.*;
import java.io.*;

import Classes.MusicFile;

public class ShareCommand implements Command{
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        Utils.titleDesc("Share a file", "Share a file or a playlist with other users");

        if(argument == null){
            Utils.p("You need to specify a file or a folder to share. please refer to "+Utils.ANSI_YELLOW+"help share"+Utils.ANSI_RESET+" for more information");
            return;
        }
        
        String args[] = argument.split(" ");
        if(args.length == 1){
            File file = new File(args[0]);
            MusicFile musicFile = new MusicFile(storage.getClientAddress(),storage.getClientPort(),file.getName(),file.getAbsolutePath());
            Utils.p("file");
        }else if(args.length == 2){
            //TODO : get the folder directory and the name of the playlist and create a playlist object
            Utils.p("folder");
        }
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            //send the command to the server
            out.println("share");

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
        String message = "Share a file with other users \n"+
                "Usage: "+Utils.ANSI_YELLOW+"share "+Utils.ANSI_RESET+Utils.ANSI_GREEN+"<file>"+Utils.ANSI_RESET+" to share a music file\n"+
                "       "+Utils.ANSI_YELLOW+"share "+Utils.ANSI_RESET +Utils.ANSI_GREEN+"<folder> <name> "+ Utils.ANSI_RESET+"to share a playlist (you will need to specify a name later\n";
        return message;
    }
}
