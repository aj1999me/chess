package dataaccess;

import model.authData;
import model.gameData;
import model.userData;
import java.util.Collection;
import java.util.TreeMap;
import server.Server.UserColor;

public class database implements dataModel {
    private TreeMap<String, userData> userDB;
    private TreeMap<String/*authToken*/, authData> authDB;
    private TreeMap<Integer/*gameID*/, gameData> gameDB;

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
    public Collection<gameData> getList() {
        return gameDB.values();
    }
    public void addGame(gameData game) {
        gameDB.put(game.gameID(), game);
    }
    public gameData getGame(int gameID) {
        return gameDB.get(gameID);
    }
    public void updatePlayer(int gameID, UserColor color, String username) {
        gameData game = gameDB.get(gameID);
        gameDB.remove(gameID);
        gameData updated;
        if (color == UserColor.WHITE) {
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
    public boolean checkColor(int gameID, UserColor color) {
        if (color == UserColor.WHITE) {
            return gameDB.get(gameID).whiteUsername() == null;
        }
        return gameDB.get(gameID).blackUsername() == null;
    }
}
