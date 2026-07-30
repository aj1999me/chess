package server;

import io.javalin.*;
import dataaccess.*;
import service.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    private Database db;
    private final UserService us;
    private final GameService gs;

    public Server() {
        db = new Database();
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
                .exception(AlreadyTakenException.class, this::atExceptionHandler)
                .exception(DataAccessException.class, this::daExceptionHandler)
                .exception(UnauthorizedAccessException.class, this::uaExceptionHandler)
                .exception(BadRequestException.class, this::brExceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void register(Context cxt) throws BadRequestException, AlreadyTakenException, DataAccessException  {
        RegisterResult result = us.register(getBodyObject(cxt, RegisterRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void login(Context cxt) throws BadRequestException, UnauthorizedAccessException, DataAccessException {
        LoginResult result = us.login(getBodyObject(cxt, LoginRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void logout(Context cxt) throws DataAccessException, UnauthorizedAccessException{
        if (!db.checkAuth(cxt.header("authorization"))){
            throw new UnauthorizedAccessException("unauthorized access");
        }
        us.logout(cxt.header("authorization"));
        cxt.status(200);
    }

    private void clear(Context cxt) throws DataAccessException {
        us.clear();
        cxt.status(200);
    }

    private void list(Context cxt) throws UnauthorizedAccessException {
        if (!db.checkAuth(cxt.header("authorization"))) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
        ListResult result = gs.listGames();
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void create(Context cxt) throws BadRequestException, UnauthorizedAccessException, AlreadyTakenException {
        if (!db.checkAuth(cxt.header("authorization"))) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
        MakeGameResult result = gs.makeGame(getBodyObject(cxt, MakeGameRequest.class));
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void join(Context cxt) throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedAccessException {
        if (!db.checkAuth(cxt.header("authorization"))) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
        gs.joinGame(getBodyObject(cxt, JoinRequest.class), cxt.header("authorization"));
        cxt.status(200);
    }

    private static <T> T getBodyObject(Context context, Class<T> clazz) {
        return new Gson().fromJson(context.body(), clazz);
    }

    private void atExceptionHandler(AlreadyTakenException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(403);
        cxt.result(body);
    }

    private void daExceptionHandler(DataAccessException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(500);
        cxt.result(body);
    }

    private void uaExceptionHandler(UnauthorizedAccessException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(401);
        cxt.result(body);
    }

    private void brExceptionHandler(BadRequestException e, Context cxt) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        cxt.status(400);
        cxt.result(body);
    }
}
