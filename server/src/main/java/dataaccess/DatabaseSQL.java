package dataaccess;
import chess.ChessGame;
import model.*;
import service.ListEntry;
import static dataaccess.DatabaseManager.*;

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
            var prep = conn.prepareStatement("DELETE FROM authDB WHERE authToken=?");
            prep.setString(1, authToken);
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean checkAuth(String authToken) throws DataAccessException {
        try(var conn = getConnection()) {
            var prep = conn.prepareStatement("SELECT authToken FROM authDB WHERE authToken=?");
            prep.setString(1, authToken);
            var rs = prep.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken);

    public Collection<ListEntry> getList();

    public void addGame(GameData game);

    public GameData getGame(int gameID);

    public void updatePlayer(int gameID, ChessGame.TeamColor color, String username);

    public boolean checkColor(int gameID, ChessGame.TeamColor color);

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
