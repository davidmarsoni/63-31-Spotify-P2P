package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.io.IOException;
import java.util.*;

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

    public static final String ANSI_BLACK_H = "\u001B[40m";
    public static final String ANSI_RED_H = "\u001B[41m";
    public static final String ANSI_GREEN_H = "\u001B[42m";
    public static final String ANSI_YELLOW_H = "\u001B[43m";
    public static final String ANSI_BLUE_H = "\u001B[44m";
    public static final String ANSI_PURPLE_H = "\u001B[45m";
    public static final String ANSI_CYAN_H = "\u001B[46m";
    public static final String ANSI_WHITE_H = "\u001B[47m";

    public static final String[] COLORS = {ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE};
    public static final String START_MESSAGE_WELCOME = "Welcome on spotify p2p please write "+ANSI_YELLOW +"help"+ANSI_RESET +" to see all the command \n";

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
        if (isServer) {
            System.out.println(loadFile("src/main/resources/server.txt"));
        } else {
            System.out.println(loadFile("src/main/resources/client.txt"));
        }
         System.out.println();
        System.out.println(loadFile("src/main/resources/version.txt")+" \n\n"+START_MESSAGE_WELCOME + ANSI_RESET);
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

    public static void titleDesc(String title) {
        titleDesc(title,null);
    }

    public static void titleDesc(String title,String desc) {
        p("");
        p(ANSI_BLUE_H + " " + title + " " + ANSI_RESET);
        p("");

        if(desc != null){
            p(desc);
            p("");
        }
    }

    public static String ask(String question, String tooltips, String regex1, String regex2, boolean isDefault) {
        Scanner sc = new Scanner(System.in);
        String type = "";
        boolean valid = false;
        do {
            System.out.print(question + " " + ANSI_BLUE + "(" + tooltips + ")" + ANSI_RESET + " : ");
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
