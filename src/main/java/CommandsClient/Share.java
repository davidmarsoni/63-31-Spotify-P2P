package CommandsClient;

import utils.*;
public class Share implements Command{
    @Override
    public void execute(String argument) {
        ShareUnShare share = new ShareUnShare("share");
        share.execute(argument);
    }

    @Override
    public String help() {
        String message = "share a file with other users \n"+
        "note : the file must be a ablsolute path \n"+
        "note 2 : if you have spaces in your path, you must put it between quotes\n"+
        "Usage: "+Utils.colorize("share ", Colors.YELLOW)+Utils.colorize("<file>",Colors.GREEN)+" to share a music file\n"+
        "       "+Utils.colorize("share ", Colors.YELLOW)+Utils.colorize("<folder> <name> ",Colors.GREEN)+"to share a playlist\n";
        return message;
    }
}
