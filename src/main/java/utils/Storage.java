package utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Classes.Entry;
import Classes.ThreadData;
import CommandsClient.Command;
import CommandsServer.CommandServer;

public class Storage {
    private ServerSocket srvSocket;
    private Socket clientSocket;
    private InetAddress localAdress;
    private int port;
    private String interfaceName = "eth1";
    private Map<Integer, ThreadData> threadDatas = new HashMap<>();
    private Map<String, Command> commands;
    private Map<String, CommandServer> serverCommands;
    private LinkedList<Entry> entries = new LinkedList<Entry>();
    private String savePath = "storageServer.json";


    private static final String IP_ADDRESS_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}$";


    public String getSavePath() {
        return this.savePath;
    }

    public boolean setSavePath(String savePath) {
        if(savePath.endsWith(".json")){
            this.savePath = savePath;
            return true;
        }else{
            System.out.println("Invalid file format, the file must be a json file");
            return false;
        }
        
    }

    public int getPort() {
        return this.port;
    }

    public boolean setPort(int port) {
        if (!isValidPort(port)) {
            System.out.println("Invalid port, listeming port for the client must be between 0 and 65535");
            return false;
        }
        if(isPortUsed(port)){
            System.out.println("This port is already used");
            return false;
        }

        this.port = port;
        return true;
    }

    private boolean isPortUsed(int port) {
        boolean isUsed = false;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // If the ServerSocket is successfully created, then the port is not in use
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // If an exception is thrown, then the port is in use
            isUsed = true;
        }
        return isUsed;
    }

    public InetAddress getLocalAdress() {
        return this.localAdress;
    }

    public String getLocalAdressString() {
        return this.localAdress.getHostAddress();
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setLocalAdress(InetAddress localAdress) {
        this.localAdress = localAdress;
    }

    public boolean isValidIpAddress(String ipAddress) {
        return ipAddress.matches(IP_ADDRESS_REGEX);
    }

    public boolean isValidPort(int port) {
        return port > 0 && port < 65535;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    public Map<String, CommandServer> getServerCommands() {
        return serverCommands;
    }

    public void setServerCommands(Map<String, CommandServer> listeningCommands) {
        this.serverCommands = listeningCommands;
    }

    public void addSharedEntry(Entry entry) {
        // test if the entry is already in the list
        boolean exists = entries.stream().anyMatch(e -> e.getName().equals(entry.getName()) && e.getPath().equals(entry.getPath()));
        if (!exists) {
            entries.add(entry);
        }
    }

    public void removeSharedEntry(Entry entry) {
        entries.removeIf(e -> e.getName().equals(entry.getName()) && e.getPath().equals(entry.getPath()));
    }

    
    public LinkedList<Entry> getSharedEntries() {
        return entries;
    }

    public void setSharedEntries(LinkedList<Entry> entries) {
        this.entries = entries;
    }

    public ServerSocket getSrvSocket() {
        return this.srvSocket;
    }

    public void setSrvSocket(ServerSocket srvSocket) {
        this.srvSocket = srvSocket;
    }

    public String getInterfaceName() {
        return this.interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    // server part
    public void addThreadData(int id, ThreadData threadData) {
        threadDatas.put(id, threadData);
    }

    public void addThreadData(ThreadData threadData) {
        addThreadData(Thread.currentThread().hashCode(), threadData);
    }

    public void removeThreadData(int id) {
        threadDatas.remove(id);
    }

    public Map<Integer, ThreadData> getThreadsDatas() {
        return threadDatas;
    }

    public ThreadData getCurrentThreadsData() {
        return threadDatas.get(Thread.currentThread().hashCode());
    }

    public String getCurrentClientAddress() {
        return getCurrentThreadsData().getAddress().getHostAddress();
    }

    public int getCurrentClientPort() {
        return getCurrentThreadsData().getPort();
    }

    public void setCurrentClientAddress(String clientAddress) {
        getCurrentThreadsData().setClientAddress(clientAddress);
    }

    public void setCurrentClientPort(int clientPort) {
        getCurrentThreadsData().setPort(clientPort);
    }

    public Socket getCurrentSocket() {
        return getCurrentThreadsData().getSocket();
    }

    public void removeCurrentThreadData() {
        removeThreadData(Thread.currentThread().hashCode());
    }

    public Entry findEntryByName(String name) {
        return entries.stream()
                      .filter(entry -> entry.getName().equals(name) && entry.isAvailable())
                      .findFirst()
                      .orElse(null);
    }

    public Entry findEntryByNameAndClientAdressAndPort(String name, String clientAdress, int clientPort) {
        return entries.stream()
                      .filter(entry -> entry.getName().equals(name) && entry.getClientAdress().equals(clientAdress) && entry.getClientPort() == clientPort && entry.isAvailable())
                      .findFirst()
                      .orElse(null);
    }
} 
