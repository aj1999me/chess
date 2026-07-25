package service;

import java.util.UUID;
import dataaccess.DataAccessException;
import model.*;
import dataaccess.database;

public class UserService {
    private database db;

    public UserService(database db) {
        this.db = db;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (db.getUser(req.username()) != null) {
            throw new DataAccessException("username already taken");
        }
        db.createUser(new userData(req.username(), req.password(), req.email()));
        db.addAuth(new authData(generateToken(), req.username()));
        return new RegisterResult(req.username(), generateToken());
    }
    public LoginResult login(LoginRequest req) throws DataAccessException {
        userData user = db.getUser(req.username());
        if (!user.password().equals(req.password())) {
            throw new DataAccessException("wrong password");
        }

        return new LoginResult(req.username(), generateToken());
    }
    public void logout(LogoutRequest req) throws DataAccessException {
        if (!db.checkAuth(req.authToken())) {
            throw new DataAccessException("unauthorized access");
        }
        db.removeAuth(req.authToken());
    }

    public void clear() {
        db.clear();
    }
}
