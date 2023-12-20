package CommandsClient;

import utils.Colors;
import utils.Utils;

public class Download  implements CommandClient {

    @Override
    public void execute(String argument) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public String help() {
        String help = "Download Command\n";
        help += "This command is used to download a file from the server\n";
        help += "Usage   : " + Utils.colorize("download ", Colors.YELLOW) + Utils.colorize("<file name>", Colors.GREEN) + " to download a file from the server (the first file name like this will be Donwloading)\n";
        help += "          " + Utils.colorize("download ", Colors.YELLOW) + Utils.colorize("<file name> <ip and port of the peer>", Colors.GREEN) + " to download a file from a specific person\n";
        help += "Example : " + Utils.colorize("download ",Colors.YELLOW) + Utils.colorize("test.mp3", Colors.GREEN) + " to download the file test.mp3 from the server\n";
        return help;
    }
  
}
