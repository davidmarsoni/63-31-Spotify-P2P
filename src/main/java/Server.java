import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import Classes.Entry;
import Classes.MusicFile;
import Classes.PlayList;
import CommandsClient.CommandClient;
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

    public static void init(){
        InetAddress localAddress = null;
        try {
            NetworkInterface ni = NetworkInterface.getByName(storage.getInterfaceName());
            Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress ia = inetAddresses.nextElement();

                if (!ia.isLinkLocalAddress()) {
                    if (!ia.isLoopbackAddress()) {
                        System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
                        localAddress = ia;
                    }
                }
            }
            if(localAddress == null) {
                System.out.println("No non-local address found for interface " + storage.getInterfaceName());
            }
            
            // Warning: the backlog value (2nd parameter is handled by the implementation
            ServerSocket mySkServer = new ServerSocket(45000, 10, localAddress);
            storage.setMySkServer(mySkServer);
            // set 3min timeout
            mySkServer.setSoTimeout(180000);

            // print some server information
            Utils.title("Server has Started",Utils.ANSI_WHITE+Utils.ANSI_GREEN_H);
            
            System.out.println("Default Timeout    : " + mySkServer.getSoTimeout());
            System.out.println("Used IpAddress     : " + mySkServer.getInetAddress());
            System.out.println("Listening to Port  : " + mySkServer.getLocalPort());
        } catch (Exception e) {
            System.err.println("Error initializing server");
            e.printStackTrace();
        }
    }

    public static void loop() {
        commands.get("help").execute(null,null,null);
        while (true) {
            //run help command for debug
            Utils.p(Utils.ANSI_BLUE_H+" Waiting for a client connection... "+Utils.ANSI_RESET);
            Utils.p("");
            try {
                Socket srvSocket = storage.getMySkServer().accept();
                storage.setSrvSocket(srvSocket);
                System.out.println("Connection accepted from " + srvSocket.getInetAddress().getHostAddress()+":"+srvSocket.getPort());
                System.out.println("");
                new Thread(() -> {
                    try {
                        //listen command from client
                        BufferedReader in = new BufferedReader(new InputStreamReader(srvSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(srvSocket.getOutputStream(), true);
    
                        //get the command
                        String[] words = in.readLine().split(" ");
                        String command = words[0];
                        String argument = words.length > 1 ? words[1] : null;
    
                        //execute the command
                        //System.out.println("Command: " + command + " Argument: " + argument);
                        CommandServer cmd = commands.get(command);
                        if (cmd != null) {
                            cmd.execute(argument,in,out);
                        } else {
                            System.out.println("Command not found "+Utils.ANSI_BLUE+ command+Utils.ANSI_RESET);
                            //respond to the client
                            out.println("Command not found "+Utils.ANSI_BLUE+ command+Utils.ANSI_RESET);
                        }
                    } catch (Exception e) {
                        System.err.println("Error handling client connection");
                        e.printStackTrace();
                    } finally {
                        try {
                            srvSocket.close();
                        } catch (IOException e) {
                            System.err.println("Error closing client socket");
                            e.printStackTrace();
                        }
                    }
                }).start();
    
            } catch (Exception e) {
                System.err.println("Error accepting client connection");
                e.printStackTrace();
            }
        }
    }

    private static void downloadFile(String argument, PrintWriter out) {
        //
    }

    private static void getListMusics(PrintWriter out) {
        Utils.p("Sending list of music to client:" + storage.getSrvSocket().getInetAddress()+":"+storage.getSrvSocket().getPort());
        //for each entry send the data to the client
        for (Entry entry : storage.getEntries()) {
            out.println(entry.toString());
        }
        //send the end of the list
        out.println("end");
        Utils.p("List of music sent");
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
