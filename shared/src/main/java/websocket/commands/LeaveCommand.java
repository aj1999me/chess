package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    private final boolean white;

    public LeaveCommand(String authToken, Integer gameID, boolean white) {
        super(CommandType.LEAVE, authToken, gameID);
        this.white = white;
    }

    public Boolean white() {
        return white;
    }
}
