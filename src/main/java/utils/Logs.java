package utils;

import java.util.logging.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {
    private static final Logger logger = Logger.getLogger(Logs.class.getName());
    private static FileHandler fh;

    static {
        try {
            // Ensure the logs directory exists
            Files.createDirectories(Paths.get("logs"));

            // Generate the log file name based on the current month
            SimpleDateFormat sdf = new SimpleDateFormat("MM");
            String logFileName = "logs/logs_" + sdf.format(new Date()) + ".txt";

            // This block configure the logger with handler and formatter
            fh = new FileHandler(logFileName, true);
            logger.addHandler(fh);

            // Add a console handler
            ConsoleHandler ch = new ConsoleHandler();
            logger.addHandler(ch);

            // Disable the parent handlers
            logger.setUseParentHandlers(false);

            SimpleFormatter fileFormatter = new SimpleFormatter() {
                private static final String format = "[%1$s] [%2$s] %3$s %n";
                private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            sdf.format(new Date(lr.getMillis())),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage().replaceAll("\u001B\\[[;\\d]*m", ""));
                }
            };

            SimpleFormatter consoleFormatter = new SimpleFormatter() {
                private static final String format = "[%1$s] [%2$s] %3$s %n";
                private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            sdf.format(new Date(lr.getMillis())),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage());
                }
            };

            fh.setFormatter(fileFormatter);
            ch.setFormatter(consoleFormatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    public static void warning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void severe(String message) {
        logger.log(Level.INFO, message);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}