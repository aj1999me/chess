package client;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class GameplayClient extends Endpoint {
    public Session session;
    private String host;
    private int port;
    private String token;
    private int id;

    public GameplayClient(String host, int port, String token, int id) throws Exception {
        this.host = host;
        this.port = port;
        this.token = token;
        this.id = id;
        String urlString = String.format(Locale.getDefault(), "ws://%s:%d/ws", host, port);

        URI uri = new URI(urlString);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
                System.out.println("\nEnter another message you want to echo:");
            }
        });
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect() throws Exception {
        var txt = new UserGameCommand(UserGameCommand.CommandType.CONNECT, token, id);
        var json = new Gson().toJson(txt);
        session.getBasicRemote().sendText(json);
    }
}
