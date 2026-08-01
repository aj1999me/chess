package service;

import java.util.UUID;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private DatabaseSQL db;

    public UserService() throws DataAccessException {
        db = new DatabaseSQL();
    }
    public UserService(DatabaseSQL db) {
        this.db = db;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest req) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (req.username() == null || req.password() == null) {
            throw new BadRequestException("username missing");
        }
        if (db.getUser(req.username()) != null) {
            throw new AlreadyTakenException("username already taken");
        }
        try {
            db.createUser(new UserData(req.username(), req.password(), req.email()));
        } catch(DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }
        String token = generateToken();
        db.addAuth(new AuthData(token, req.username()));
        return new RegisterResult(req.username(), token);
    }
    public LoginResult login(LoginRequest req) throws UnauthorizedAccessException, BadRequestException, DataAccessException {
        if (req.username() == null || req.password() == null) {
            throw new BadRequestException("username missing");
        }
        UserData user = db.getUser(req.username());
        if (user == null) {
            throw new UnauthorizedAccessException("user does not exist");
        }
        boolean corrPass = BCrypt.checkpw(req.password(), user.password());
        if (!corrPass) {
            throw new UnauthorizedAccessException("wrong password");
        }
        String auth = generateToken();
        db.addAuth(new AuthData(auth, req.username()));
        return new LoginResult(auth, req.username());
    }
    public void logout(String token) throws DataAccessException {
        db.removeAuth(token);
    }

    public void clear() throws DataAccessException {
        db.clear();
    }

    public DatabaseSQL getDb() {
        return db;
    }
}
