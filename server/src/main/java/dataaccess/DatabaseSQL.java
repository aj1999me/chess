package dataaccess;
import chess.ChessGame;
import model.*;
import service.ListEntry;
import static dataaccess.DatabaseManager.*;
import com.google.gson.Gson;
import java.util.ArrayList;

import java.sql.*;
import java.util.Collection;

public class DatabaseSQL implements DataModel {
    public DatabaseSQL() throws DataAccessException {
        try (var conn = getConnection()) {
            var prep = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS db");
            prep.executeUpdate();

            conn.setCatalog("db");

            var createUserDB = """
            CREATE TABLE  IF NOT EXISTS userDB (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";


            try (var createTableStatement = conn.prepareStatement(createUserDB)) {
                createTableStatement.executeUpdate();
            }

            var createAuthDB = """
            CREATE TABLE  IF NOT EXISTS userDB (
                id INT NOT NULL AUTO_INCREMENT,
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";


            try (var createTableStatement = conn.prepareStatement(createAuthDB)) {
                createTableStatement.executeUpdate();
            }

            var createGameDB = """
            CREATE TABLE  IF NOT EXISTS userDB (
                id INT NOT NULL AUTO_INCREMENT,
                gameID INT NOT NULL,
                whiteUsername VARCHAR(255) DEFAULT NULL,
                blackUsername VARCHAR(255) DEFAULT NULL,
                gameName VARCHAR(255) NOT NULL,
                game longtext NOT NULL,
                PRIMARY KEY (id)
            )""";


            try (var createTableStatement = conn.prepareStatement(createGameDB)) {
                createTableStatement.executeUpdate();
            }
        } catch(SQLException e) {
            throw new DataAccessException("SQL access failed");
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try(var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM userDB WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    var name = rs.getString("username");
                    var hashedPass = rs.getString("password");
                    var email = rs.getString("email");

                    return new UserData(name, hashedPass, email);
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData user) throws DataAccessException {
        try(var conn = getConnection()) {
            try(var preppedStatement = conn.prepareStatement("INSERT INTO userDB (username, password, email) VALUES(?,?,?)")) {
                preppedStatement.setString(1, user.username());
                preppedStatement.setString(2, user.password()); // add hashing functionality
                preppedStatement.setString(3, user.email());
                preppedStatement.executeUpdate();
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void addAuth(AuthData auth) throws DataAccessException {
        try(var conn = getConnection()) {
            try(var preppedStatement = conn.prepareStatement("INSERT INTO userDB (authToken, username) VALUES(?,?,?)")) {
                preppedStatement.setString(1, auth.authToken());
                preppedStatement.setString(2, auth.username()); // add hashing functionality
                preppedStatement.executeUpdate();
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void removeAuth(String authToken) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var prep = conn.prepareStatement("DELETE FROM authDB WHERE authToken=?")) {
                prep.setString(1, authToken);
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean checkAuth(String authToken) throws DataAccessException {
        try(var conn = getConnection()) {
            try(var prep = conn.prepareStatement("SELECT authToken FROM authDB WHERE authToken=?")) {
                prep.setString(1, authToken);
                var rs = prep.executeQuery();
                return rs.next();
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try(var conn = getConnection()) {
            try (var prep = conn.prepareStatement("SELECT authToken, username FROM authDB WHERE authToken=?")) {
                prep.setString(1, authToken);
                var rs = prep.executeQuery();
                if (rs.next()) {
                    var token = rs.getString("authToken");
                    var user = rs.getString("username");
                    return new AuthData(token, user);
                }
                return null;
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<ListEntry> getList() throws DataAccessException {
        var list = new ArrayList<ListEntry>();
        try(var conn = getConnection()) {
            try(var prep = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM gameDB")) {
                var rs = prep.executeQuery();
                while(rs.next()) {
                    var id = rs.getInt("gameID");
                    var white = rs.getString("whiteUsername");
                    var black = rs.getString("blackUsername");
                    var name = rs.getString("gameName");
                    list.add(new ListEntry(id, white, black, name));
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return list;
    }

    public void addGame(GameData game) throws DataAccessException {
        try(var conn = getConnection()) {
            try(var prep = conn.prepareStatement("INSERT INTO gameDB (gameID, gameName, game) VALUES(?,?,?)")) {
                prep.setInt(1, game.gameID());
                prep.setString(2, game.gameName());
                var json = new Gson().toJson(game.game());
                prep.setString(3, json);
                prep.executeUpdate();
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try(var conn = getConnection()) {
            try(var prep = conn.prepareStatement("SELECT FROM gameDB WHERE gameID=?")) {
                prep.setInt(1, gameID);
                var rs = prep.executeQuery();
                if (rs.next()) {
                    var white = rs.getString("whiteUsername");
                    var black = rs.getString("blackUsername");
                    var name = rs.getString("gameName");
                    var game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(gameID, white,black, name, game);
                }
                return null;
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void removeGame(int gameID) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var prep = conn.prepareStatement("DELETE FROM gameDB WHERE gameID=?")) {
                prep.setInt(1, gameID);
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updatePlayer(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException, AlreadyTakenException {
        var game = getGame(gameID);
        if (game == null) {
            throw new DataAccessException("game does not exist");
        }
        removeGame(gameID);
        GameData updated;
        if (color == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("color already taken");
            }
            updated = new GameData(gameID,
                    username, game.blackUsername(),
                    game.gameName(), game.game());
        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("color already taken");
            }
            updated = new GameData(gameID,
                    game.whiteUsername(), username,
                    game.gameName(), game.game());
        }
        addGame(updated);
    }

    public void clear() throws DataAccessException {
        try(var conn = getConnection()) {
            try(var prep = conn.prepareStatement("TRUNCATE TABLE userDB;")) {
                prep.executeUpdate();
            }
            try(var prep = conn.prepareStatement("TRUNCATE TABLE authDB;")) {
                prep.executeUpdate();
            }
            try(var prep = conn.prepareStatement("TRUNCATE TABLE gameDB;")) {
                prep.executeUpdate();
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
