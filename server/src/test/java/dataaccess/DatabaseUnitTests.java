package dataaccess;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

public class DatabaseUnitTests {
    public DatabaseSQL db;

    @Test
    public void createUserGood() {
        var user = new UserData("NewUser", "Password", "Email");
        try{
            var db = new DatabaseSQL();
            db.createUser(user);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }

    }
}
