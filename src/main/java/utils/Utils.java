package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;

public class Utils {
    public static final String START_MESSAGE_WELCOME = "Welcome on spotify p2p please write "+Utils.colorize("help",Colors.YELLOW) +" to see all the command \n";

    private static final Random rand = new Random();

    public static void renderStart(boolean isServer) {
        String START_MESSAGE_LOGO = loadFile("src/main/resources/logo.txt");
        StringBuilder coloredLogo = new StringBuilder();
        for (char c : START_MESSAGE_LOGO.toCharArray()) {
            // Generate a random color
            String color = Colors.COLORS[rand.nextInt(Colors.COLORS.length)];
            // Append the character with the color to the StringBuilder
            coloredLogo.append(color).append(c).append(Colors.RESET);
        }
        // Print the colored logo
        System.out.println(coloredLogo);
        
        if (isServer) {
            System.out.println(loadFile("src/main/resources/server.txt"));
        } else {
            System.out.println(loadFile("src/main/resources/client.txt"));
        }
        
        String version = loadFile("src/main/resources/version.txt");
        System.out.println();
        if(isServer){
            System.out.println(version + " \n" + Colors.RESET);
        }else{
            System.out.println(version + " \n\n" + START_MESSAGE_WELCOME + Colors.RESET);
        }
    }
    
    public static String loadFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
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
            System.out.print(question + " " + colorize("(" + tooltips + ")" ,Colors.RESET) + " : ");
            type = sc.nextLine().trim().toLowerCase();
            if (isDefault && type.equals("")) {
                type = regex2;
                valid = true;
            } 

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
}
