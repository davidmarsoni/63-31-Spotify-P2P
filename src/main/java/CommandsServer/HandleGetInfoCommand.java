package CommandsServer;

import java.io.*;

import Classes.Entry;
import Classes.MusicFile;
import Classes.PlayList;
import utils.Colors;
import utils.LogsServer;
import utils.StorageServer;
import utils.Utils;

public class HandleGetInfoCommand implements CommandServer {
    private static StorageServer storage = StorageServer.getInstance();
    public HandleGetInfoCommand() {
    }

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        // wait for info from the client
        LogsServer.info("Waiting for name of the file to get info from the client");
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
            File file;
            try {
                file = new File(entry.getPath());
            } catch (Exception e) {
                file = null;
                String text = "The file " + Utils.colorize(name, Colors.GREEN) + " doesn't exist in the list of music on the server";
                out.println(text);
                out.println("end");
                LogsServer.info(text);
                return;
            }
           

            if (!file.isDirectory()) {
                MusicFile musicFile;
                try {
                    musicFile = (MusicFile) entry;
                } catch (Exception e) {
                    musicFile = null;
                    LogsServer.info("Music file " + Utils.colorize(name, Colors.GREEN) + " is not a music file");
                }

                if(musicFile == null){
                    String text = "Music file " + Utils.colorize(name, Colors.GREEN) + " doesn't exist in the list of music on the server";
                    out.println(text);
                    out.println("end");
                    LogsServer.info(text);
                    return;
                }
               
                out.println("data#file#" + musicFile.getName() + "#" + musicFile.getPath() + "#" + musicFile.getClientAdress() + "#" + musicFile.getClientPort());
                LogsServer.info("Sending info of the music file " + Utils.colorize(musicFile.getName(), Colors.GREEN) + " to the client");
            } else {
                PlayList playList;
                try {
                    playList = (PlayList) entry;
                } catch (Exception e) {
                    playList = null;
                    LogsServer.info("Playlist " + Utils.colorize(name, Colors.GREEN) + " is not a playlist");
                    out.println("Playlist " + Utils.colorize(name, Colors.GREEN) + " dosn't exist in the on the server");

                }

                if(playList == null){
                    String text = "Playlist " + Utils.colorize(name, Colors.GREEN) + " doesn't exist in the list of music on the server";
                    out.println(text);
                    out.println("end");
                    LogsServer.info(text);
                    return;
                }

                out.println("data#playlist#" + playList.getName() + "#" + playList.getPath() + "#" + playList.getClientAdress() + "#" + playList.getClientPort());
                LogsServer.info("Sending info of the playlist " + Utils.colorize(playList.getName(), Colors.GREEN) + " to the client");
            }

            out.println("end");
           
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String help() {
        return "return the details of the file by it file name name path ip and port of the host";
    }
}
