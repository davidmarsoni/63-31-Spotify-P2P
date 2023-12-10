
import java.io.*;

public class Storage {
    private static Storage instance;
    private String ServerAddress;
    private int ServerPort;

    private Storage() {
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
            instance.load();
        }
        return instance;
    }

    public void save() {
        try {
            PrintWriter writer = new PrintWriter("storage.txt");
            writer.println(ServerAddress);
            writer.println(ServerPort);
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
                this.ServerAddress = reader.readLine();
                this.ServerPort = Integer.parseInt(reader.readLine());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setServerAddress(String serverAddress) {
        this.ServerAddress = serverAddress;
    }

    public void setServerPort(int serverPort) {
        this.ServerPort = serverPort;
    }

    public String getServerAddress() {
        return this.ServerAddress;
    }

    public int getServerPort() {
        return this.ServerPort;
    }
}