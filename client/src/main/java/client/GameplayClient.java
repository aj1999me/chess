package client;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerNotification;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class GameplayClient extends Endpoint {
    public Session session;
    private String host;
    private int port;
    private String token;
    private int id;
    private final boolean white;

    public GameplayClient(String host, int port, String token, int id, boolean white) throws Exception {
        this.host = host;
        this.port = port;
        this.token = token;
        this.id = id;
        this.white = white;
        String urlString = String.format(Locale.getDefault(), "ws://%s:%d/ws", host, port);

        URI uri = new URI(urlString);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            var type = new Gson().fromJson(message, ServerMessage.class).getServerMessageType();
            if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
                var game = new Gson().fromJson(message, LoadGameMessage.class).game();
                new DrawBoard(white, game.getBoard());
            } else if (type == ServerMessage.ServerMessageType.ERROR) {

            } else if (type == ServerMessage.ServerMessageType.NOTIFICATION) {
                var notification = new Gson().fromJson(message, ServerNotification.class).getMessage();
                System.out.printf(notification);
            }
        });
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect() throws Exception {
        var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void leave() throws Exception {
        var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void resign() throws Exception {
        var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, token, id);
        var json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }
}
