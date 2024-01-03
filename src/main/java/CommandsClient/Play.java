package CommandsClient;

import utils.Colors;
import utils.StorageClient;
import utils.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Play implements Command {
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        if (storage.getClientSocket(true) == null || argument == null) {
            System.out.println("You need to specify a file name see " + Utils.colorize("help play", Colors.YELLOW)
                    + " for more details");
            return;
        }

        String name = null;
        String clientAddress = null;
        int clientPort = 0;
        String[] splitArgument = null;

        // Check if argument starts with a quote (single or double)
        if (argument.startsWith("\"") || argument.startsWith("'")) {
            // Find the closing quote
            int endQuoteIndex = argument.indexOf(argument.charAt(0), 1);
            if (endQuoteIndex != -1) {
                // Extract the name and remove the quotes
                name = argument.substring(1, endQuoteIndex);
                // Split the rest of the argument
                splitArgument = argument.substring(endQuoteIndex + 1).trim().split(" ");
            }
        } else {
            // If no quotes, split the argument as before
            splitArgument = argument.split(" ");
            name = splitArgument[0];
        }

        if (splitArgument != null && splitArgument.length == 2) {
            String[] addressPort = splitArgument[1].split(":");
            clientAddress = addressPort[0];
            clientPort = Integer.parseInt(addressPort[1]);
        }

        String data = name + (clientAddress != null ? "#" + clientAddress + ":" + clientPort : "");

        try {
            System.out.println("Try to get information about " + Utils.colorize(name, Colors.GREEN)
                    + (clientAddress != null ? " on this host " + Utils.colorize(clientAddress + ":" + clientPort, Colors.PURPLE) : " from the server "
                    + storage.getServerAddress() + ":"
                    + storage.getServerPort()));

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            // send the command to the server
            out.println("getInfo");
            out.println(data);

            // listen the response from the server
            String response;
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                String reponseParts[] = response.split("#");
                if (reponseParts[0].equalsIgnoreCase("data")) {
                    System.out.println("The " + reponseParts[1] + " " + Utils.colorize(reponseParts[2], Colors.GREEN)
                            + " is available on the host " + Utils.colorize(reponseParts[4], Colors.PURPLE) + ":" + Utils.colorize(reponseParts[5], Colors.PURPLE));
                    if (reponseParts[1].equalsIgnoreCase("file")) {
                        System.out.println("Trying to stream the file");
                    } else if (reponseParts[1].equalsIgnoreCase("playlist")) {
                        System.out.println("Trying to stream the playlist");
                    }
                    streamEntry(reponseParts[1], reponseParts[2], reponseParts[3], reponseParts[4],
                            Integer.parseInt(reponseParts[5]));
                } else {
                    System.out.println(response);
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        }

    }

    private void streamEntry(String type, String name, String path, String ip, int port) {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("stream");
            out.println(type + "#" + name + "#" + path);
            String response = "";
            while (!(response = in.readLine()).equalsIgnoreCase("start")) {
                System.out.println(response);
            }

            if (type.equalsIgnoreCase("file")) {
                System.out.println("Start streaming the file");
                InputStream is = clientSocket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                Player player;
                try {
                    player = new Player(bis);
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }

            } else if (type.equalsIgnoreCase("playlist")) {
                System.out.println("Start streaming the playlist");

                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

                // Create a ByteArrayOutputStream to hold all the musics data
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int read;

                while (true) {
                    read = dis.read(buffer);
                    if (read == -1) {
                        // end of the stream so we can break the loop
                        break;
                    }
                    baos.write(buffer, 0, read);
                    if (read < buffer.length) {
                        // If we read less than the buffer size, then we reached the end of the song
                        // so we can play the song
                        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                        BufferedInputStream bis = new BufferedInputStream(bais);

                        Player player;
                        try {
                            player = new Player(bis);
                            player.play();
                        } catch (JavaLayerException e) {
                            e.printStackTrace();
                        }
                        // Clear the ByteArrayOutputStream and continue to the next song
                        baos = new ByteArrayOutputStream();
                    }
                }
            }

        } catch ( IOException e) {
           
        }finally{
            try {
                clientSocket.close();
            } catch (IOException e) {
              
            }
        }
    }

    @Override
    public String help() {
        String help = "";
        help += "This command is used to play a file from the server\n";
        help += "Usage   : " + Utils.colorize("play ", Colors.YELLOW) + Utils.colorize("<file name>", Colors.GREEN)
                + " to play a file from the server (the first file name like this will be Donwloading)\n";
        help += "          " + Utils.colorize("play ", Colors.YELLOW)
                + Utils.colorize("<file name> <ip and port of the host>", Colors.GREEN)
                + " to play a file from a specific person\n";
        help += "Example : " + Utils.colorize("play ", Colors.YELLOW) + Utils.colorize("test.mp3", Colors.GREEN)
                + " to play the file test.mp3 from the server\n"
                + "          " + Utils.colorize("play ", Colors.YELLOW)
                + Utils.colorize("\"music for test.mp3\"", Colors.GREEN) + " "
                + Utils.colorize("192.169.1.10", Colors.PURPLE) + ":" + Utils.colorize("12345", Colors.PURPLE)
                + " to play the file test.mp3 from a specific person\n";
        return help;
    }

}
