package utils;

import java.net.InetAddress;

import java.net.Socket;

public class ThreadData {
    private Socket socket;
    private InetAddress address;
    private int port = 40000;

    public ThreadData() {
    }

    public ThreadData(Socket socket) {
        this.socket = socket;
    }

    public ThreadData(Socket socket, InetAddress clientAddress, int clientPort) {
        this.socket = socket;
        this.address = clientAddress;
        this.port = clientPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public void setClientAddress(String address) {
        try {
            this.address = InetAddress.getByName(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
