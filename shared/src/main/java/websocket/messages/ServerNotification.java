package websocket.messages;

public class ServerNotification extends ServerMessage {
    private final String message;

    public ServerNotification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
