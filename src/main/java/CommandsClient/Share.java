package CommandsClient;

import utils.*;
public class Share implements CommandClient{
    @Override
    public void execute(String argument) {
        ShareUnShare share = new ShareUnShare("share");
        share.execute(argument);
    }

    @Override
    public String help() {
        String message = "share a file with other users \n"+
        "note : the file must be a ablsolute path"+
        "Usage: "+Utils.colorize("share ", Utils.ANSI_YELLOW)+Utils.colorize("<file>",Utils.ANSI_GREEN)+" to share a music file\n"+
        "       "+Utils.colorize("share ", Utils.ANSI_YELLOW)+Utils.colorize("<folder> <name> ",Utils.ANSI_GREEN)+"to share a playlist\n";
        return message;
    }
}
