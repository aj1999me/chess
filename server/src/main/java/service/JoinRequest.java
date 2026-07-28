package service;

import server.Server.UserColor;

public record JoinRequest(int gameID, UserColor color) {}
