package dataaccess;

import model.*;
import java.util.Collection;
import java.util.TreeMap;
import chess.ChessGame.TeamColor;
import service.ListEntry;
import java.util.ArrayList;

public class Database implements DataModel {
    private TreeMap<String, UserData> userDB;
    private TreeMap<String, AuthData> authDB;
    private TreeMap<Integer, GameData> gameDB;

    public Database() {
        userDB = new TreeMap<>();
        authDB = new TreeMap<>();
        gameDB = new TreeMap<>();
    }

    public UserData getUser(String username) {
        return userDB.get(username);
    }
    public void createUser(UserData user) {
        userDB.put(user.username(), user);
    }
    public void addAuth(AuthData auth) {
        authDB.put(auth.authToken(), auth);
    }
    public void removeAuth(String authToken) {
        authDB.remove(authToken);
    }
    public boolean checkAuth(String authToken) {
        return authDB.containsKey(authToken);
    }
    public AuthData getAuth(String authToken) {
        return authDB.get(authToken);
    }
    public Collection<ListEntry> getList() {
        var list = new ArrayList<ListEntry>();
        for (var gameData : gameDB.values()) {
            list.add(new ListEntry(gameData));
        }
        return list;
    }
    public void addGame(GameData game) {
        gameDB.put(game.gameID(), game);
    }
    public GameData getGame(int gameID) {
        return gameDB.get(gameID);
    }
    public void updatePlayer(int gameID, TeamColor color, String username) {
        GameData game = gameDB.get(gameID);
        gameDB.remove(gameID);
        GameData updated;
        if (color == TeamColor.WHITE) {
            updated = new GameData(gameID,
                    username, game.blackUsername(),
                    game.gameName(), game.game());
        } else {
            updated = new GameData(gameID,
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

    public boolean isEmpty() {
        return authDB.isEmpty() && userDB.isEmpty() && gameDB.isEmpty();
    }
}
