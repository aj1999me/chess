package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;
import java.lang.Math;

public class GameService {
    private database db;

    public GameService(database db) {
        this.db = db;
    }

    public ListResult listGames() {
        return new ListResult(db.getList());
    }

    public MakeGameResult makeGame(MakeGameRequest req) throws BadRequestException, AlreadyTakenException {
        if (req.gameName() == null) {
            throw new BadRequestException("missing game name");
        }
        if (db.getGame(Math.abs(req.gameName().hashCode())) != null) {
            throw new AlreadyTakenException("game already exists");
        }
        int gameID = Math.abs(req.gameName().hashCode());
        db.addGame(new gameData(gameID, null, null, req.gameName(), new ChessGame()));
        return new MakeGameResult(gameID);
    }
    public void joinGame(JoinRequest req, String authToken) throws BadRequestException, DataAccessException, AlreadyTakenException {
        if (req.playerColor() == null || req.gameID() == null) {
            throw new BadRequestException("missing information");
        }
        if (db.getGame(req.gameID()) == null) {
            throw new DataAccessException("game does not exist");
        }
        if (!db.checkColor(req.gameID(), req.playerColor())) {
            throw new AlreadyTakenException("color already taken");
        }
        db.updatePlayer(req.gameID(), req.playerColor(), db.getAuth(authToken).username());
    }
}
