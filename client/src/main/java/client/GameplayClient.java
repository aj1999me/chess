package client;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class GameplayClient extends Endpoint {
    public Session session;

    public GameplayClient(String host, int port) throws Exception {
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
}
