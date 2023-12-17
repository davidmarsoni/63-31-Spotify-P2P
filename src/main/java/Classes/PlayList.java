package Classes;
import java.util.ArrayList;

import utils.Utils;

public class PlayList extends Entry {
    private ArrayList<String> MusicFiles;
    private String type = "playList";

    public PlayList() {
        this.MusicFiles = new ArrayList<>();
    }

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
        return toString(false);
    }

    public String toString(Boolean complete) {
        String result = "";
        if (complete) {
            result= Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("PlayList", Utils.ANSI_WHITE)+ " : "+ Utils.colorize(getName(), Utils.ANSI_GREEN) +"\n";
            result += Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("peer", Utils.ANSI_WHITE)+ " : "+ Utils.colorize(getClientAdress(),Utils.ANSI_DARK_PURPLE)+ ":" + Utils.colorize(String.valueOf(getClientPort()), Utils.ANSI_DARK_PURPLE) +"\n";
            result += Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("Musics names", Utils.ANSI_WHITE)+"\n";
            result += Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("------------", Utils.ANSI_WHITE)+"\n";
            for (String musicFile : MusicFiles) {
                result += Utils.colorize("| ", Utils.ANSI_BLUE) + musicFile +"\n";
            }
        }else{
            result = Utils.colorize(getName(), Utils.ANSI_GREEN);
        }
        return result;
    }
}
