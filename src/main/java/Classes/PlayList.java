package Classes;
import java.util.ArrayList;

import utils.Colors;
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
        String BLUE = Colors.BLUE;
        String WHITE = Colors.WHITE;
        String GREEN = Colors.GREEN;
        if (complete) {
            result= Utils.colorize("| ", BLUE) + Utils.colorize("PlayList",WHITE)+ " : "+ Utils.colorize(getName(),GREEN) +"\n";
            result += Utils.colorize("| ",BLUE) + Utils.colorize("peer",WHITE)+ " : "+ Utils.colorize(getClientAdress(),Colors.DARK_PURPLE)+ ":" + Utils.colorize(String.valueOf(getClientPort()), Colors.DARK_PURPLE) +"\n";
            result += Utils.colorize("| ", BLUE) + Utils.colorize("Musics names", WHITE)+"\n";
            result += Utils.colorize("| ", BLUE) + Utils.colorize("------------", WHITE)+"\n";
            for (String musicFile : MusicFiles) {
                result += Utils.colorize("| ", BLUE) + musicFile +"\n";
            }
        }else{
            result = Utils.colorize(getName(), GREEN);
        }
        return result;
    }
}
