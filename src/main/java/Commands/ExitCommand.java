package Commands;

import utils.Storage;

/**
 * Exit command
 * This command exit the program
 */
public class ExitCommand implements Command {
    private Storage storage = Storage.getInstance();

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
