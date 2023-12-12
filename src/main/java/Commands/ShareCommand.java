package Commands;

import utils.Utils;

public class ShareCommand implements Command{

    @Override
    public void execute(String argument) {
       
    }

    @Override
    public String help() {
        String message = "Share a file with other users \n"+
                "Usage: "+Utils.ANSI_YELLOW+"share "+Utils.ANSI_RESET+"<file> to share a music file\n"+
                "       "+Utils.ANSI_YELLOW+"share "+Utils.ANSI_RESET +"<folder> to share a playlist (you will need to specify a name later\n";
        return message;
    }
}
