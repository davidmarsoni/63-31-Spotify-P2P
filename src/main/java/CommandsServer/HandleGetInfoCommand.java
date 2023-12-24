package CommandsServer;

import java.io.*;

import Classes.Entry;
import Classes.MusicFile;
import Classes.PlayList;
import utils.Colors;
import utils.StorageServer;
import utils.Utils;

public class HandleGetInfoCommand implements CommandServer {
    private static StorageServer storage = StorageServer.getInstance();
    public HandleGetInfoCommand() {
    }

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        // wait for info from the client
        System.out.println("Waiting for info from the client");
        try {
            String data = in.readLine();
            String splitData[] = data.split("#");
            String name = splitData[0];
            //test if it is a file or a playlist
            String[] ipAndPort = null;
            if (splitData.length > 1) {
                ipAndPort = splitData[1].split(":");
            }
            Entry entry;
             if (ipAndPort == null) {
                entry = storage.findEntryByName(name);
            } else {
                entry = storage.findEntryByNameAndClientAdressAndPort(name, ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            }
            File file = new File(entry.getPath());

            if (!file.isDirectory()) {
                MusicFile musicFile;
                try {
                    musicFile = (MusicFile) entry;
                } catch (Exception e) {
                    musicFile = null;
                }

                if(musicFile == null){
                    out.println("The file " + Utils.colorize(name, Colors.GREEN) + " doesn't exist in the list of music on the server");
                    out.println("end");
                    return;
                }
               
                out.println("data#file#" + musicFile.getName() + "#" + musicFile.getPath() + "#" + musicFile.getClientAdress() + "#" + musicFile.getClientPort());
            } else {
                PlayList playList;
                try {
                    playList = (PlayList) entry;
                } catch (Exception e) {
                    playList = null;
                }

                if(playList == null){
                    out.println("The playlist " + Utils.colorize(name, Colors.GREEN) + " doesn't exist in the list of music on the server");
                    out.println("end");
                    return;
                }

                out.println("data#playlist#" + playList.getName() + "#" + playList.getClientAdress() + "#" + playList.getClientPort());
            }

            out.println("end");
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public String help() {
        return "return the details of the file by it file name name path ip and port of the host";
    }
}
