package server;

import io.javalin.*;
import dataaccess.database;
import model.*;
import service.*;

public class Server {

    private final Javalin javalin;
    private database db;
    private final UserService us;
    private final GameService gs;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        db = new database();
        us = new UserService(db);
        gs = new GameService(db);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public enum UserColor {WHITE, BLACK}
}
