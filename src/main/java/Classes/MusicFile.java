package Classes;
import utils.Colors;
import utils.Utils;

/**
 * This class is use to store information about a music file
 */
public class MusicFile extends Entry {
    /**
     * The type of the entry use for json serialization
     */
    private String type = "musicFile";

    public MusicFile() {}

    public MusicFile(String ClientAdress, int ClientPort, String name, String path) {
        super(ClientAdress, ClientPort,name,path);
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
            result= Utils.colorize("| ",BLUE) + Utils.colorize("MusicFile", WHITE)+ " : "+ Utils.colorize(getName(), GREEN) +"\n";
            result += Utils.colorize("| ", BLUE) + Utils.colorize("host", WHITE)+ " : "+ Utils.colorize(getClientAdress(),Colors.PURPLE)+ ":" + Utils.colorize(String.valueOf(getClientPort()), Colors.PURPLE) +"\n";
        }else{
            result = Utils.colorize(getName(), GREEN);
        }
        return result;
    }
}
