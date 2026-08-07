package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private final boolean white;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, boolean white) {
        super(commandType, authToken, gameID);
        this.white = white;
    }

    public Boolean white() {
        return white;
    }
}
