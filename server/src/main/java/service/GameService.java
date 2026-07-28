package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;

public class GameService {
    private database db;

    public GameService(database db) {
        this.db = db;
    }

    public ListResult listGames() {
        return new ListResult(db.getList());
    }
    public MakeGameResult makeGame(MakeGameRequest req) throws AlreadyTakenException {
        if (db.getGame(req.gameName().hashCode()) != null) {
            throw new AlreadyTakenException("game already exists");
        }
        int gameID = req.gameName().hashCode();
        db.addGame(new gameData(gameID, null, null, req.gameName(), new ChessGame()));
        return new MakeGameResult(gameID);
    }
    public void joinGame(JoinRequest req, String authToken) throws DataAccessException, AlreadyTakenException {
        if (db.getGame(req.gameID()) == null) {
            throw new DataAccessException("game does not exist");
        }
        if (!db.checkColor(req.gameID(), req.color())) {
            throw new AlreadyTakenException("color already taken");
        }
        db.updatePlayer(req.gameID(), req.color(), db.getAuth(authToken).username());
    }
}
