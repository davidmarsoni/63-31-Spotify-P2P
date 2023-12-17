package Classes;
import utils.Utils;

public class MusicFile extends Entry {
    private String type = "musicFile";

    public MusicFile() {
      
    }
    public MusicFile(String ClientAdress, int ClientPort, String name, String path) {
        super(ClientAdress, ClientPort,name,path);
    }
    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(Boolean complete) {
        String result = "";
        if (complete) {
            result= Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("MusicFile", Utils.ANSI_WHITE)+ " : "+ Utils.colorize(getName(), Utils.ANSI_GREEN) +"\n";
            result += Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("peer", Utils.ANSI_WHITE)+ " : "+ Utils.colorize(getClientAdress(),Utils.ANSI_DARK_PURPLE)+ ":" + Utils.colorize(String.valueOf(getClientPort()), Utils.ANSI_DARK_PURPLE) +"\n";
            result += Utils.colorize("| ", Utils.ANSI_BLUE) + Utils.colorize("path", Utils.ANSI_WHITE)+ " : "+ Utils.colorize(getPath(),Utils.ANSI_DARK_PURPLE) +"\n";
        }else{
            result = Utils.colorize(getName(), Utils.ANSI_GREEN);
        }
        return result;
    }
}
