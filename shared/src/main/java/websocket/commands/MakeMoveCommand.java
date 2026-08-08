package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final Boolean white;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, Boolean white) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.white = white;
    }

    public ChessMove move() {
        return move;
    }

    public Boolean white() {
        return white;
    }
}
