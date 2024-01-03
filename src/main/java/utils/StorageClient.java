package utils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import Classes.*;
import CommandsClient.*;
import CommandsServer.StreamEntry;

public class StorageClient extends Storage {
    private static StorageClient instance;
    private String savePath = "storageClient.json";

    // client part
    private InetAddress serverAddress;
    private int serverPort = 45000;

    private StorageClient() {
        try {
            serverAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            setLocalAdress(InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setPort(40000);
        try {
            setLocalAdress(InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

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
    
    public static StorageClient getInstance() {
        if (instance == null) {
            instance = new StorageClient();
            instance.initCommands();
        }
        return instance;
    }

    public void save() { 
        try {
            PrintWriter writer = new PrintWriter(getSavePath());
            Gson gson = new Gson();
            // create a object to store the data
            ClientData client = new ClientData();
            client.clientAddress = getLocalAdress();
            client.listeningPort = getPort();
            client.serverAddress = serverAddress;
            client.serverPort = serverPort;
            client.entries = getSharedEntries();
            String json = gson.toJson(client);
            writer.println(json);
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
                ClientData client = null;
                if (json != null && !json.isEmpty()) {
                    client = gson.fromJson(json, ClientData.class);
                    // rest of your code
                } else {
                    System.out.println("Invalid or empty JSON string.");
                }
                // set the data to the storage
                setLocalAdress(client.clientAddress);
                do {
                    //test the port
                    if(setPort(client.listeningPort)){
                        break;
                    }
                    client.listeningPort = Integer.parseInt(Utils.ask("Set the Listening port", String.valueOf(client.listeningPort), "^([0-9]{1,4}|[0-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])$", String.valueOf(client.listeningPort), true));

                } while (true);
                
                serverAddress = client.serverAddress;
                serverPort = client.serverPort;
                setSharedEntries(client.entries);
                br.close();

                // create a
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    private void initCommands() {
        setCommands(new HashMap<>());
        getCommands().put("connect", new Connect());
        getCommands().put("help", new Help());
        getCommands().put("exit", new Exit());
        getCommands().put("init", new Init());
        getCommands().put("list", new List());
        getCommands().put("share", new Share());
        getCommands().put("unshare", new UnShare());
        getCommands().put("config", new Config());
        getCommands().put("disconnect", new Disconnect());
        getCommands().put("play", new Play());
        getCommands().put("ping", new Ping());

        setServerCommands(new HashMap<>());
        getServerCommands().put("stream", new StreamEntry());

    }

    public String getServerAddress() {
        return this.serverAddress.getHostAddress();
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public Socket getClientSocket(Boolean print) {
        if (getClientSocket() == null && print) {
            System.out.println("You are not connected to a server, please use the command " + Colors.YELLOW + "connect"
                    + Colors.RESET + " to connect to a server");
            return null;
        }
        return getClientSocket();
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

    public void closeClientSocket() {
        try {
            getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listSharedEntries() {
        if (getSharedEntries().size() == 0) {
            System.out.println("You don't have any shared entries");
        } else {
            System.out.println("You have " + getSharedEntries().size() + " shared entries");
            for (Entry entry : getSharedEntries()) {
                System.out.println(entry.toString());
            }
        }
    }   
}