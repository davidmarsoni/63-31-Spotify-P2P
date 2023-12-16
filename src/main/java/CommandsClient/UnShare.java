package CommandsClient;

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
        "note : the file must be a ablsolute path"+
        "Usage: "+Utils.colorize("unshare ", Utils.ANSI_YELLOW)+Utils.colorize("<file>",Utils.ANSI_GREEN)+" to unshare a music file\n"+
        "       "+Utils.colorize("unshare ", Utils.ANSI_YELLOW)+Utils.colorize("<folder> <name> ",Utils.ANSI_GREEN)+"to unshare a playlist\n";
        return message;
    }
    
}
