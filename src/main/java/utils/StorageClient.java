package utils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import Classes.*;
import CommandsClient.*;
import CommandsClient.List;

public class StorageClient {
    private static StorageClient instance;
    private InetAddress serverAddress;
    private int serverPort = 45000;
    private InetAddress clientAddress;
    private int clientPort = 40000;
    private Socket clientSocket;
    private Map<String, CommandClient> commands;
    private Map<String, CommandListeningClient> listeningCommands;
    private LinkedList<Entry> entries = new LinkedList<Entry>();

    private StorageClient() {
        try {
            serverAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            clientAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static StorageClient getInstance() {
        if (instance == null) {
            instance = new StorageClient();
            instance.load();
        }
        return instance;
    }

    public void save() {
        try {
            PrintWriter writer = new PrintWriter("storageClient.json");
            Gson gson = new Gson();
            //create a object to store the data
            ClientData client = new ClientData();
            client.clientAddress = clientAddress;
            client.clientPort = clientPort;
            client.serverAddress = serverAddress;
            client.serverPort = serverPort;
            client.entries = entries;
            String json = gson.toJson(client);
            writer.println(json);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File("storageClient.json");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String json = br.readLine();
                //create a gson object and handle abstract class for that there is a type field in the json for the Entry object
                Gson gson = new GsonBuilder().registerTypeAdapter(Entry.class, new JsonDeserializer<Entry>() {
                    @Override
                    public Entry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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
                ClientData client = gson.fromJson(json, ClientData.class);
                //set the data to the storage
                clientAddress = client.clientAddress;
                clientPort = client.clientPort;
                serverAddress = client.serverAddress;
                serverPort = client.serverPort;
                entries = client.entries;
                br.close();

                // create a 
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
        initCommands();
    }

    private void initCommands() {
        commands = new HashMap<>();
        commands.put("connect", new Connect());
        commands.put("help", new Help());
        commands.put("exit", new Exit());
        commands.put("init", new Init());
        commands.put("list", new List());
        commands.put("share", new Share());
        commands.put("unshare", new UnShare());
        commands.put("config", new Config());
        commands.put("test", new Test());
        commands.put("disconnect", new Disconnect());


    }

    public String getServerAddress() {
        return this.serverAddress.getHostAddress();
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public Socket getClientSocket() {
        return getClientSocket(true);
    }

    public Socket getClientSocket(Boolean print) {
        if (clientSocket == null && print) {
            System.out.println("You are not connected to a server, please use the command " + Utils.ANSI_YELLOW + "connect"
                    + Utils.ANSI_RESET + " to connect to a server");
            return null;
        }
        return clientSocket;
    }

    public void setClientSocket(Socket socket) {
        clientSocket = socket;
    }

    public Map<String, CommandClient> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, CommandClient> commands) {
        this.commands = commands;
    }

    public Map<String, CommandListeningClient> getListeningCommands() {
        return listeningCommands;
    }

    public void setListeningCommands(Map<String, CommandListeningClient> listeningCommands) {
        this.listeningCommands = listeningCommands;
    }

    private static final String IP_ADDRESS_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}$";

    private boolean isValidIpAddress(String ipAddress) {
        return ipAddress.matches(IP_ADDRESS_REGEX);
    }

    private boolean isValidPort(int port) {
        return port > 0 && port < 65535;
    }

    public void setClientAddress(String clientAddress) {
        if (isValidIpAddress(clientAddress)) {
            try {
                this.clientAddress = InetAddress.getByName(clientAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid IP address");
        }
    }

    public void setClientPort(int clientPort) {
        if (isValidPort(clientPort)) {
            this.clientPort = clientPort;
        } else {
            System.out.println("Invalid port number");
        }
    }

    public void setServerAddress(String serverAddress) {
        if (isValidIpAddress(serverAddress)) {
            try {
                this.serverAddress = InetAddress.getByName(serverAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid IP address");
        }
    }

    public void setServerPort(int serverPort) {
        if (isValidPort(serverPort)) {
            this.serverPort = serverPort;
        } else {
            System.out.println("Invalid port number");
        }
    }

    public String getClientAddress() {
        return this.clientAddress.getHostAddress();
    }

    public int getClientPort() {
        return this.clientPort;
    }

    public void closeClientSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSharedEntry(Entry entry) {
       //test if the entry is already in the list
        for (Entry entry2 : entries) {
            if(entry2.getName().equals(entry.getName()) && entry2.getPath().equals(entry.getPath())){
                return;
            }
        }
        entries.add(entry);
    }

    public void removeSharedEntry(Entry entry) {
        for (Entry entry2 : entries) {
            if(entry2.getName().equals(entry.getName()) && entry2.getPath().equals(entry.getPath())){
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

    
}