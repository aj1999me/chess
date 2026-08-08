package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.GsonBuilder;
import io.javalin.*;
import dataaccess.*;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import service.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import websocket.commands.*;
import websocket.messages.*;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Map;
import java.util.HashSet;


public class Server {

    private final Javalin javalin;
    private DatabaseSQL db;
    private final UserService us;
    private final GameService gs;

    private HashSet<WsContext> clients = new HashSet<>();

    public Server() {
        try {
            db = new DatabaseSQL();
        } catch(DataAccessException e) {
            System.out.println("failed to initiate database");
        }
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
                .ws("/ws", ws -> {
                    ws.onConnect(ctx -> {
                        ctx.enableAutomaticPings();
                        System.out.println("Websocket connected");
                        clients.add(ctx);
                    });
                    ws.onMessage(this::handleCommand);
                    ws.onClose(ctx -> System.out.println("Websocket closed"));
                })
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
        db.clear();
        clients.clear();
        cxt.status(200);
    }

    private void list(Context cxt) throws UnauthorizedAccessException, DataAccessException {
        if (!db.checkAuth(cxt.header("authorization"))) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
        ListResult result = gs.listGames();
        cxt.status(200);
        cxt.result(new Gson().toJson(result));
    }

    private void create(Context cxt) throws BadRequestException, UnauthorizedAccessException, AlreadyTakenException, DataAccessException {
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

    public DatabaseSQL getDb() {
        return db;
    }

    public void handleCommand(WsMessageContext ctx) {
        var json = ctx.message();
        var type = new Gson().fromJson(json, UserGameCommand.class).getCommandType();
        if (type == UserGameCommand.CommandType.CONNECT) {
            var command = new Gson().fromJson(json, ConnectCommand.class);
            connectToGame(ctx, command);
        } else if (type == UserGameCommand.CommandType.LEAVE) {
            var command = new Gson().fromJson(json, LeaveCommand.class);
            leave(ctx, command);
        } else if (type == UserGameCommand.CommandType.RESIGN) {
            var command = new Gson().fromJson(json, ResignCommand.class);


        } else if (type == UserGameCommand.CommandType.MAKE_MOVE) {
            var command = new Gson().fromJson(json, MakeMoveCommand.class);
            makeMove(ctx, command);
        }
    }

    public void sendError(WsContext ctx, String message) {
        var json = new Gson().toJson(new ErrorMessage(message));
        ctx.send(json);
    }

    public void loadGame(WsContext ctx, ChessGame game) throws IOException {
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .create();
        var message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String str = gson.toJson(message);
        ctx.send(str);
    }

    public void notifyAll(WsContext ctx, String message) throws IOException {
        var notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        for (var client : clients) {
            if (!client.session.equals(ctx.session)) {
                var json = new Gson().toJson(notification);
                client.send(json);
            }
        }
    }

    public void leaveGame(WsContext ctx, int gameID, Boolean white) throws Exception {
        System.out.printf("running leaveGame function%n%n");
        if (white != null) {
            ChessGame.TeamColor color;
            if (white) {
                color = ChessGame.TeamColor.WHITE;
            } else {
                color = ChessGame.TeamColor.BLACK;
            }
            db.updatePlayer(gameID, color, null);
        }
        clients.removeIf(client -> client.session.equals(ctx.session));
    }

    public void connectToGame(WsContext ctx, ConnectCommand command) {
        try {
            if (db.checkAuth(command.getAuthToken())) {
                String username = db.getAuth(command.getAuthToken()).username();
                var game = db.getGame(command.getGameID());
                if (game == null) {
                    throw new Exception("Error: incorrect gameID");
                }
                loadGame(ctx, game.game());
                String message = username + " joined the game as ";
                if (command.white() == null) {
                    message += "observer";
                } else if (command.white()) {
                    message += "white";
                } else {
                    message += "black";
                }
                System.out.println(message);
                notifyAll(ctx, message);
            } else {
                throw new UnauthorizedAccessException("Error: unauthorized user%n%n");
            }
        } catch(Exception e) {
            sendError(ctx, e.getMessage());
        }
    }

    public void leave(WsContext ctx, LeaveCommand command) {
        try {
            if (db.checkAuth(command.getAuthToken())) {
                String username = db.getAuth(command.getAuthToken()).username();
                notifyAll(ctx, username + " left the game.%n%n");
                leaveGame(ctx, command.getGameID(), command.white());
            } else {
                throw new UnauthorizedAccessException("Error: unauthorized user%n%n");
            }
        } catch(Exception e) {
            sendError(ctx, e.getMessage());
        }
    }

    public void makeMove(WsContext ctx, MakeMoveCommand command) {
        try {
            if (db.checkAuth(command.getAuthToken())) {
                var newGame = db.updateGame(command.getGameID(), command.move());
                for (var client : clients) {
                    loadGame(client, newGame);
                }
                notifyAll(ctx, "a move was made.%n%n");

                if (newGame.isInCheckmate(newGame.getTeamTurn())) {
                    newGame.endGame();
                    notifyAll(ctx, "that's checkmate!%n%n");
                } else if (newGame.isInCheck(newGame.getTeamTurn())) {
                    notifyAll(ctx, "that's check%n%n");
                } else if (newGame.isInStalemate(newGame.getTeamTurn())) {
                    newGame.endGame();
                    notifyAll(ctx, "that's a stalemate!%n%n");
                }
            } else {
                throw new UnauthorizedAccessException("Error: unauthorized user%n%n");
            }
        } catch(Exception e) {
            sendError(ctx, e.getMessage());
        }
    }
}
