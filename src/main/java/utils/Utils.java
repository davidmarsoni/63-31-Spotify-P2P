package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static final String START_MESSAGE_WELCOME = "Welcome on spotify p2p please write "+Utils.colorize("help",Colors.YELLOW) +" to see all the command \n";

    private static final Random rand = new Random();

    public static void renderStart(boolean isServer) {
        InputStream logoStream = Utils.class.getResourceAsStream("/logo.txt");
        String START_MESSAGE_LOGO = loadFile(logoStream);
        StringBuilder coloredLogo = new StringBuilder();
        for (char c : START_MESSAGE_LOGO.toCharArray()) {
            // Generate a random color
            String color = Colors.COLORS[rand.nextInt(Colors.COLORS.length)];
            // Append the character with the color to the StringBuilder
            coloredLogo.append(color).append(c).append(Colors.RESET);
        }
        // Print the colored logo
        System.out.println(coloredLogo);

        InputStream messageStream;
        if (isServer) {
            messageStream = Utils.class.getResourceAsStream("/server.txt");
        } else {
            messageStream = Utils.class.getResourceAsStream("/client.txt");
        }
        System.out.println(loadFile(messageStream));

        InputStream versionStream = Utils.class.getResourceAsStream("/version.txt");
        String version = loadFile(versionStream);
        System.out.println();
        if(isServer){
            System.out.println(version + " \n" + Colors.RESET);
        }else{
            System.out.println(version + " \n\n" + START_MESSAGE_WELCOME + Colors.RESET);
        }
    }

    public static String loadFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String colorize(String s,String color){
        return color+s+Colors.RESET;
    }


    public static void title(String title,String color) {
        System.out.println("");
        System.out.println(color + " " + title + " " + Colors.RESET);
        System.out.println("");
    }

    public static void title(String title) {
        title(title,Colors.BLUE_H);
    }

    public static void titleDesc(String title,String desc) {
        title(title);
        if(desc != null){
            System.out.println(desc);
            System.out.println("");
        }
    }

    public static String ask(String question, String tooltips, String regex1, String regex2, boolean isDefault) {
        Scanner sc = new Scanner(System.in);
        String type = "";
        boolean valid = false;
        do {
            System.out.print(question + " " + colorize("(" + tooltips + ")" ,Colors.BLUE) + " : ");
            type = sc.nextLine().trim().toLowerCase();
            if (isDefault && type.equals("")) {
                type = regex2;
                valid = true;
            } 
            type = type.replaceAll("\"", "");
            type = type.replaceAll("\'", "");  

            if (type.matches(regex1) || type.matches(regex2)) {
                valid = true;
            } else {
                System.out.println("Invalid type");
            }
        } while (!valid);
        
        return type;
    }

    public static String ask(String question, String tooltips, String regex1, String regex2) {
        return ask(question, tooltips, regex1, regex2, false);
    }

    public static String ask(String question, String tooltips, String regex) {
        return ask(question, tooltips, regex, "", false);
    }

    public static String ask(String question, String tooltips) {
        return ask(question, tooltips, ".*", "", false);
    }

    public static int getAvailablePort() {
        int port = 0;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }
    
}
