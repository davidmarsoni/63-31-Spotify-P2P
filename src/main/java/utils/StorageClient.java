package utils;

import java.io.*;

import java.net.Socket;

import Commands.*;

import java.util.HashMap;
import java.util.Map;

public class StorageClient {
    private static StorageClient instance;
    private String serverAddress = "127.0.0.1";
    private int serverPort = 45000;
    private String clientAddress ="127.0.0.1";
    private int clientPort = 40000;
    private Socket clientSocket;
    private Map<String, Command> commands;

    private StorageClient() {
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
            writer.println(serverAddress);
            writer.println(serverPort);
            writer.println(clientAddress);
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
                this.serverAddress = reader.readLine();
                this.serverPort = Integer.parseInt(reader.readLine());
                this.clientAddress = reader.readLine();
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
        commands.put("connect", new ConnectCommand(serverAddress, serverPort));
        commands.put("help", new HelpClientCommand());
        commands.put("exit", new ExitCommand());
        commands.put("init", new InitCommand());
        commands.put("listMusics", new ListMusicsCommand());
        commands.put("share", new ShareCommand());
    }

    public String getServerAddress() {
        return this.serverAddress;
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

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, Command> commands) {
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
            this.clientAddress = clientAddress;
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
            this.serverAddress = serverAddress;
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
        return this.clientAddress;
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

    


}