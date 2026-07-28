package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import chess.ChessGame.TeamColor;
import service.ListEntry;

import java.util.Collection;

public interface DataModel {

    public UserData getUser(String username);

    public void createUser(UserData user);

    public void addAuth(AuthData auth);

    public void removeAuth(String authToken);

    public boolean checkAuth(String authToken);

    public AuthData getAuth(String authToken);

    public Collection<ListEntry> getList();

    public void addGame(GameData game);

    public GameData getGame(int gameID);

    public void updatePlayer(int gameID, TeamColor color, String username);

    public boolean checkColor(int gameID, TeamColor color);
}
