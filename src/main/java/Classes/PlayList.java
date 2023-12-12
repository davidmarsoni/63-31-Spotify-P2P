package Classes;
import utils.Utils;

public class PlayList extends Entry {
    private String PlayListName;
    private String PlayListPath;
    private MusicFile[] MusicFiles;

    public PlayList(String ClientAdress, int ClientPort, String PlayListName, String PlayListPath, MusicFile[] MusicFiles) {
        super(ClientAdress, ClientPort);
        this.PlayListName = PlayListName;
        this.PlayListPath = PlayListPath;
        this.MusicFiles = MusicFiles;
    }

    public String getPlayListName() {
        return this.PlayListName;
    }

    public void setPlayListName(String PlayListName) {
        this.PlayListName = PlayListName;
    }

    public String getPlayListPath() {
        return this.PlayListPath;
    }

    public void setPlayListPath(String PlayListPath) {
        this.PlayListPath = PlayListPath;
    }

    public MusicFile[] getMusicFiles() {
        return this.MusicFiles;
    }

    public void setMusicFiles(MusicFile[] MusicFiles) {
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
