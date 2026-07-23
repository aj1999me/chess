package dataaccess;

import model.authData;
import model.gameData;
import model.userData;

import java.util.TreeMap;

public class database {
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

}
