package Classes;
import java.util.ArrayList;

import utils.Utils;

public class PlayList extends Entry {
    private ArrayList<String> MusicFiles;

    public PlayList(String ClientAdress, int ClientPort, String path,String name, ArrayList<String> MusicFiles) {
        super(ClientAdress, ClientPort,name,path);
        
        this.MusicFiles = MusicFiles;
    }

    public ArrayList<String> getMusicFiles() {
        return this.MusicFiles;
    }

    public void setMusicFiles(ArrayList<String> MusicFiles) {
        this.MusicFiles = MusicFiles;
    }

    @Override
    public String toString() {
        String result = "PlayList: " + Utils.ANSI_GREEN + getName() + Utils.ANSI_RESET + " is available on " + Utils.ANSI_BLUE + getClientAdress() + ":" + getClientPort() + Utils.ANSI_RESET + "\n";
        for (String musicFile : MusicFiles) {
            result += musicFile + "\n";
        }
        return result;
    }
}
