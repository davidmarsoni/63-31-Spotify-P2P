package Classes;

import java.net.InetAddress;

public class Client {
    private InetAddress ClientAdress;
    private int ClientPort;
    private Boolean isAvailable = true;	

    public Client() {
    }
    public Client(InetAddress ClientAdress, int ClientPort) {
        this.ClientAdress = ClientAdress;
        this.ClientPort = ClientPort;
    }

    public Client(String ClientAdress, int ClientPort) {
        try{
            this.ClientAdress = InetAddress.getByName(ClientAdress);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.ClientPort = ClientPort;
    }

    public InetAddress getClientAdress() {
        return this.ClientAdress;
    }

    public void setClientAdress(InetAddress ClientAdress) {
        this.ClientAdress = ClientAdress;
    }

    public int getClientPort() {
        return this.ClientPort;
    }

    public void setClientPort(int ClientPort) {
        this.ClientPort = ClientPort;
    }

    public Boolean isAvailable() {
        return this.isAvailable;
    }

    public void setAvailable(Boolean isAvalaible) {
        this.isAvailable = isAvalaible;
    }

    @Override
    public String toString() {
        return "Client [ClientAdress=" + ClientAdress + ", ClientPort=" + ClientPort + ", isAvailable=" + isAvailable
                + "]";
    }
}
