import java.io.*;
import java.net.*;
import java.util.*;

import CommandsServer.CommandServer;
import utils.*;

public class Server {

    static Scanner sc = new Scanner(System.in);
    static StorageServer storage = StorageServer.getInstance();
    static Map<String, CommandServer> commands = storage.getCommands();

    public static void main(String[] args) {
        Utils.renderStart(true);
        init();
        loop();
    }

    public static void init() {
        InetAddress localAddress = null;
        try {
            NetworkInterface ni = NetworkInterface.getByName(storage.getInterfaceName());
            Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress ia = inetAddresses.nextElement();

                if (!ia.isLinkLocalAddress()) {
                    if (!ia.isLoopbackAddress()) {
                        System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
                        localAddress = ia;
                    }
                }
            }
            if (localAddress == null) {
                System.out.println("No non-local address found for interface " + storage.getInterfaceName());
            }

            // Warning: the backlog value (2nd parameter is handled by the implementation
            ServerSocket mySkServer = new ServerSocket(45000, 10, localAddress);
            storage.setMySkServer(mySkServer);
            // set 3min timeout
            mySkServer.setSoTimeout(180000);

            // print some server information
            Utils.title("Server Started", Utils.ANSI_GREEN_H);

            System.out.println("Default Timeout   : " + mySkServer.getSoTimeout());
            System.out.println("Used IpAddress    : " + mySkServer.getInetAddress().getHostAddress());
            System.out.println("Listening to Port : " + mySkServer.getLocalPort());
        } catch (Exception e) {
            System.err.println("Error initializing server");
            e.printStackTrace();
        }
    }

    public static void loop() {
        commands.get("help").execute(null, null, null);
        while (true) {
            Utils.title("Waiting for a client connection...", Utils.ANSI_BLUE_H);

            try {
                Socket srvSocket = storage.getMySkServer().accept();

                storage.setSrvSocket(srvSocket);
                System.out.println(
                        "Connection accepted from " + storage.getSrvSocket().getInetAddress().getHostAddress());
                new Thread(() -> {
                    // listen command from client
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(storage.getSrvSocket().getInputStream()));
                        PrintWriter out = new PrintWriter(storage.getSrvSocket().getOutputStream(), true);
                        while (true) {
                            System.out.println("Waiting for a command from client");
                            // get the command
                            String[] words = in.readLine().split(" ");
                            String command = words[0];
                            String argument = words.length > 1 ? words[1] : null;

                            // execute the command
                            // System.out.println("Command: " + command + " Argument: " + argument);
                            CommandServer cmd = commands.get(command);
                            if (cmd != null) {
                                cmd.execute(argument, in, out);
                            } else {
                                System.out.println(
                                        "Command not found " + Utils.ANSI_BLUE + command + Utils.ANSI_RESET);
                                // respond to the client
                                out.println("Command not found " + Utils.ANSI_BLUE + command + Utils.ANSI_RESET);
                            }
                            
                        }

                    } catch (Exception e) {
                        
                        Utils.title("Client :"+storage.getSrvSocket().getInetAddress().getHostAddress()+":"+storage.getSrvSocket().getPort()+" disconnected",Utils.ANSI_RED_H);
                    }

                }).start();

            } catch (Exception e) {
                System.err.println("No client connection durrint this wait time");
            }
        }
    }

    public static void chat(BufferedReader in, PrintWriter out) {
        try {
            String userInput;
            while (!(userInput = in.readLine()).equalsIgnoreCase("exit")) {
                System.out.println("Client: " + userInput);
                out.println(userInput);
            }
        } catch (IOException e) {
            System.err.println("I/O error in chat function");
            e.printStackTrace();
        }
    }

}
