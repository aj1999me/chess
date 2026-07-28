package service;

import chess.ChessGame.TeamColor;

public record JoinRequest(TeamColor playerColor, int gameID) {}
