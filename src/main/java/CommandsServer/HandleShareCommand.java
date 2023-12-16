package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Entry;
import Classes.MusicFile;
import utils.Utils;

public class HandleShareCommand implements CommandServer {
    private utils.StorageServer storage = utils.StorageServer.getInstance();
    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        //get the data from the client input not the argument
        try {
            String data = in.readLine();
            //split the data to get the type of the share and the name of the file or playlist
            String[] splitData = data.split(" ");
            //if the type is a file
            if(splitData[0].equals("file")){

                MusicFile file = new MusicFile(storage.getClientAddress(), storage.getClientPort(), splitData[1], splitData[2]);
                //test if the file already exist in the list of music (same name and same path) 
                //test for the music file name and path
                for (Entry entry : storage.getEntries()) {
                    if(entry.getName().equals(file.getName()) && entry.getPath().equals(file.getPath())){
                        out.println("The file "+Utils.ANSI_GREEN+ file.getName()+Utils.ANSI_RESET+" already exist in the list of music on the server");
                        
                        out.println("end");
                        return;
                    }
                }
                out.println("Music file "+Utils.ANSI_GREEN+ file.getName()+Utils.ANSI_RESET+" added to the list of music");
                out.println("end");
                //add the file to the list of music
                storage.addEntry(file);

             

                
            }else if(splitData[0].equals("playlist")){
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public String help() {
       return "Handle the complete share of a file or a playlist to the sever from a client";
    }
    
}
