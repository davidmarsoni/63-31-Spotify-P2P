package CommandsClient;

import java.io.*;
import java.util.*;

import Classes.*;
import utils.*;

public class ShareUnShare implements Command {
    private StorageClient storage = StorageClient.getInstance();
    private String type = "";
    private String data = "";
    private boolean isFile = true;

    public ShareUnShare(String Type) {
        this.type = Type;
    }
    @Override
    public void execute(String argument) {
        Boolean error = false;
        if (storage.getClientSocket(true) == null || argument == null) {
            return;
        }

        Utils.title(type.equals("share") ? "Share file ..." : "Unshare file ...");

        String args[] = argument.split(" ");

        if (argument.contains("\"")) {
            error = handleQuotedArgument(argument, args);
        }

        if (error) {
            return;
        }

        if (isFile) {
            error = handleFile(args);
        } else {
           error = handleFolder(args);
        }

        if (error) {
            return;
        }

        sendCommandToServer();
    }

    private boolean handleQuotedArgument(String argument, String[] args) {
        int index = argument.indexOf("\"");
        int index2 = argument.indexOf("\"", index + 1);
        args[0] = argument.substring(index + 1, index2).trim();

        File file = new File(args[0]);
        isFile = file.isFile();
        if (!isFile) {
            try{
                args[1] = argument.substring(index2 + 1).trim();
            }catch(Exception e){
                System.out.println("You need to specify a folder and a name for the playlist see " + Utils.colorize("help share", Colors.YELLOW)
                + " for more details");
                return true;
            }
        }
        return false;
    }

    private boolean handleFile(String[] args) {
        File file = new File(args[0].trim());
        if (!file.exists() && file.isFile() && !file.getName().endsWith(".mp3")) {
            System.out.println("The file " + Utils.colorize(args[0], Colors.BLUE) +" doesn't exist or is not a mp3 file");
            return true;
        }

        MusicFile musicFile = new MusicFile(storage.getLocalAdressString(), storage.getPort(), file.getName(), file.getAbsolutePath());
        updateStorage(musicFile);

        data = "file#" + musicFile.getName() + "#" + musicFile.getPath();

        return false;
    }

    private boolean handleFolder(String[] args) {
        ArrayList<String> musicFiles = new ArrayList<>();
        File folder = new File(args[0].trim());
        File[] listOfFiles = folder.listFiles();
        if (!folder.exists() || listOfFiles.length == 0) {
            System.out.println("The folder " +Utils.colorize(args[0], Colors.BLUE)+ " doesn't exist or is empty");
            return true;
        }
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                MusicFile musicFile = new MusicFile(storage.getLocalAdressString(), storage.getPort(), file.getName(), file.getAbsolutePath());
                musicFiles.add(musicFile.getName());
            }
        }

        PlayList playList = new PlayList(storage.getLocalAdressString(), storage.getPort(), args[0].trim(), args[1].trim(), musicFiles);
        updateStorage(playList);

        data = "playlist#" + playList.getName() + "#" + playList.getPath();

        for (String musicFile : musicFiles) {
            data += "#" + musicFile;
        }

        return false;
    }

    private void updateStorage(Entry entry) {
        if (type.equals("share")) {
            storage.addSharedEntry(entry);
        } else if (type.equals("unshare")) {
            storage.removeSharedEntry(entry);
        }
    }

    private void sendCommandToServer() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(storage.getClientSocket().getInputStream()));
            PrintWriter out = new PrintWriter(storage.getClientSocket().getOutputStream(), true);

            out.println(type);
            out.println(data);

            String response;
            while (!(response = in.readLine()).equalsIgnoreCase("end")) {
                System.out.println(response);
            }

            Utils.title(type.equals("share") ? "Share Entry done" : "UnShare Entry done");
        } catch (Exception e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        throw new UnsupportedOperationException("This command is a superCommand and doesn't have a help method it just there to avoid code duplication");
    }
}