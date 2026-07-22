package service;

import java.util.UUID;
import dataaccess.DataAccessException;
import model.*;

public class UserService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (getUser(req.username()) != null) {
            throw new DataAccessException("username already taken");
        }
        createUser(new userData(req.username(), req.password(), req.email()));
        return new RegisterResult(req.username(), generateToken());
    }
    public LoginResult login(LoginRequest req) throws DataAccessException {
        userData user = getUser(req.username());
        if (!user.password().equals(req.password())) {
            throw new DataAccessException("wrong password");
        }
        return new LoginResult(req.username(), generateToken());
    }
    public void logout(LogoutRequest) {

    }
}
