package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private final boolean white;

    public ConnectCommand(String authToken, Integer gameID, boolean white) {
        super(CommandType.CONNECT, authToken, gameID);
        this.white = white;
    }

    public Boolean white() {
        return white;
    }
}
