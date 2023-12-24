package utils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Classes.Entry;
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
   

    private static final String IP_ADDRESS_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}$";

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
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
        for (Entry entry2 : entries) {
            if (entry2.getName().equals(entry.getName()) && entry2.getPath().equals(entry.getPath())) {
                return;
            }
        }
        entries.add(entry);
    }

    public void removeSharedEntry(Entry entry) {
        for (Entry entry2 : entries) {
            if (entry2.getName().equals(entry.getName()) && entry2.getPath().equals(entry.getPath())) {
                entries.remove(entry2);
                return;
            }
        }
    }

    public void listSharedEntries() {
        StringBuilder sb = new StringBuilder();
        for (Object entry : entries) {
            sb.append(entry.toString()).append(" , ");
        }
        // Remove the last comma and space
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        System.out.println(sb.toString());
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
        for (Entry entry : entries) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }
        return null;
    }

    public Entry findEntryByNameAndClientAdressAndPort(String name, String clientAdress, int clientPort) {
        for (Entry entry : entries) {
            if (entry.getName().equals(name) && entry.getClientAdress().equals(clientAdress)
                    && entry.getClientPort() == clientPort) {
                return entry;
            }
        }
        return null;
    }
} 
