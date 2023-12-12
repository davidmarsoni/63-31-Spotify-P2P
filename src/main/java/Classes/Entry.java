package Classes;
import utils.Utils;

public abstract class Entry {
    private String ClientAdress;
    private int ClientPort;

    public Entry(String ClientAdress, int ClientPort) {
        this.ClientAdress = ClientAdress;
        this.ClientPort = ClientPort;
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
}
