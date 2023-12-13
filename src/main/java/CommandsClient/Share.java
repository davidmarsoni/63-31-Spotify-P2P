package CommandsClient;

import utils.*;
import java.io.*;
import java.util.ArrayList;

import Classes.MusicFile;
import Classes.PlayList;

public class Share implements CommandClient{
    private StorageClient storage = StorageClient.getInstance();
    private String data = "";
    @Override
    public void execute(String argument) {
        Utils.titleDesc("Share a file", "Share a file or a playlist with other users");

        if(argument == null){
            Utils.p("You need to specify a file or a folder to share. please refer to "+Utils.ANSI_YELLOW+"help share"+Utils.ANSI_RESET+" for more information");
            return;
        }
        
        String args[] = argument.split(" ");
        if(args.length == 1){
            System.out.println(args[0]);
        
            File file = new File(args[0]);
            //test if the file exist
            if(!file.exists()){
                Utils.p("The file "+Utils.ANSI_BLUE+args[0]+Utils.ANSI_RESET+" doesn't exist");
                return;
            }
            MusicFile musicFile = new MusicFile(storage.getClientAddress(),storage.getClientPort(),file.getName(),file.getAbsolutePath());
            storage.addSharedEntry(musicFile);

           
            data = "file "+musicFile.getMusicName()+" "+musicFile.getMusicPath();
        }else if(args.length == 2){
            ArrayList<MusicFile> musicFiles = new ArrayList<MusicFile>();
            File folder = new File(args[0]);
            File[] listOfFiles = folder.listFiles();
            if(!folder.exists() || listOfFiles.length == 0){
                Utils.p("The folder "+Utils.ANSI_BLUE+args[0]+Utils.ANSI_RESET+" doesn't exist or is empty");
                return;
            }
            for (int i = 0; i < listOfFiles.length; i++) {
                if(listOfFiles[i].isFile()){
                    MusicFile musicFile = new MusicFile(storage.getClientAddress(),storage.getClientPort(),listOfFiles[i].getName(),listOfFiles[i].getAbsolutePath());
                    musicFiles.add(musicFile);
                }
            }
            //create the playlist
            PlayList playList = new PlayList(storage.getClientAddress(),storage.getClientPort(),args[1],musicFiles);
            storage.addSharedEntry(playList);
        }
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            //send the command to the server
            out.println("share");
            out.println(data);
            //listen the response from the server
            String response = "";
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
            
            }

            in.close();
            out.close();
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
