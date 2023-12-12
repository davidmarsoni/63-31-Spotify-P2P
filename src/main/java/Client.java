import java.net.*;
import java.util.*;
import Commands.*;
import utils.*;

public class Client {
    static Socket clientSocket;
    static InetAddress serverAddress;
    static Storage storage = Storage.getInstance();
    static Map<String, Command> commands = storage.getCommands();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Utils.renderStart(false);
        //load the data
        loop();
    }

    /**
     * Loop to get the user input
     */
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
                //System.out.println("Command: " + command + " Argument: " + argument);

                Command cmd = commands.get(command);
                if (cmd != null) {
                    cmd.execute(argument);
                } else {
                    System.out.println("Command not found "+Utils.ANSI_BLUE+ command+Utils.ANSI_RESET);
                }
            }
        }
    }
}
