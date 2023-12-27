package Classes;
/**
 * Entry is a abstract class that represent a file or a playlist
 */
public abstract class Entry {
    private String ClientAdress; // the adress of the client that share the file
    private int ClientPort; // the port of the client that share the file
    private String name; // the name of the file or the playlist
    private String path; // the path of the file or the playlist
    private boolean isAvailable = true; // if the file or the playlist is available or not

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
