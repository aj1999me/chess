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

    public RegisterResult register(RegisterRequest req) throws BadRequestException, AlreadyTakenException {
        if (req.username() == null || req.password() == null) {
            throw new BadRequestException("username missing");
        }
        if (db.getUser(req.username()) != null) {
            throw new AlreadyTakenException("username already taken");
        }
        db.createUser(new userData(req.username(), req.password(), req.email()));
        String token = generateToken();
        db.addAuth(new authData(token, req.username()));
        return new RegisterResult(req.username(), token);
    }
    public LoginResult login(LoginRequest req) throws UnauthorizedAccessException, BadRequestException {
        if (req.username() == null || req.password() == null) {
            throw new BadRequestException("username missing");
        }
        userData user = db.getUser(req.username());
        if (user == null) {
            throw new UnauthorizedAccessException("user does not exist");
        }
        if (!user.password().equals(req.password())) {
            throw new UnauthorizedAccessException("wrong password");
        }
        String auth = generateToken();
        db.addAuth(new authData(auth, req.username()));
        return new LoginResult(auth, req.username());
    }
    public void logout(String token) {
        db.removeAuth(token);
    }

    public void clear() {
        db.clear();
    }
}
