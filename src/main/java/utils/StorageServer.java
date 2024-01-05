package utils;

import java.net.InetAddress;
import java.util.*;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import Classes.Client;
import Classes.Entry;
import Classes.MusicFile;
import Classes.PlayList;
import Classes.ServerData;
import CommandsServer.ClientShareInfoCommand;
import CommandsServer.CommandServer;
import CommandsServer.EndCommand;
import CommandsServer.HandleGetInfoCommand;
import CommandsServer.HandleShareCommand;
import CommandsServer.HandleUnShareCommand;
import CommandsServer.HelpCommand;
import CommandsServer.PingCommand;
import CommandsServer.SendListEntryCommand;
import java.lang.reflect.Type;

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


     public void save() { 
        try {
            PrintWriter writer = new PrintWriter(getSavePath());
            Gson gson = new Gson();
            // create a object to store the data
            ServerData server = new ServerData();
            server.address = getLocalAdress();
            server.port = getPort();
            server.entries = getSharedEntries();
            server.clients = getClients();
            String json = gson.toJson(server);
            writer.println(json);
            System.out.println("The file is saved at : " + getSavePath());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
       
        File file = new File(getSavePath());
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String json = br.readLine();
                // create a gson object and handle abstract class for that there is a type field
                // in the json for the Entry object
                Gson gson = new GsonBuilder().registerTypeAdapter(Entry.class, new JsonDeserializer<Entry>() {
                    @Override
                    public Entry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        JsonObject jsonObject = json.getAsJsonObject();
                        String type = jsonObject.get("type").getAsString();
                        if (type.equals("musicFile")) {
                            return context.deserialize(jsonObject, MusicFile.class);
                        } else if (type.equals("playList")) {
                            return context.deserialize(jsonObject, PlayList.class);
                        }
                        return null;
                    }
                }).create();
                ServerData server = null;
                if (json != null && !json.isEmpty()) {
                    server = gson.fromJson(json, ServerData.class);
                    // rest of your code
                } else {
                    System.out.println("Invalid or empty JSON string.");
                }
                // set the data to the storage
                setLocalAdress(server.address);
                do {
                    //test the port
                    if(setPort(server.port)){
                        break;
                    }
                    server.port = Integer.parseInt(Utils.ask("Set the Listening port", String.valueOf(server.port), "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$", String.valueOf(server.port), true));

                } while (true);
                
                setSharedEntries(server.entries);
                setClients(server.clients);

                //update all the client entry to false because when the server is restarted all the client are disconnected
                getSharedEntries().forEach(entry -> entry.setAvailable(false));
                //same for the clients
                getClients().forEach(client -> client.setAvailable(false));

                br.close();

                // create a
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
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

    public void setClients(LinkedList<Client> clients) {
        clients.forEach(client -> addClient(client));
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
