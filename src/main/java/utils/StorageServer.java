package utils;

import java.net.InetAddress;
import java.util.*;

import Classes.Client;
import Classes.Entry;
import Classes.MusicFile;
import CommandsServer.ClientShareInfoCommand;
import CommandsServer.CommandServer;
import CommandsServer.EndCommand;
import CommandsServer.HandleGetInfoCommand;
import CommandsServer.HandleShareCommand;
import CommandsServer.HandleUnShareCommand;
import CommandsServer.HelpCommand;
import CommandsServer.PingCommand;
import CommandsServer.SendListEntryCommand;

public class StorageServer extends Storage{
    private static StorageServer instance = null;
    private LinkedList<Client> clients = new LinkedList<Client>();
  
    private StorageServer() {
        // private constructor to prevent instantiation
    }

    public static StorageServer getInstance() {
        if (instance == null) {
            instance = new StorageServer();
            instance.initCommands();
            instance.setPort(45000);
            try {
                instance.setLocalAdress(InetAddress.getByName("127.0.0.1"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private void initCommands(){
        setServerCommands(new HashMap<String, CommandServer>());
        getServerCommands().put("help", new HelpCommand());
        getServerCommands().put("list", new SendListEntryCommand());
        getServerCommands().put("share", new HandleShareCommand());
        getServerCommands().put("unshare", new HandleUnShareCommand());
        getServerCommands().put("sendInfo", new ClientShareInfoCommand());
        getServerCommands().put("end", new EndCommand());
        getServerCommands().put("ping", new PingCommand());
        getServerCommands().put("getInfo", new HandleGetInfoCommand());

        //TODO : Remove this
        getSharedEntries().add(new MusicFile("129.168.102.344", 64532, "Cave-v2.mp3", "Todo"));
    }

    public void updateClientEntry(Boolean isAvailable) {
        // each entry that have the current client address and port will be updated to be alvailable
        for (Entry entry : getSharedEntries()) {
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
    public void printLog(String message) {
        String log = "["+getCurrentSocket().getInetAddress().getHostAddress()+":"+getCurrentSocket().getPort()+"] "+message;
        Logs.info(log);
        System.out.println(log);
    }
}
