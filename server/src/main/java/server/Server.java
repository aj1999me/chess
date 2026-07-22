package server;

import io.javalin.*;
import model.*;
import java.util.TreeMap;
public class Server {

    private TreeMap<String, userData> userDB;
    private TreeMap<String/*authToken*/, authData> authDB;
    private TreeMap<Integer/*gameID*/, gameData> gameDB;

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
