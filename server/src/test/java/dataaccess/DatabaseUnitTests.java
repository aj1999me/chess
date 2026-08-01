package dataaccess;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;
import server.Server;

public class DatabaseUnitTests {
    public DatabaseSQL db;
    public Server sv;

    private void makeUser() throws DataAccessException {
        var user = new UserData("NewUser", "Password", "Email");
        db.createUser(user);
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
}
