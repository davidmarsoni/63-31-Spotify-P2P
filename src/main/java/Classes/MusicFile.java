package Classes;
import utils.Utils;

public class MusicFile extends Entry {
    private String MusicName;
    private String MusicPath;

    public MusicFile(String ClientAdress, int ClientPort, String MusicName, String MusicPath) {
        super(ClientAdress, ClientPort);
        this.MusicName = MusicName;
        this.MusicPath = MusicPath;
    }

    public String getMusicName() {
        return this.MusicName;
    }

    public void setMusicName(String MusicName) {
        this.MusicName = MusicName;
    }

    public String getMusicPath() {
        return this.MusicPath;
    }

    public void setMusicPath(String MusicPath) {
        this.MusicPath = MusicPath;
    }
    @Override
    public String toString() {
        return Utils.ANSI_GREEN + getMusicName() + Utils.ANSI_RESET + " is available on " + Utils.ANSI_BLUE + getClientAdress() + ":" + getClientPort() + Utils.ANSI_RESET + "\n";
    }
}
