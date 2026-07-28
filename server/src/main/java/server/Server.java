package server;

import io.javalin.*;
import dataaccess.*;
import model.*;
import service.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import java.util.Map;

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
                .put("/game", this::join)
                .exception(AlreadyTakenException.class, this::ATexceptionHandler)
                .exception(DataAccessException.class, this::DAexceptionHandler)
                .exception(UnauthorizedAccessException.class, this::UAexceptionHandler);

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

    private void register(Context cxt) throws AlreadyTakenException  {
        RegisterResult result = us.register(getBodyObject(cxt, RegisterRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void login(Context cxt) throws UnauthorizedAccessException {
        LoginResult result = us.login(getBodyObject(cxt, LoginRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void logout(Context cxt) throws UnauthorizedAccessException{
        if (!db.checkAuth(cxt.header("authorization"))){
            throw new UnauthorizedAccessException("unauthorized access");
        }
        us.logout(cxt.header("authorization"));
        cxt.status(200);
    }

    private void clear(Context cxt) {
        us.clear();
        cxt.status(200);
    }

    private void list(Context cxt) throws UnauthorizedAccessException {
        ListResult result = gs.listGames(getBodyObject(cxt, ListRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void create(Context cxt) throws UnauthorizedAccessException, AlreadyTakenException {
        MakeGameResult result = gs.makeGame(getBodyObject(cxt, MakeGameRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void join(Context cxt) throws DataAccessException, AlreadyTakenException, UnauthorizedAccessException {
        gs.joinGame(getBodyObject(cxt, JoinRequest.class));
        cxt.status(200);
    }

    private static <T> T getBodyObject(Context context, Class<T> clazz) {
        return new Gson().fromJson(context.body(), clazz);
    }

    private void ATexceptionHandler(AlreadyTakenException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(403);
        cxt.json(body);
    }

    private void DAexceptionHandler(DataAccessException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(500);
        cxt.json(body);
    }

    private void UAexceptionHandler(UnauthorizedAccessException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(401);
        cxt.json(body);
    }

}
