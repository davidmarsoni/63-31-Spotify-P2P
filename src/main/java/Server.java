import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;

import utils.*;

public class Server {
    static Socket srvSocket = null;
    static InetAddress localAddress = null;
    static ServerSocket mySkServer;
    static String interfaceName = "eth1";
    static int port = 45000;
    static Scanner sc = new Scanner(System.in);
    static LinkedList<Entry> entries = new LinkedList<Entry>();


    public static void main(String[] args) {
        Utils.renderStart(true);
        entries.add(new MusicFile("123", 123, "music1", "path1"));
        entries.add(new MusicFile("123", 123, "music2", "path2"));
        entries.add(new MusicFile("123", 123, "music3", "path3"));
        PlayList playList = new PlayList("123", 123, "playList1", "path1", new MusicFile[]{new MusicFile("123", 123, "music1", "path1"), new MusicFile("123", 123, "music2", "path2"), new MusicFile("123", 123, "music3", "path3")});
        entries.add(playList);
        init();
        loop();
    }

    public static void init(){
        try {
            NetworkInterface ni = NetworkInterface.getByName(interfaceName);
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
                System.out.println("No non-local address found for interface " + interfaceName);
            }
            // Warning: the backlog value (2nd parameter is handled by the implementation
            mySkServer = new ServerSocket(45000, 10, localAddress);

            // set 3min timeout
            mySkServer.setSoTimeout(180000);

            // print some server information
            System.out.println("Server is started");
            System.out.println("==================================");
            System.out.println("Default Timeout :" + mySkServer.getSoTimeout());
            System.out.println("Used IpAddress :" + mySkServer.getInetAddress());
            System.out.println("Listening to Port :" + mySkServer.getLocalPort());
        } catch (Exception e) {

        }
    }

    public static void loop() {
        while (true) {
            try {
                srvSocket = mySkServer.accept();
                System.out.println("Connection accepted from " + srvSocket.getInetAddress());
                System.out.println("==================================");
    
                new Thread(() -> {
                    try {
                        //listen command from client
                        BufferedReader in = new BufferedReader(new InputStreamReader(srvSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(srvSocket.getOutputStream(), true);
    
                        //get the command
                        String[] words = in.readLine().split(" ");
                        String command = words[0];
                        String argument = words.length > 1 ? words[1] : null;
    
                        switch (command) {
                            case "chat":
                                chat(in, out);
                                break;
                            case "listMusics":
                                Utils.p("ListMusics");
                                getListMusics(out);
                                break;
                            case "download":
                                if (argument != null) {
                                    Utils.p("Download", argument);
                                    downloadFile(argument, out);
                                } else {
                                    out.println("Please specify a music name to download.");
                                }
                                break;
                            default:
                                //send a message to the client to say that the command is not found
                                out.println("Command not found [server side]");
                                break;
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
        Utils.p("Sending list of music to client:" + srvSocket.getInetAddress()+":"+srvSocket.getPort());
        //for each entry send the data to the client
        for (Entry entry : entries) {
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
