package Commands;

import java.util.Scanner;

import utils.Storage;
import utils.Utils;

public class InitCommand implements Command {
    private Storage storage = Storage.getInstance();
    private Scanner sc = new Scanner(System.in);

    @Override
    public void execute(String argument) {
        Utils.titleDesc("Init", "Configure the server address and port");
        // Ask for the server address and port and verify if it's valid
        boolean valid = false;
        String tempServerName = "";
        String serverName = storage.getServerAddress();
        int serverPort = storage.getServerPort();

        do {
            System.out.print("Server address " + Utils.ANSI_BLUE + "(" + storage.getServerAddress() + ")"
                    + Utils.ANSI_RESET + " : ");
            tempServerName = sc.nextLine().trim();
            // use a regex to check if the ip is valid
            if (tempServerName.equals("")) {
                tempServerName = serverName;
            }

            if (tempServerName.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}$")) {
                valid = true;
            } else {
                System.out.println("Invalid IP address");
            }
        } while (!valid);

        serverName = tempServerName;

        valid = false;
        int tempServerPort = 0;
        String input = "";

        do {
            System.out.print("Server port " + Utils.ANSI_BLUE + "(" + serverPort + ")" + Utils.ANSI_RESET + " : ");
            // if the port is empty, use the default port or if it equals 0 if the user
            // press enter use default port
            input = sc.nextLine().trim();
            if (input.equals("")) {
                tempServerPort = serverPort;
                valid = true;
            } else {
                try {
                    tempServerPort = Integer.parseInt(input);
                    if (tempServerPort > 0 && tempServerPort < 65535) {
                        valid = true;
                    } else {
                        System.out.println("Invalid port number");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid port number");
                }
            }
        } while (!valid);

        storage.setServerAddress(serverName);
        storage.setServerPort(tempServerPort);
    }

    @Override
    public String help() {
        return "Allow you to configure the server address and port";
    }

}
