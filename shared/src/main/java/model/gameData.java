package model;

import chess.ChessGame;
import java.util.Objects;

public class gameData {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final ChessGame game;

    gameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }
    public int getGameID() {
        return gameID;
    }
    public String getWhiteUsernameUser() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public String getGameName() {
        return gameName;
    }
    public ChessGame getGame() {
        return game;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        model.gameData other = (model.gameData) o;
        return other.gameID == gameID &&
                other.whiteUsername.equals(whiteUsername) &&
                other.blackUsername.equals(blackUsername) &&
                other.gameName.equals(gameName) &&
                other.game.equals(game);
        }
        @Override
        public int hashCode() {
            return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
        }
}
