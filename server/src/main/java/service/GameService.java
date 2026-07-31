package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;
import java.lang.Math;

public class GameService {
    private DatabaseSQL db;

    public GameService(DatabaseSQL db) {
        this.db = db;
    }

    public ListResult listGames() throws DataAccessException {
        return new ListResult(db.getList());
    }

    public MakeGameResult makeGame(MakeGameRequest req) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (req.gameName() == null) {
            throw new BadRequestException("missing game name");
        }
        if (db.getGame(Math.abs(req.gameName().hashCode())) != null) {
            throw new AlreadyTakenException("game already exists");
        }
        int gameID = Math.abs(req.gameName().hashCode());
        db.addGame(new GameData(gameID, null, null, req.gameName(), new ChessGame()));
        return new MakeGameResult(gameID);
    }

    public void joinGame(JoinRequest req, String authToken) throws BadRequestException, DataAccessException, AlreadyTakenException {
        if (req.playerColor() == null || req.gameID() == null) {
            throw new BadRequestException("missing information");
        }
        db.updatePlayer(req.gameID(), req.playerColor(), db.getAuth(authToken).username());
    }
}
