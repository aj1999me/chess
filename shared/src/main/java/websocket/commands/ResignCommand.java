package websocket.commands;

public class ResignCommand extends UserGameCommand {
    private final boolean white;

    public ResignCommand(String authToken, Integer gameID, boolean white) {
        super(CommandType.RESIGN, authToken, gameID);
        this.white = white;
    }

    public Boolean white() {
        return white;
    }
}

