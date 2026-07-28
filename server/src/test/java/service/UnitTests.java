package service;
import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedAccessException;
import model.*;
import org.junit.jupiter.api.*;

public class UnitTests {

    @Test
    public void goodRegistration() {
        var service = new UserService();
        var req = new RegisterRequest("NewUser", "Password", "stuff@bonk.com");
        try {
            var result = service.register(req);
            Assertions.assertEquals("NewUser", result.username());
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void badRegistration() {
        var service = new UserService();
        var req = new RegisterRequest(null, "Password", "email@email.email");
        try {
            var result = service.register(req);
            throw new AssertionError("did not throw error");
        } catch(BadRequestException e) {
            assert true;
        } catch(Exception e) {
            throw new AssertionError("threw wrong exception");
        }
    }

    @Test
    public void goodLogin() {
        var service = new UserService();
        var req1 = new RegisterRequest("ExistingUser", "RightPassword", "bunga@munga.com");
        try {
            service.register(req1);
            var req2 = new LoginRequest("ExistingUser", "RightPassword");
            service.login(req2);
        } catch(Exception e) {
            throw new AssertionError("failed to register or login");
        }
    }

    @Test
    public void badLogin() {
        var service = new UserService();
        var req1 = new RegisterRequest("ExistingUser", "RightPassword", "bunga@munga.com");
        try {
            service.register(req1);
        } catch(Exception e) {
            throw new AssertionError("failed to register");
        }
        var req2 = new LoginRequest("ExistingUser", "WrongPassword");
        try {
            service.login(req2);
        } catch(BadRequestException e) {
            throw new AssertionError("threw wrong exception");
        } catch(UnauthorizedAccessException e) {
            assert true;
        }
    }

    @Test
    public void goodLogout() {
        var service = new UserService();
        var req = new RegisterRequest("ExistingUser", "RightPassword", "bunga@munga.com");
        try {
            var result = service.register(req);
            service.logout(result.authToken());
        } catch(Exception e) {
            throw new AssertionError("failed to register or logout");
        }
    }

    @Test
    public void badLogout() {
        var service = new UserService();
        var req = new RegisterRequest("ExistingUser", "RightPassword", "bunga@munga.com");
        String token;
        try {
            token = service.register(req).authToken();
        } catch(Exception e) {
            throw new AssertionError("failed to register");
        }
        try {
            service.logout(token);
        } catch(Exception e) {
            assert true;
        }
    }

    @Test
    public void goodList() {
        var service = new UserService();
        var gs = new GameService(service.getDb());
        var req = new RegisterRequest("User", "Password", "bunga@munga.com");
        try {
            service.register(req);
        } catch(Exception e) {
            throw new AssertionError("failed to register");
        }
        ListResult res = gs.listGames();
        assert res.games().isEmpty();
    }

    @Test
    public void badList() {
        var service = new UserService();
        var gs = new GameService(service.getDb());
        var req1 = new RegisterRequest("User", "Password", "bunga@munga.com");
        try {
            service.register(req1);
            var req2 = new MakeGameRequest("aloha");
            gs.makeGame(req2);
        } catch(Exception e) {
            throw new AssertionError("failed to register or create game");
        }
        assert !service.getDb().checkAuth("ugwemugwemosas");
        ListResult res = gs.listGames();
    }

    @Test
    public void goodCreate() {
        var service = new UserService();
        var gs = new GameService(service.getDb());
        var req1 = new RegisterRequest("User1", "Passwort", "bongo@mungees.com");
        try {
            service.register(req1);
            var req2 = new MakeGameRequest("bober");
            gs.makeGame(req2);
        } catch(Exception e) {
            throw new AssertionError("failed to register or create game");
        }
    }

    @Test
    public void badCreate() {
        var service = new UserService();
        var gs = new GameService(service.getDb());
        var req1 = new RegisterRequest("Userino", "Passu", "bungees@munga.com");
        try {
            service.register(req1);
        } catch(Exception e) {
            throw new AssertionError("failed to register");
        }
        try {
            var req2 = new MakeGameRequest(null);
            gs.makeGame(req2);
        } catch(BadRequestException e) {
            assert true;
        } catch(AlreadyTakenException e) {
            throw new AssertionError("wrong exception thrown");
        }
    }

    @Test
    public void goodJoin() {
        var service = new UserService();
        var gs = new GameService(service.getDb());
        var req1 = new RegisterRequest("User123", "Passwordo", "bongees@mungees.com");
        int gameID;
        String token;
        try {
            token = service.register(req1).authToken();
            var req2 = new MakeGameRequest("bobrowisko");
            gameID = gs.makeGame(req2).gameID();
        } catch (Exception e) {
            throw new AssertionError("failed to register or create game");
        }
        try {
            var req3 = new JoinRequest(ChessGame.TeamColor.WHITE, gameID);
            gs.joinGame(req3, token);
        } catch (Exception e) {
            throw new AssertionError("failed to join game");
        }
    }

    @Test
    public void badJoin() {
        var service = new UserService();
        var gs = new GameService(service.getDb());
        var req1 = new RegisterRequest("User13", "Passies", "ugh@mungees.com");
        int gameID;
        String token;
        try {
            token = service.register(req1).authToken();
            var req2 = new MakeGameRequest("bobry");
            gameID = gs.makeGame(req2).gameID();
        } catch(Exception e) {
            throw new AssertionError("failed to register or create game");
        }
        var req3 = new JoinRequest(ChessGame.TeamColor.WHITE, gameID);
        try {
            gs.joinGame(req3, token);
        } catch(Exception e) {
            throw new AssertionError("failed to join game");
        }
        try {
            gs.joinGame(req3, token);
            throw new AssertionError("tried to join game as color that's already taken");
        } catch(AlreadyTakenException e) {
            assert true;
        } catch(Exception e) {
            throw new AssertionError("threw wrong exception");
        }
    }
}
