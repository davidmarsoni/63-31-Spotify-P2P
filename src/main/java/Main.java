public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify 'server' or 'client' as an argument.");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "server":
                Server.start();
                break;
            case "client":
                Client.start();
                break;
            default:
                System.out.println("Invalid argument. Please specify 'server' or 'client'.");
                break;
        }
    }
}