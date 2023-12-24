package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Classes.Entry;
import Classes.MusicFile;
import Classes.PlayList;
import utils.Colors;
import utils.Utils;
import java.util.ArrayList;

public class HandleShareUnShare implements CommandServer {
    private utils.StorageServer storage = utils.StorageServer.getInstance();
    private String type;

    public HandleShareUnShare(String type) {
        this.type = type;
    }

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        // get the data from the client input not the argument
        try {
            String data = in.readLine();
            // split the data to get the type of the share and the name of the file or
            // playlist
            String[] splitData = data.split("#");
            // if the type is a file
            if (splitData[0].equals("file")) {
                MusicFile file = new MusicFile(storage.getCurrentClientAddress(), storage.getCurrentClientPort(), splitData[1],
                        splitData[2]);
                // test if the file already exist in the list of music (same name and same path)
                // test for the music file name and path
                for (Entry entry : storage.getSharedEntries()) {
                    if (entry.getName().equals(file.getName()) && entry.getPath().equals(file.getPath())
                            && entry.getClientAdress().equals(file.getClientAdress())
                            && entry.getClientPort() == file.getClientPort()) {
                        if (type.equals("share")) {
                            out.println("The file " + Utils.colorize(file.getName(), Colors.GREEN)
                                    + " already exist in the list of music on the server");
                        } else {
                            out.println("Music file " + Utils.colorize(file.getName(), Colors.GREEN)
                                    + " removed from the list of music");
                            storage.removeSharedEntry(entry);
                        }
                        out.println("end");
                        return;
                    }
                }
                if (type.equals("share")) {
                    // add the file to the list of music
                    storage.addSharedEntry(file);
                    out.println("Music file " + Utils.colorize(file.getName(), Colors.GREEN)
                            + " added to the list of music");

                } else {
                    out.println("The file " + Utils.colorize(file.getName(), Colors.GREEN)
                            + " doesn't exist in the list of music on the server");
                }
                out.println("end");

            } else if (splitData[0].equals("playlist")) {
                // get the playlist info from the client
                String playlistName = splitData[1].trim();
                String playlistPath = splitData[2];

                // the end are the list of music file in the playlist and it end with end
                // is the splitData[3] to the end  data.substring(data.indexOf("#", data.indexOf(playlistPath) + 1) + 1).split("#");
                ArrayList<String> musicFiles = new ArrayList<String>();
                String[] musicFilesData = data.substring(data.indexOf("#", data.indexOf(playlistPath) + 1) + 1)
                        .split("#");
                for (String musicFileData : musicFilesData) {
                    musicFiles.add(musicFileData);
                }
                //create the playlist
                PlayList playlist = new PlayList(storage.getCurrentClientAddress(), storage.getCurrentClientPort(), playlistPath,
                        playlistName, musicFiles);

                //test if the playlist already exist in the list of music (same name and same path)
                for (Entry entry : storage.getSharedEntries()) {
                    if (entry.getName().equals(playlistName) && entry.getPath().equals(playlistPath)
                            && entry.getClientAdress().equals(storage.getCurrentClientAddress())
                            && entry.getClientPort() == storage.getCurrentClientPort()) {
                        if (type.equals("share")) {
                            out.println("The playlist " + Utils.colorize(playlistName, Colors.GREEN)
                                    + " already exist in the list of music on the server");
                        } else {
                            out.println("Playlist " + Utils.colorize(playlistName, Colors.GREEN)
                                    + " removed from the list of music");
                            storage.removeSharedEntry(entry);
                        }
                        out.println("end");
                        return;
                    }
                }

                
                if (type.equals("share")) {
                    // add the playlist to the list of music
                    storage.addSharedEntry(playlist);
                    out.println("Playlist " + Utils.colorize(playlistName, Colors.GREEN)
                            + " added to the list of music");
                } else {
                    out.println("The playlist " + Utils.colorize(playlistName, Colors.GREEN)
                            + " doesn't exist in the list of music on the server");
                }
                out.println("end");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "This command is a superCommand and doesn't have a help method it just there to avoid code duplication");
    }

}
