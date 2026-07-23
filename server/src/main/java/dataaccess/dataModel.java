package dataaccess;

import model.authData;
import model.gameData;
import model.userData;
import server.Server;

import java.util.Collection;

public interface dataModel {

    public userData getUser(String username);

    public void createUser(userData user);

    public void addAuth(authData auth);

    public void removeAuth(String authToken);

    public boolean checkAuth(String authToken);

    public authData getAuth(String authToken);

    public Collection<gameData> getList();

    public void addGame(gameData game);

    public gameData getGame(int gameID);

    public void updatePlayer(int gameID, Server.UserColor color, String username);

    public boolean checkColor(int gameID, Server.UserColor color);
}
