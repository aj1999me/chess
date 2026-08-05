package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        ServerFacadeTests.port = port;
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() {
        ServerFacade.clear("localhost", port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerGood() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            new ServerFacade("localhost", port).register(req);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void registerBad() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            facade.register(req);
            Assertions.assertThrows(Exception.class, () -> facade.register(req));
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void loginGood() {
        var req = new LoginRequest("aj", "mungabunga");
        try{
            registerGood();
            new ServerFacade("localhost", port).login(req);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void loginBad() {
        var req = new LoginRequest("aj", "wrongPassword");
        try{
            registerGood();
            Assertions.assertThrows(Exception.class, () -> new ServerFacade("localhost", port).login(req));
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void logoutGood() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            facade.logout(facade.register(req));
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void logoutBad() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            var auth = facade.register(req);
            Assertions.assertThrows(Exception.class, () -> facade.logout(auth + "wrongPassword"));
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void makeGood() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            String auth = facade.register(req);
            facade.makeGame("stinkabunga", auth);
            facade.makeGame("ungabuhungabunga", auth);
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void makeBad() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            String auth = facade.register(req);
            facade.makeGame("stinkabunga", auth);
            Assertions.assertThrows(Exception.class, () -> facade.makeGame("stinkabunga", auth));
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void joinGood() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            String auth = facade.register(req);
            int id = facade.makeGame("stinkabunga", auth);
            facade.joinGame(id, ChessGame.TeamColor.WHITE, auth);
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void joinBad() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            String auth = facade.register(req);
            int id = facade.makeGame("stinkabunga", auth);
            Assertions.assertThrows(Exception.class, () -> facade.joinGame(1, ChessGame.TeamColor.WHITE, auth));
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void listGood() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            String auth = facade.register(req);
            int id = facade.makeGame("stinkabunga", auth);
            var list = facade.list(auth);
            assert list.games().size() == 1;
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void listBad() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", port);
            String auth = facade.register(req);
            int id = facade.makeGame("stinkabunga", auth);
            Assertions.assertThrows(Exception.class, () -> facade.list(auth + "bad auth"));
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }
}
