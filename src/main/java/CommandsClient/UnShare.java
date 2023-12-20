package CommandsClient;

import utils.Colors;
import utils.Utils;

public class UnShare implements CommandClient {
    public UnShare() {}

    @Override
    public void execute(String argument) {
        ShareUnShare unshare = new ShareUnShare("unshare");
        unshare.execute(argument);
    }

    @Override
    public String help() {
        String message = "Unshare a file with other users \n"+
        "note : the file must be a ablsolute path \n"+
        "Usage: "+Utils.colorize("unshare ", Colors.YELLOW)+Utils.colorize("<file>",Colors.GREEN)+" to unshare a music file\n"+
        "       "+Utils.colorize("unshare ", Colors.YELLOW)+Utils.colorize("<folder> <name> ",Colors.GREEN)+"to unshare a playlist\n";
        return message;
    }
    
}
