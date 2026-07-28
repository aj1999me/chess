package service;
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
        var req1 = new RegisterRequest("ExistingUser", "RightPassword", "bunga@munga.com");
        try {
            var result = service.register(req1);
            service.logout(result.authToken());
        } catch(Exception e) {
            throw new AssertionError("failed to register or login");
        }
    }
}
