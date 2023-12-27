package Classes;
import java.util.ArrayList;

import utils.Colors;
import utils.Utils;

public class PlayList extends Entry {
    private ArrayList<String> MusicNames; // the list of music Names
    private String type = "playList"; // the type of the entry (musicFile or playlist) use for json serialization

    public PlayList() {
        this.MusicNames = new ArrayList<>();
    }

    public PlayList(String ClientAdress, int ClientPort, String path,String name, ArrayList<String> MusicFiles) {
        super(ClientAdress, ClientPort,name,path);
        
        this.MusicNames = MusicFiles;
    }

    public ArrayList<String> getMusicNames() {
        return this.MusicNames;
    }

    public void setMusicNames(ArrayList<String> MusicFiles) {
        this.MusicNames = MusicFiles;
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
            result += Utils.colorize("| ",BLUE) + Utils.colorize("host",WHITE)+ " : "+ Utils.colorize(getClientAdress(),Colors.DARK_PURPLE)+ ":" + Utils.colorize(String.valueOf(getClientPort()), Colors.DARK_PURPLE) +"\n";
            result += Utils.colorize("| ", BLUE) + Utils.colorize("Musics names", WHITE)+"\n";
            result += Utils.colorize("| ", BLUE) + Utils.colorize("------------", WHITE)+"\n";
            for (String musicFile : MusicNames) {
                result += Utils.colorize("| ", BLUE) + musicFile +"\n";
            }
        }else{
            result = Utils.colorize(getName(), GREEN);
        }
        return result;
    }
}
