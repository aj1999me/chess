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
        ClientMain.clear("localhost", 8080);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void firstTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            new ClientMain("localhost", 8080).register(req);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void secondTest() {
        var req = new LoginRequest("aj", "mungabunga");
        try{
            firstTest();
            new ClientMain("localhost", 8080).login(req);
        } catch(Exception e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void thirdTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var client = new ClientMain("localhost", 8080);

            new LoggedInClient(client.register(req), "localhost", 8080, client.getHttpClient()).logout();
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void fourthTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var client = new ClientMain("localhost", 8080);

            var innerLoop  = new LoggedInClient(client.register(req), "localhost", 8080, client.getHttpClient());
            innerLoop.makeGame("stinkabunga");
            innerLoop.makeGame("ungabuhungabunga");
            innerLoop.printList(innerLoop.list());
            innerLoop.makeGame("extrabungalicious");
            innerLoop.printList(innerLoop.list());
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

    @Test
    public void fifthTest() {
        var req = new RegisterRequest("aj", "mungabunga", "googoogaga@email.com");
        try{
            var client = new ClientMain("localhost", 8080);

            var innerLoop  = new LoggedInClient(client.register(req), "localhost", 8080, client.getHttpClient());
            innerLoop.makeGame("stinkabunga");
            innerLoop.printList(innerLoop.list());
            innerLoop.joinGame(1, ChessGame.TeamColor.WHITE);
            innerLoop.printList(innerLoop.list());
        } catch (Exception e) {
            throw new AssertionError("failed");
        }
    }

}
