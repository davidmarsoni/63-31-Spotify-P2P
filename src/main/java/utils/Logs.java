package utils;

import java.io.IOException;
import java.util.logging.*;

public class Logs {
    private static Logger logger = Logger.getLogger(Logs.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            // Get the current month
            String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date());
            // Create a new FileHandler that writes to a file named "log_<month>.txt"
            fileHandler = new FileHandler("log_" + month + ".txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing log file", e);
        }
    }

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void warning(String msg) {
        logger.log(Level.WARNING, msg);
    }

    public static void severe(String msg) {
        logger.log(Level.SEVERE, msg);
    }
}