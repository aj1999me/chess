package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    private final boolean white;

    public LeaveCommand(CommandType commandType, String authToken, Integer gameID, boolean white) {
        super(commandType, authToken, gameID);
        this.white = white;
    }

    public Boolean white() {
        return white;
    }
}
