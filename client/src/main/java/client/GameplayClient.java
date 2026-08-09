package client;

import chess.*;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.*;
import websocket.messages.*;
import java.net.URI;
import java.util.Locale;

public class GameplayClient extends Endpoint {
    public Session session;
    private String token;
    private int id;
    private final Boolean white;
    private ChessGame game;

    public GameplayClient(String host, int port, String token, int id, boolean white) throws Exception {
        this.token = token;
        this.id = id;
        this.white = white;
        String urlString = String.format(Locale.getDefault(), "ws://%s:%d/ws", host, port);

        URI uri = new URI(urlString);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        session.addMessageHandler(String.class, (MessageHandler.Whole<String>) message -> {
            var type = new Gson().fromJson(message, ServerMessage.class).getServerMessageType();
            if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
                game = new Gson().fromJson(message, LoadGameMessage.class).game();
                new DrawBoard(white, game);
            } else if (type == ServerMessage.ServerMessageType.ERROR) {
                var error = new Gson().fromJson(message, ErrorMessage.class).getMessage();
                System.out.printf(error);
            } else if (type == ServerMessage.ServerMessageType.NOTIFICATION) {
                var notification = new Gson().fromJson(message, ServerNotification.class).getMessage();
                System.out.printf(notification);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect() throws Exception {
        var command = new ConnectCommand(token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void leave() throws Exception {
        var command = new LeaveCommand(token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
        session.close();
    }

    public void resign() throws Exception {
        var command = new ResignCommand(token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void makeMove(ChessMove move) throws Exception {
        var command = new MakeMoveCommand(token, id, move);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void refresh() throws Exception {
        var command = new RefreshCommand(token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void highlight(ChessPosition pos) throws Exception {
        if (game.getBoard().getPiece(pos) == null) {
            throw new Exception("Error: there is no piece there%n%n");
        }
        new DrawBoard(white, game, pos);
    }
}
