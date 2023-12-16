package Classes;
import utils.Utils;

public class MusicFile extends Entry {
    public MusicFile(String ClientAdress, int ClientPort, String name, String path) {
        super(ClientAdress, ClientPort,name,path);
    }
    @Override
    public String toString() {
        return Utils.ANSI_GREEN + getName() + Utils.ANSI_RESET + " is available on " + Utils.ANSI_BLUE + getClientAdress() + ":" + getClientPort() + Utils.ANSI_RESET + "\n";
    }
}
