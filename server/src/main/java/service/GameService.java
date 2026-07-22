package service;

import model.*;
import dataaccess.DataAccessException;

public class GameService {
    public ListResult listGames(ListRequest req) throws DataAccessException {
        if (!checkAuth(req.authToken())) {
            throw new DataAccessException("unauthorized access");
        }

        return new ListResult(getList());
    }
    public void makeGame(MakeGameRequest req) throws DataAccessException {
        if (!checkAuth(req.authToken())) {
            throw new DataAccessException("unauthorized access");
        }
        if (getGame(req.gameName()) != null) {
            throw new DataAccessException("game already exists");
        }
        addGame(new gameData());

    }
}
