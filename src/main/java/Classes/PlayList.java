package Classes;
import java.lang.reflect.Array;
import java.util.ArrayList;

import utils.Utils;

public class PlayList extends Entry {
    private String PlayListName;
    private ArrayList<MusicFile> MusicFiles;

    public PlayList(String ClientAdress, int ClientPort, String PlayListName, ArrayList<MusicFile> MusicFiles) {
        super(ClientAdress, ClientPort);
        this.PlayListName = PlayListName;
        this.MusicFiles = MusicFiles;
    }

    public String getPlayListName() {
        return this.PlayListName;
    }

    public void setPlayListName(String PlayListName) {
        this.PlayListName = PlayListName;
    }

    public ArrayList<MusicFile> getMusicFiles() {
        return this.MusicFiles;
    }

    public void setMusicFiles(ArrayList<MusicFile> MusicFiles) {
        this.MusicFiles = MusicFiles;
    }

    @Override
    public String toString() {
        String result = "PlayList: " + Utils.ANSI_GREEN + getPlayListName() + Utils.ANSI_RESET + " is available on " + Utils.ANSI_BLUE + getClientAdress() + ":" + getClientPort() + Utils.ANSI_RESET + "\n";
        for (MusicFile musicFile : MusicFiles) {
            result += musicFile.getMusicName() + "\n";
        }
        return result;
    }
}
