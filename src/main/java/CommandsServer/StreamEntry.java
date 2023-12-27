package CommandsServer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import Classes.Entry;
import Classes.PlayList;
import CommandsClient.Play;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;

import utils.Colors;
import utils.StorageClient;
import utils.Utils;

// TODO : do logs
public class StreamEntry implements CommandServer {
    private StorageClient storage = StorageClient.getInstance();

    @Override
    public void execute(String argument, BufferedReader in, PrintWriter out) {
        out.println("connected to the  " + Utils.colorize(storage.getServerAddress(), Colors.DARK_PURPLE) + ":"
                + Utils.colorize(String.valueOf(storage.getServerPort()), Colors.DARK_PURPLE));

        try {
            String[] data = in.readLine().split("#");
            String type = data[0];
            String name = data[1];

            Entry entry = storage.findEntryByName(name);
            if (entry == null) {
                out.println("The " + type + " " + Utils.colorize(name, Colors.GREEN)
                        + " doesn't exist in the list of music on this host");
                return;
            }
            out.println("start");

            if (type.equals("playlist")) {
                
                PlayList playlist = (PlayList) entry;
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(storage.getCurrentSocket().getOutputStream()));
                byte[] done = new byte[3];
                String str = "done"; // randomly anything
                done = str.getBytes();
                for (int i = 0; i < playlist.getMusicNames().size(); i++) {
                    FileInputStream fis = new FileInputStream(
                            playlist.getPath() + "/" + playlist.getMusicNames().get(i));
                    int n;
                    byte[] buf = new byte[4096];

                    while ((n = fis.read(buf)) != -1) {
                        dos.write(buf, 0, n);
                        dos.flush();
                    }
                    dos.write(done, 0, 3);
                    dos.flush();
                    fis.close();
                }
                dos.close();

            } else if (type.equals("file")) {
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(storage.getCurrentSocket().getOutputStream()));

                FileInputStream fis = new FileInputStream(entry.getPath());
                BufferedInputStream bis = new BufferedInputStream(fis);

                int bytesRead;
                byte[] buffer = new byte[4096];

                while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                    dos.flush();
                }

                out.println("end");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                storage.getCurrentSocket().close();
                storage.removeCurrentThreadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String help() {
        return "Stream a file or a playlist to a ohter user";
    }

}
