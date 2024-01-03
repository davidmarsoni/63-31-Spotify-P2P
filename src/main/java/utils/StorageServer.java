package utils;

import java.net.InetAddress;
import java.util.*;

import Classes.Client;
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
    }

    public void updateClientEntry(Boolean isAvailable) {
        getSharedEntries().stream()
            .filter(entry -> entry.getClientAdress().equals(getCurrentClientAddress()) && entry.getClientPort() == getCurrentClientPort())
            .forEach(entry -> entry.setAvailable(isAvailable));
    }

    public void addClient(Client client) {
        boolean exists = clients.stream().anyMatch(client2 -> client2.getClientAdress().equals(client.getClientAdress()) && client2.getClientPort() == client.getClientPort());
        if (!exists) {
            clients.add(client);
        }
    }

    public void removeClient(Client client) {
        clients.removeIf(client2 -> client2.getClientAdress().equals(client.getClientAdress()) && client2.getClientPort() == client.getClientPort());
    }

    public void updateClient(Client client) {
        clients.stream()
            .filter(client2 -> client2.getClientAdress().equals(client.getClientAdress()) && client2.getClientPort() == client.getClientPort())
            .findFirst()
            .ifPresentOrElse(client2 -> {
                client2.setAvailable(client.isAvailable());
                updateClientEntry(client.isAvailable());
            }, () -> addClient(client));
    }
    public LinkedList<Client> getClients() {
        return clients;
    }
}
