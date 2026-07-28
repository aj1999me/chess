package service;

import java.util.UUID;
import dataaccess.*;
import model.*;

public class UserService {
    private database db;

    public UserService(database db) {
        this.db = db;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest req) throws AlreadyTakenException {
        if (db.getUser(req.username()) != null) {
            throw new AlreadyTakenException("username already taken");
        }
        db.createUser(new userData(req.username(), req.password(), req.email()));
        String token = generateToken();
        db.addAuth(new authData(token, req.username()));
        return new RegisterResult(req.username(), token);
    }
    public LoginResult login(LoginRequest req) throws UnauthorizedAccessException {
        userData user = db.getUser(req.username());
        if (!user.password().equals(req.password())) {
            throw new UnauthorizedAccessException("wrong password");
        }
        String auth = generateToken();
        db.addAuth(new authData(req.username(), auth));
        return new LoginResult(req.username(), auth);
    }
    public void logout(String token) {
        db.removeAuth(token);
    }

    public void clear() {
        db.clear();
    }
}
