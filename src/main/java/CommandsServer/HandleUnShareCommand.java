package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Entry;
import Classes.MusicFile;
import utils.Utils;

public class HandleUnShareCommand implements CommandServer {
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
                //test if the file already exist in the list of music (same name and same path) if yes remove it 
                //test for the music file name and path
                for (Entry entry : storage.getEntries()) {
                    if(entry.getName().equals(splitData[1]) && entry.getPath().equals(splitData[2]) && entry.getClientAdress().equals(storage.getClientAddress()) && entry.getClientPort() == storage.getClientPort()){
                        out.println("Music file "+Utils.ANSI_GREEN+ entry.getName()+Utils.ANSI_RESET+" removed from the list of music");
                        out.println("end");
                        //remove the file from the list of music
                        storage.removeEntry(entry);
                        return;
                    }
                }
             
                out.println("The file "+Utils.ANSI_GREEN+ splitData[1]+Utils.ANSI_RESET+" doesn't exist in the list of music on the server");

            }else if(splitData[0].equals("playlist")){
                //TODO : handle the unshare of a playlist
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public String help() {
       return "Handle the complete unshare of a file or a playlist to the sever from a client";
    }
    
}
