package client;

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

}
