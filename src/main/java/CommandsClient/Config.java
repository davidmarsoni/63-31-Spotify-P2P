package CommandsClient;

import utils.StorageClient;
import utils.Utils;

public class Config implements CommandClient {
    private StorageClient storage = StorageClient.getInstance();
    @Override
    public void execute(String argument) {
        Utils.titleDesc("Current config", "Current configuration of the client");
        Utils.p("Client : " + Utils.ANSI_BLUE+ storage.getClientAddress() + Utils.ANSI_RESET+":"+Utils.ANSI_BLUE+storage.getClientPort()+Utils.ANSI_RESET);
        Utils.p("Server : " + Utils.ANSI_BLUE+ storage.getServerAddress() + Utils.ANSI_RESET+":"+Utils.ANSI_BLUE+storage.getServerPort()+Utils.ANSI_RESET);
        //list all the entries shared by the client
        if(storage.getSharedEntries().isEmpty()) {
            Utils.p("No shared entries");
        }else {
            Utils.p("Shared entries : ");
            storage.listSharedEntries();
        }
    }

    @Override
    public String help() {
        return "Print the current configuration of the client";
    }
    
}
