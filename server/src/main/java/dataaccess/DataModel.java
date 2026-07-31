package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import chess.ChessGame.TeamColor;
import service.ListEntry;

import java.util.Collection;

public interface DataModel {

    public UserData getUser(String username) throws DataAccessException;

    public void createUser(UserData user) throws DataAccessException;

    public void addAuth(AuthData auth) throws DataAccessException;

    public void removeAuth(String authToken) throws DataAccessException;

    public boolean checkAuth(String authToken) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public Collection<ListEntry> getList();

    public void addGame(GameData game);

    public GameData getGame(int gameID);

    public void updatePlayer(int gameID, TeamColor color, String username);

    public boolean checkColor(int gameID, TeamColor color) throws DataAccessException;

    public void clear() throws DataAccessException;
}
