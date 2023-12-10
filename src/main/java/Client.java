import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import utils.Utils;
public class Client {
    static Socket clientSocket;
    static InetAddress serverAddress;
    static String serverName = "127.0.0.1";
    static int serverPort = 45000;

    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        Utils.renderStart(false);

        //load the data
        load();
        loop();
    }

    public static void loop(){
        String input = "";

        while (true) {
            System.out.print("> ");
            //wait for the user input and trim it
            if (sc.hasNextLine()) {
                input = sc.nextLine().trim();
                String[] words = input.split(" ", 2);
                String command = words[0];
                String argument = words.length > 1 ? words[1] : null;

                switch (command) {
                    case "help":
                        help();
                        break;
                    case "exit":
                        exit();
                        break;
                    case "init":
                        init();
                        break;
                    case "connect":
                        connect();
                        break;
                    case "disconnect":
                        disconnected();
                        break;
                    case "listMusics":
                        listMusics();
                        break;
                    case "chat":
                        chat();
                        System.out.println("Chat ended");
                        break;
                    case "download":
                        if (argument != null) {
                            Utils.p("Download", argument);
                        } else {
                            System.out.println("Please specify a music name to download.");
                        }
                        break;
                    default:
                        System.out.println("Command not found "+Utils.ANSI_BLUE+ command+Utils.ANSI_RESET);
                        break;
                }
            }
        }
    }

    private static void listMusics() {
        connect();
        System.out.println("Try to get the list of music from " + serverName + ":" + serverPort);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //send the command to the server
            out.println("ListMusics");
            Utils.p("List of available music :");
            //listen the response from the server
            String response = "";
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
            }
        }catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        } 
    }

            

    public static void help(){
        Utils.p("Help page  :");
        Utils.p("help       : show this message");
        Utils.p("exit       : exit the program");
        Utils.p("connect    : connect to the server");
        Utils.p("init       : init the server address and port");
        Utils.p("chat       : start a chat with the server");
    }

    public static void init(){
         // Ask for the server address and port and verify if it's valid
            boolean valid = false;
            String tempServerName = "";
            do{
                System.out.print("Server address "+Utils.ANSI_BLUE+"("+serverName+")"+Utils.ANSI_RESET+" : ");
                tempServerName = sc.nextLine().trim();
                //use a regex to check if the ip is valid
                if(tempServerName.equals("")){
                    tempServerName = serverName;
                }
                
                if (serverName.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}$")) {
                    valid = true;
                } else {
                    System.out.println("Invalid IP address");
                }
            } while (!valid);
            
            serverName = tempServerName;

            valid = false;
            int tempServerPort = 0;
            String input = "";
            
            do{
                System.out.print("Server port "+Utils.ANSI_BLUE+"("+serverPort+")"+Utils.ANSI_RESET+" : ");
                //if the port is empty, use the default port or if it equals 0 if the user press enter use default port
                input = sc.nextLine().trim();
                if(input.equals("")){
                    tempServerPort = serverPort;
                    valid = true;
                } else {
                    try {
                        tempServerPort = Integer.parseInt(input);
                        if (tempServerPort > 0 && tempServerPort < 65535) {
                            valid = true;
                        } else {
                            System.out.println("Invalid port number");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid port number");
                    }
                }
            } while (!valid);
    }


    public static void connect(){
        System.out.println("Try to establish a connection to " + serverName + ":" + serverPort);
        try {
            serverAddress = InetAddress.getByName(serverName);
            clientSocket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to " + serverAddress);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverName);
        } catch (ConnectException e) {
            System.err.println("Connection refused by server: " + serverName);
        } catch (IOException e) {
            System.err.println("I/O error for the connection to: " + serverName);
        }
    }

    public static void disconnected(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("I/O error for the connection to: " + serverName);
        }
    }

    public static void exit(){
        Utils.p("Exiting...");
        //save the data
        save();
        //close the socket

        System.exit(0);

    }

    public static void chat() {
        connect();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //send the command to the server
            out.println("chat");
            Utils.p("Welcome to the chat mode please write "+Utils.ANSI_GREEN +"/exit"+Utils.ANSI_RESET +" to exit");
            try {
                String userInput;
                while (!(userInput = sc.nextLine()).equalsIgnoreCase("/exit")) {
                    out.println(userInput);
                    System.out.println("Server: " + in.readLine());
                }
            } catch (IOException e) {
                System.err.println("I/O error in chat function");
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                System.err.println("No line found");
            }
            System.out.println("Closing connection");
            in.close();
            out.close();
            disconnected();
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.err.println("I/O error in chat function");
            e.printStackTrace();
        }
    }


    public static void save(){
       //save the data
        Storage storage = Storage.getInstance();
        storage.setServerAddress(serverName);
        storage.setServerPort(serverPort);
        storage.save();
    }

    public static void load(){
        //load the data
        Storage storage = Storage.getInstance();
        serverName = storage.getServerAddress();
        serverPort = storage.getServerPort();
    }
}
