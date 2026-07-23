package service;

import java.util.Collection;
import chess.ChessGame;
import model.gameData;

public record ListResult(Collection<gameData> gameList) {}
