package utils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
import CommandsServer.SendListEntryCommand;

//TODO : do the storage in json for the server
public class StorageServer {
    private static StorageServer instance = null;
    private Socket srvSocket = null;
    private InetAddress localAddress = null;
    private ServerSocket mySkServer;
    private String interfaceName = "eth1";
    private int port = 45000;
    private LinkedList<Entry> entries = new LinkedList<Entry>();
    private Map<String,CommandServer> commands ;
    private InetAddress clientAddress;
    private int clientPort = 40000;
    private LinkedList<Client> clients = new LinkedList<Client>();

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

    public String getClientAddress() {
        return clientAddress.getHostAddress();
    }

    public void setClientAddress(String clientAddress) {
        try {
            this.clientAddress = InetAddress.getByName(clientAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
       
    }

    public void updateClientEntry(Boolean isAvailable) {
        // each entry that have the current client address and port will be updated to be alvailable
        for (Entry entry : entries) {
            if (entry.getClientAdress().equals(clientAddress.getHostAddress()) && entry.getClientPort() == clientPort) {
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
            }
        }
    }
    public LinkedList<Client> getClients() {
        return clients;
    }

}
