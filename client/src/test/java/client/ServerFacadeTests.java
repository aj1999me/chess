package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() {
        ServerFacade.clear("localhost", 8080);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void firstTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            new ServerFacade("localhost", 8080).register(req);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void secondTest() {
        var req = new LoginRequest("aj", "mungabunga");
        try{
            firstTest();
            new ServerFacade("localhost", 8080).login(req);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void thirdTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", 8080);
            facade.logout(facade.register(req));
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void fourthTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", 8080);
            String auth = facade.register(req);
            facade.makeGame("stinkabunga", auth);
            facade.makeGame("ungabuhungabunga", auth);
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void fifthTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var facade = new ServerFacade("localhost", 8080);
            String auth = facade.register(req);
            int id = facade.makeGame("stinkabunga", auth);
            facade.joinGame(id, ChessGame.TeamColor.WHITE, auth);
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

}
