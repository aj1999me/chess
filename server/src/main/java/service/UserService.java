package service;

import java.util.UUID;
import dataaccess.DataAccessException;

public class UserService {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        String username = req.username();
        String password = req.password();
        String email = req.email();
        if (getUser(username) != null) {
            throw new DataAccessException("username already taken");
        }

        return new RegisterResult(username, authToken);
    }
    public LoginResult login(LoginRequest req) {
        String username = req.username();
        String password = req.password();
        String authToken = generateToken();
        return new LoginResult(username, authToken);
    }
    public void logout(LogoutRequest) {

    }
}
