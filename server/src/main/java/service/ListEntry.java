package service;
import model.GameData;

public class ListEntry {

    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;

    public ListEntry(GameData obj) {
        this.gameID = obj.gameID();
        whiteUsername = obj.whiteUsername();
        blackUsername = obj.blackUsername();
        gameName = obj.gameName();
    }

    public ListEntry(int gameID, String whiteUsername, String blackUsername, String gameName) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }
}
