package dataaccess;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;
import server.Server;
import chess.ChessGame;

public class DatabaseUnitTests {
    public DatabaseSQL db;
    public Server sv;

    private void makeUser() throws DataAccessException {
        var user = new UserData("NewUser", "Password", "Email");
        db.createUser(user);
    }

    private void addToken() throws DataAccessException {
        var auth = new AuthData("blahblahblah", "user");
        db.addAuth(auth);
    }

    private void makeGame() throws DataAccessException {
        var game = new GameData(1234, null, null, "bunga", new ChessGame());
        db.addGame(game);
    }

    @BeforeEach
    public void prep() {
        try {
            sv = new Server();
            db = sv.getDb();
            db.clear();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void createUserGood() {
        var user = new UserData("NewUser", "Password", "Email");
        try{
            db.createUser(user);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void createUserBad() {
        var user = new UserData(null, null, "email");
        Assertions.assertThrows(DataAccessException.class, () -> db.createUser(user));
    }

    @Test
    public void getUserGood() {
        try {
            makeUser();
            db.getUser("NewUser");
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void getUserDNE() {
        UserData result;
        try {
            makeUser();
            result = db.getUser("DNE");
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
        Assertions.assertNull(result);
    }

    @Test
    public void addAuthGood() {
        var auth = new AuthData("blahblahblah", "user");
        try{
            db.addAuth(auth);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void addAuthBad() {
        var auth = new AuthData(null, "user");
        Assertions.assertThrows(DataAccessException.class, () -> db.addAuth(auth));
    }

    @Test
    public void removeAuthGood() {
        try{
            addToken();
            db.removeAuth("blahblahblah");
        } catch(DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void removeAuthBad() {
        try{
            addToken();
            db.removeAuth("blah");
        } catch(DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void checkAuthGood() {
        try{
            addToken();
            Assertions.assertTrue(db.checkAuth("blahblahblah"));
        } catch(DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void checkAuthBad() {
        try{
            addToken();
            Assertions.assertFalse(db.checkAuth("blahbadbad"));
        } catch(DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void getAuthGood() {
        try{
            addToken();
            Assertions.assertNotNull(db.getAuth("blahblahblah"));
        } catch(DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void getAuthBad() {
        try{
            addToken();
            Assertions.assertNull(db.getAuth("blahbadbad"));
        } catch(DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void addGameGood() {
        var game = new GameData(1234, null, null, "bunga", new ChessGame());
        try{
            db.addGame(game);
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void addGameBad() {
        var game = new GameData(1234, null, null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> db.addGame(game));
    }

    @Test
    public void getGameGood() {
        try {
            makeGame();
            Assertions.assertNotNull(db.getGame(1234));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void getGameBad() {
        try{
            makeGame();
            Assertions.assertNull(db.getGame(123));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void removeGameGood() {
        try{
            makeGame();
            db.removeGame(1234);
            Assertions.assertNull(db.getGame(1234));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void removeGameBad() {
        try{
            makeGame();
            db.removeGame(123);
            Assertions.assertNotNull(db.getGame(1234));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void updatePlayerGood() {
        try {
            makeGame();
            db.updatePlayer(1234, ChessGame.TeamColor.WHITE, "PhatNickher");
            Assertions.assertThrows(AlreadyTakenException.class, () -> {
                db.updatePlayer(1234, ChessGame.TeamColor.WHITE, "PhatNickher");
            });
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void updatePlayerBad() {
        try{
            makeGame();
            Assertions.assertThrows(DataAccessException.class, () -> {
                db.updatePlayer(000, ChessGame.TeamColor.WHITE, "PhatNickher");
            });
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void getListGood() {
        try{
            makeGame();
            var list = db.getList();
            Assertions.assertFalse(list.isEmpty());
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void getListBad() {
        try{
            var list = db.getList();
            Assertions.assertTrue(list.isEmpty());
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void clearGood() {
        try{
            makeUser();
            makeGame();
            addToken();
            db.clear();
            Assertions.assertTrue(db.isEmpty());
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }
}
