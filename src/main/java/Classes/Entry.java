package Classes;
/**
 * Entry is a abstract class that represent A Entry file accepted by the server
 * Entry can be a MusicFile or a Playlist
 */
public abstract class Entry {
    private String ClientAdress; // the adress of the client that share the file
    private int ClientPort; // the port of the client that share the file
    private String name; 
    private String path;
    private boolean isAvailable = true;

    public Entry() {
    }
    public Entry(String ClientAdress, int ClientPort,String name,String path) {
        this.ClientAdress = ClientAdress;
        this.ClientPort = ClientPort;
        this.name = name;
        this.path = path;
    }
    
    public String getClientAdress() {
        return this.ClientAdress;
    }

    public void setClientAdress(String ClientAdress) {
        this.ClientAdress = ClientAdress;
    }

    public int getClientPort() {
        return this.ClientPort;
    }

    public void setClientPort(int ClientPort) {
        this.ClientPort = ClientPort;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }
             
    public void setAvailable(boolean isAvalaible) {
        this.isAvailable = isAvalaible;
    }
}
