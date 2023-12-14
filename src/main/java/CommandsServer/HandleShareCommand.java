package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.MusicFile;

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
                storage.addEntry(file);
                out.println("Music file "+file.toString()+" added to the list of music");
                out.println("end");

                
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
