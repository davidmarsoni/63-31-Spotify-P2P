package utils;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Classes.Entry;
import CommandsClient.*;

public class StorageClient {
    private static StorageClient instance;
    private InetAddress serverAddress;
    private int serverPort = 45000;
    private InetAddress clientAddress;
    private int clientPort = 40000;
    private Socket clientSocket;
    private Map<String, CommandClient> commands;
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
            PrintWriter writer = new PrintWriter("storage.txt");
            writer.println(serverAddress.getHostAddress());
            writer.println(serverPort);
            writer.println(clientAddress.getHostAddress());
            writer.println(clientPort);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File("storage.txt");
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try {
                    this.serverAddress = InetAddress.getByName(reader.readLine());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                this.serverPort = Integer.parseInt(reader.readLine());
                try {
                    this.clientAddress = InetAddress.getByName(reader.readLine());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                this.clientPort = Integer.parseInt(reader.readLine());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
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
        commands.put("listMusics", new ListMusics());
        commands.put("share", new Share());
        commands.put("config", new Config());
        commands.put("test", new Test());
    }

    public String getServerAddress() {
        return this.serverAddress.getHostAddress();
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public Socket getClientSocket() {
        if(clientSocket == null){
            Utils.p("You are not connected to a server, please use the command "+Utils.ANSI_YELLOW+"connect"+Utils.ANSI_RESET+" to connect to a server");
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
        if(isValidPort(clientPort)){
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
        if(isValidPort(serverPort)){
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

    public void addSharedEntry(Entry entry){
        entries.add(entry);
    }

    public void removeSharedEntry(Entry entry){
        entries.remove(entry);
    }

    public void listSharedEntries(){
        entries.forEach((entry) -> {
            Utils.p(entry.toString());
        });
    }
    public LinkedList<Entry> getSharedEntries() {
        return entries;
    }

}