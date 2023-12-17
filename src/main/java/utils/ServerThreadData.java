package utils;

import java.net.InetAddress;

import java.net.Socket;

public class ServerThreadData {
    private Socket socket;
    private InetAddress clientAddress;
    private int clientPort = 40000;

    public ServerThreadData() {
    }

    public ServerThreadData(Socket socket) {
        this.socket = socket;
    }

    public ServerThreadData(Socket socket, InetAddress clientAddress, int clientPort) {
        this.socket = socket;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        try {
            this.clientAddress = InetAddress.getByName(clientAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int port) {
        this.clientPort = port;
    }

}
