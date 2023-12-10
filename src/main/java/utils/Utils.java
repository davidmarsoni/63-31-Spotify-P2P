package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.io.IOException;

public class Utils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String[] COLORS = {ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE};
    public static final String START_MESSAGE_WELCOME = "Welcome on spotify p2p please write "+ANSI_GREEN +"help"+ANSI_RESET +" to see all the command \n";
    public static final String VERSION_MESSAGE = "V 0.0.1 \n\n";

    public static void renderStart(boolean isServer) {
        Random rand = new Random();
        String START_MESSAGE_LOGO = loadFile("src/main/resources/logo.txt");
        for (char c : START_MESSAGE_LOGO.toCharArray()) {
            // Generate a random color
             String color = COLORS[rand.nextInt(COLORS.length)];
            // Print the character with the color
            System.out.print(color + c + ANSI_RESET);
        }
        System.out.println();
        // System.out.print(ANSI_YELLOW + START_MESSAGE_LOGO + ANSI_RESET);
        if (isServer) {
            System.out.println(loadFile("src/main/resources/server.txt"));
        } else {
            System.out.println(loadFile("src/main/resources/client.txt"));
        }
         System.out.println();
        System.out.println(VERSION_MESSAGE+START_MESSAGE_WELCOME + ANSI_RESET);
    }
    
    public static String loadFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void p(String State,String s) {
        System.out.println(State + " > " + s);
    }
    public static void p(String s) {
        System.out.println(s);
    }
}
