package dataaccess;
import chess.ChessGame;
import model.*;
import service.ListEntry;
import static dataaccess.DatabaseManager.*;

import java.sql.*;
import java.util.Collection;

public class DatabaseSQL {
    public DatabaseSQL() throws DataAccessException, SQLException {
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
        }
    }

    public UserData getUser(String username) {

    }

    public void createUser(UserData user) throws SQLException, DataAccessException {
        try(var conn = getConnection()) {
            try(var preppedStatement = conn.prepareStatement("INSERT INTO userDB (username, password, email) VALUES(?,?,?)")) {
                preppedStatement.setString(1, user.username());
                preppedStatement.setString(2, user.password()); // add hashing functionality
                preppedStatement.setString(3, user.email());
                preppedStatement.executeUpdate();
            }
        }
    }

    public void addAuth(AuthData auth) throws SQLException, DataAccessException {
        try(var conn = getConnection()) {
            try(var preppedStatement = conn.prepareStatement("INSERT INTO userDB (authToken, username) VALUES(?,?,?)")) {
                preppedStatement.setString(1, auth.authToken());
                preppedStatement.setString(2, auth.username()); // add hashing functionality
                preppedStatement.executeUpdate();
            }
        }
    }

    public void removeAuth(String authToken);

    public boolean checkAuth(String authToken);

    public AuthData getAuth(String authToken);

    public Collection<ListEntry> getList();

    public void addGame(GameData game);

    public GameData getGame(int gameID);

    public void updatePlayer(int gameID, ChessGame.TeamColor color, String username);

    public boolean checkColor(int gameID, ChessGame.TeamColor color);
}
