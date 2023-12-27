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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Classes.PlayList;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Play implements Command {
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument) {
        String fileName = "";
        String ipAndPort = "";
        String data = "";
        // verifications
        if (storage.getClientSocket(true) == null) {
            return;
        }
        if (argument == null) {
            System.out.println("You need to specify a file name see " + Utils.colorize("help play", Colors.YELLOW)
                    + " for more details");
            return;
        }

        if (argument.contains("\"")) {
            fileName = argument.substring(argument.indexOf("\"") + 1, argument.lastIndexOf("\""));
            if (argument.substring(argument.lastIndexOf("\"") + 1).trim().length() > 0) {
                ipAndPort = argument.substring(argument.lastIndexOf("\"") + 1).trim();
            }
            data = fileName + "#" + ipAndPort;
        } else {
            String args[] = argument.split(" ");
            fileName = args[0];
            data = fileName;
            if (args.length > 1) {
                ipAndPort = args[1];
                data += "#" + ipAndPort;
            }

        }

        try {
            if (ipAndPort.length() > 0) {
                System.out.println("Try to get information about " + fileName + " on this host " + ipAndPort);
            } else {
                System.out.println("Try to get information about " + fileName + " from the server "
                        + storage.getServerAddress() + ":"
                        + storage.getServerPort());
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            // send the command to the server
            out.println("getInfo");
            out.println(data);
            // listen the response from the server
            String response = "";

            // wait for the end of the list
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                String reponseParts[] = response.split("#");
                if (reponseParts[0].equalsIgnoreCase("data")) {
                    System.out.println("The " + reponseParts[1] + " " + Utils.colorize(reponseParts[2], Colors.GREEN)
                            + " is available on the host " + reponseParts[4] + ":" + reponseParts[5]);
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
        try {
            Socket clientSocket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("stream");
            out.println(type + "#" + name + "#" + path);
            String response = "";
            while (!(response = in.readLine()).equalsIgnoreCase("start")) {
                System.out.println(response); // TODO Correc bug disconnecting
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

        } catch (

        IOException e) {
            e.printStackTrace();
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
                + Utils.colorize("192.169.1.10", Colors.DARK_PURPLE) + ":" + Utils.colorize("12345", Colors.DARK_PURPLE)
                + " to play the file test.mp3 from a specific person\n";
        return help;
    }

}
