package server;

import io.javalin.*;
import dataaccess.database;
import model.*;
import service.*;
import io.javalin.http.Context;
import com.google.gson.Gson;


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

    private void register(Context cxt) {
        RegisterResult result = us.register(getBodyObject(cxt, RegisterRequest.class));
        cxt.json(result);
    }

    private void login(Context cxt) {
        LoginResult result = us.login(getBodyObject(cxt, LoginRequest.class));
        cxt.json(result);
    }

    private void logout(Context cxt) {
        us.logout(getBodyObject(cxt, LogoutRequest.class));
    }

    private void clear(Context cxt) {
        us.clear();
    }

    private void list(Context cxt) {
        ListResult result = gs.listGames(getBodyObject(cxt, ListRequest.class));
        cxt.json(result);
    }

    private void create(Context cxt) {
        MakeGameResult result = gs.makeGame(getBodyObject(cxt, MakeGameRequest.class));
        cxt.json(result);
    }

    private void join(Context cxt) {
        gs.joinGame(getBodyObject(cxt, JoinRequest.class));
    }

    private static <T> T getBodyObject(Context context, Class<T> clazz) {
        return new Gson().fromJson(context.body(), clazz);
    }



}
