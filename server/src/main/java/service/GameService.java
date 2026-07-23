package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;

public class GameService {
    private database db;

    public GameService(database db) {
        this.db = db;
    }

    public ListResult listGames(ListRequest req) throws DataAccessException {
        if (!db.checkAuth(req.authToken())) {
            throw new DataAccessException("unauthorized access");
        }

        return new ListResult(db.getList());
    }
    public void makeGame(MakeGameRequest req) throws DataAccessException {
        if (!db.checkAuth(req.authToken())) {
            throw new DataAccessException("unauthorized access");
        }
        if (db.getGame(req.gameName().hashCode()) != null) {
            throw new DataAccessException("game already exists");
        }
        /*generate game ID*/
        db.addGame(new gameData(req.gameName().hashCode(), null, null, req.gameName(), new ChessGame()));

    }
    public void joinGame(JoinRequest req) throws DataAccessException {
        if (!db.checkAuth(req.authToken())) {
            throw new DataAccessException("unauthorized access");
        }
        if (db.getGame(req.gameID()) != null) {
            throw new DataAccessException("game already exists");
        }
        if (!db.checkColor(req.color())) {
            throw new DataAccessException("color already taken");
        }
        db.updatePlayer(req.gameID(), req.color(), db.getAuth(req.authToken()).username());
    }
}
