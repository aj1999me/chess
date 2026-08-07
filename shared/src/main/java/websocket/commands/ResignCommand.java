package websocket.commands;

public class ResignCommand extends UserGameCommand {
    private final boolean white;

    public ResignCommand(CommandType commandType, String authToken, Integer gameID, boolean white) {
        super(commandType, authToken, gameID);
        this.white = white;
    }

    public Boolean white() {
        return white;
    }
}

