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
        db = new database();
        us = new UserService(db);
        gs = new GameService(db);
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/db", this::clear)
                .delete("/session", this::logout)
                .get("/game", this::list)
                .post("/game", this::create)
                .put("/game", this::join);

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
