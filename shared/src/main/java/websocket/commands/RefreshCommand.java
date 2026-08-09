package websocket.commands;

public class RefreshCommand extends UserGameCommand {
    public RefreshCommand(String authToken, Integer gameID) {
        super(CommandType.REFRESH, authToken, gameID);
    }
}
