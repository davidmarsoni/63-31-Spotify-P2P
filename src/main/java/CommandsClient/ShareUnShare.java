package CommandsClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import Classes.MusicFile;
import Classes.PlayList;
import utils.StorageClient;
import utils.Utils;

public class ShareUnShare implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();
    private String type = "";
    private String data = "";
    
    public ShareUnShare(String Type) {
        this.type = Type;
    }

    @Override
    public void execute(String argument) {
        //check if the client is connected to the server
        if(storage.getClientSocket() == null) {
            return;
        }

        if(type.equals("share")){
            Utils.title("Share file ...");
        }else if(type.equals("unshare")){
            Utils.title("Unshare file ...");
        }

        if(argument == null){
            Utils.p("You need to specify a file or a folder to "+type+". please refer to "+Utils.colorize("help "+type, Utils.ANSI_YELLOW)+" for more information");
            return;
        }
        
        String args[] = argument.split(" ");
        if(args.length == 1){
            File file = new File(args[0]);
            //test if the file exist
            if(!file.exists()){
                Utils.p("The file "+Utils.ANSI_BLUE+args[0]+Utils.ANSI_RESET+" doesn't exist");
                return;
            }

            MusicFile musicFile = new MusicFile(storage.getClientAddress(),storage.getClientPort(),file.getName(),file.getAbsolutePath());
            if(type.equals("share")){
                storage.addSharedEntry(musicFile);
            }else if(type.equals("unshare")){
                storage.removeSharedEntry(musicFile);
            }

            data = "file "+musicFile.getName()+" "+musicFile.getPath();
           
        }else if(args.length == 2){
            ArrayList<String> musicFiles = new ArrayList<String>();
            File folder = new File(args[0]);
            File[] listOfFiles = folder.listFiles();
            if(!folder.exists() || listOfFiles.length == 0){
                Utils.p("The folder "+Utils.ANSI_BLUE+args[0]+Utils.ANSI_RESET+" doesn't exist or is empty");
                return;
            }
            for (int i = 0; i < listOfFiles.length; i++) {
                if(listOfFiles[i].isFile()){
                    MusicFile musicFile = new MusicFile(storage.getClientAddress(),storage.getClientPort(),listOfFiles[i].getName(),listOfFiles[i].getAbsolutePath());
                    musicFiles.add(musicFile.getName());
                }
            }
            //create the playlist
            PlayList playList = new PlayList(storage.getClientAddress(),storage.getClientPort(),args[0],args[1],musicFiles);
            if(type.equals("share")){
                storage.addSharedEntry(playList);
            }else if(type.equals("unshare")){
                storage.removeSharedEntry(playList);
            }

            data = "playlist "+playList.getName()+" "+playList.getPath();

            for (String musicFile : musicFiles) {
                data += " "+musicFile;
            }
        }
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            //send the command to the server
            out.println(type);
            out.println(data);
            //listen the response from the server
            String response = "";
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
                //TODO : handle a error message from the server if the file already exist
            
            }
            if(type.equals("share")){
                Utils.title("Share Entry done");
            }else if(type.equals("unshare")){
                Utils.title("UnShare Entry done");
            }
        }catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("This command is a superCommand and doesn't have a help method it juste there to avoid code duplication");
    }
    
}
