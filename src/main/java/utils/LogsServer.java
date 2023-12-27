package utils;

import java.util.logging.Level;

public class LogsServer {
    static StorageServer storage = StorageServer.getInstance();
    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    public static void severe(String message) {
       log(Level.SEVERE, message);
    }

    public static void log(Level level, String message) {
        log(level, message,true);
    }

    public static void log(Level level, String message,boolean isPrefixed) {
        String log ; 
        if(isPrefixed){
            log = Utils.colorize("["+storage.getCurrentSocket().getInetAddress().getHostAddress()+":"+storage.getCurrentSocket().getPort()+"] ",Colors.BLUE)+message;       
        }else{
            log = message;
        }
        Logs.log(level, log);
    }
}
