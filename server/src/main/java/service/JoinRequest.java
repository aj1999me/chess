package service;

import server.Server.UserColor;

public record JoinRequest(String authToken, int gameID, UserColor color) {}
