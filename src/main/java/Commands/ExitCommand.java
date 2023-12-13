package Commands;

import utils.StorageClient;

/**
 * Exit command
 * This command exit the program
 */
public class ExitCommand implements Command {
    private StorageClient storage = StorageClient.getInstance();

    public ExitCommand() {}
    @Override
    public void execute(String argument) {
        storage.save();
        System.exit(0);
    }
    @Override
    public String help() {
        return "Exit the program";
    }
}
