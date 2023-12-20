package utils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import Classes.Client;
import Classes.Entry;
import Classes.MusicFile;
import CommandsServer.ClientShareInfoCommand;
import CommandsServer.CommandServer;
import CommandsServer.EndCommand;
import CommandsServer.HandleShareCommand;
import CommandsServer.HandleUnShareCommand;
import CommandsServer.HelpCommand;
import CommandsServer.PingCommand;
import CommandsServer.SendListEntryCommand;

public class StorageServer {
    private static StorageServer instance = null;
    private InetAddress localAddress = null;
    private String interfaceName = "eth1";
    private int port = 45000;
    private ServerSocket mySkServer;
   

    private LinkedList<Entry> entries = new LinkedList<Entry>();
    private LinkedList<Client> clients = new LinkedList<Client>();
    private Map<Integer,ServerThreadData> serverThreadDatas = new HashMap<>();
    private Map<String,CommandServer> commands ;


    private Socket srvSocket = null;

    private StorageServer() {
        // private constructor to prevent instantiation
    }

    public static StorageServer getInstance() {
        if (instance == null) {
            instance = new StorageServer();
            instance.initCommands();
        }
        return instance;
    }

    private void initCommands(){
        commands = new HashMap<>();
        commands.put("help", new HelpCommand());
        commands.put("list", new SendListEntryCommand());
        commands.put("share", new HandleShareCommand());
        commands.put("unshare", new HandleUnShareCommand());
        commands.put("sendInfo", new ClientShareInfoCommand());
        commands.put("end", new EndCommand());
        commands.put("ping", new PingCommand());

        //TODO : Remove this
        entries.add(new MusicFile("129.168.102.344", 64532, "Cave-v2.mp3", "Todo"));
    }

    public Socket getSrvSocket() {
        return srvSocket;
    }

    public void setSrvSocket(Socket srvSocket) {
        this.srvSocket = srvSocket;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public ServerSocket getMySkServer() {
        return mySkServer;
    }

    public void setMySkServer(ServerSocket mySkServer) {
        this.mySkServer = mySkServer;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public LinkedList<Entry> getEntries() {
        return entries;
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);
    }

    public void removeEntry(Entry entry) {
        this.entries.remove(entry);
    }

    public Map<String, CommandServer> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, CommandServer> commands) {
        this.commands = commands;
    }

    public void updateClientEntry(Boolean isAvailable) {
        // each entry that have the current client address and port will be updated to be alvailable
        for (Entry entry : entries) {
            if (entry.getClientAdress().equals(getCurrentClientAddress()) && entry.getClientPort() == getCurrentClientPort()) {
                entry.setAvailable(isAvailable);
            }
        }
    }

    public void addClient(Client client) {
        //only add the client if it's not already in the list
        for (Client client2 : clients) {
            if (client2.getClientAdress().equals(client.getClientAdress()) && client2.getClientPort() == client.getClientPort()) {
                return;
            }
        }
        clients.add(client);
    }

    public void removeClient(Client client) {
        for (Client client2 : clients) {
            if (client2.getClientAdress().equals(client.getClientAdress()) && client2.getClientPort() == client.getClientPort()) {
                clients.remove(client2);
            }
        }
    }

    public void updateClient(Client client) {
        for (Client client2 : clients) {
            if (client2.getClientAdress().equals(client.getClientAdress()) && client2.getClientPort() == client.getClientPort()) {
                client2.setAvailable(client.isAvailable());
                updateClientEntry(client.isAvailable());
                return;
            }
        }
        //if the client is not in the list we add it
        addClient(client);
    }
    public LinkedList<Client> getClients() {
        return clients;
    }

    public void addServerThreadData(int id, ServerThreadData serverThreadData) {
        serverThreadDatas.put(id, serverThreadData);
    }

    public void addServerThreadData(ServerThreadData serverThreadData) {
        addServerThreadData(Thread.currentThread().hashCode(), serverThreadData);
    }

    public void removeServerThreadData(int id) {
        serverThreadDatas.remove(id);
    }

    public Map<Integer, ServerThreadData> getServerThreadsDatas() {
        return serverThreadDatas;
    }

    public ServerThreadData getCurrentServerThreadsData() {
        return serverThreadDatas.get(Thread.currentThread().hashCode());
    }

    public String getCurrentClientAddress() {
        return getCurrentServerThreadsData().getClientAddress().getHostAddress();
    }

    public int getCurrentClientPort() {
        return getCurrentServerThreadsData().getClientPort();
    }

    public void setCurrentClientAddress(String clientAddress) {
        getCurrentServerThreadsData().setClientAddress(clientAddress);
    }

    public void setCurrentClientPort(int clientPort) {
        getCurrentServerThreadsData().setClientPort(clientPort);
    }

    public Socket getCurrentSocket() {
        return getCurrentServerThreadsData().getSocket();
    }


    public void printLog(String message) {
        //TODO : Add logs handling
        System.out.println("["+getCurrentSocket().getInetAddress().getHostAddress()+":"+getCurrentSocket().getPort()+"] "+message);
    }
}
