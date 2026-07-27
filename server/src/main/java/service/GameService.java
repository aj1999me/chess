package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;

public class GameService {
    private database db;

    public GameService(database db) {
        this.db = db;
    }

    public ListResult listGames(ListRequest req) throws UnauthorizedAccessException {
        if (!db.checkAuth(req.authToken())) {
            throw new UnauthorizedAccessException("unauthorized access");
        }

        return new ListResult(db.getList());
    }
    public MakeGameResult makeGame(MakeGameRequest req) throws UnauthorizedAccessException, AlreadyTakenException {
        if (!db.checkAuth(req.authToken())) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
        if (db.getGame(req.gameName().hashCode()) != null) {
            throw new AlreadyTakenException("game already exists");
        }
        int gameID = req.gameName().hashCode();
        db.addGame(new gameData(gameID, null, null, req.gameName(), new ChessGame()));
        return new MakeGameResult(gameID);
    }
    public void joinGame(JoinRequest req) throws UnauthorizedAccessException, DataAccessException, AlreadyTakenException {
        if (!db.checkAuth(req.authToken())) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
        if (db.getGame(req.gameID()) == null) {
            throw new DataAccessException("game does not exist");
        }
        if (!db.checkColor(req.gameID(), req.color())) {
            throw new AlreadyTakenException("color already taken");
        }
        db.updatePlayer(req.gameID(), req.color(), db.getAuth(req.authToken()).username());
    }
}
