package utils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import Classes.Entry;
import Classes.MusicFile;

import CommandsServer.CommandServer;
import CommandsServer.HandleShareCommand;
import CommandsServer.HelpCommand;
import CommandsServer.SendListEntryCommand;

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
        commands.put("listMusics", new SendListEntryCommand());
        commands.put("share", new HandleShareCommand());

        entries.add(new MusicFile("sss", 123, "sss", "sss"));
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
       
    }
}
