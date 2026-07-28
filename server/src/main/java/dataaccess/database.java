package dataaccess;

import model.*;
import java.util.Collection;
import java.util.TreeMap;
import chess.ChessGame.TeamColor;
import service.ListEntry;
import java.util.ArrayList;

public class database implements dataModel {
    private TreeMap<String, userData> userDB;
    private TreeMap<String, authData> authDB;
    private TreeMap<Integer, gameData> gameDB;

    public database() {
        userDB = new TreeMap<>();
        authDB = new TreeMap<>();
        gameDB = new TreeMap<>();
    }

    public userData getUser(String username) {
        return userDB.get(username);
    }
    public void createUser(userData user) {
        userDB.put(user.username(), user);
    }
    public void addAuth(authData auth) {
        authDB.put(auth.authToken(), auth);
    }
    public void removeAuth(String authToken) {
        authDB.remove(authToken);
    }
    public boolean checkAuth(String authToken) {
        return authDB.containsKey(authToken);
    }
    public authData getAuth(String authToken) {
        return authDB.get(authToken);
    }
    public Collection<ListEntry> getList() {
        var list = new ArrayList<ListEntry>();
        for (var gameData : gameDB.values()) {
            list.add(new ListEntry(gameData));
        }
        return list;
    }
    public void addGame(gameData game) {
        gameDB.put(game.gameID(), game);
    }
    public gameData getGame(int gameID) {
        return gameDB.get(gameID);
    }
    public void updatePlayer(int gameID, TeamColor color, String username) {
        gameData game = gameDB.get(gameID);
        gameDB.remove(gameID);
        gameData updated;
        if (color == TeamColor.WHITE) {
            updated = new gameData(gameID,
                    username, game.blackUsername(),
                    game.gameName(), game.game());
        } else {
            updated = new gameData(gameID,
                    game.whiteUsername(), username,
                    game.gameName(), game.game());
        }
        gameDB.put(gameID, updated);
    }
    public boolean checkColor(int gameID, TeamColor color) {
        if (color == TeamColor.WHITE) {
            return gameDB.get(gameID).whiteUsername() == null;
        }
        return gameDB.get(gameID).blackUsername() == null;
    }

    public void clear() {
        authDB.clear();
        userDB.clear();
        gameDB.clear();
    }
}
