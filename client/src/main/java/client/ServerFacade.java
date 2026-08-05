package client;

import chess.ChessGame;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ServerFacade {
    private final String host;
    private final int port;
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public ServerFacade(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String login(LoginRequest req) throws Exception {
        var json = new Gson().toJson(req);
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/session", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new Gson().fromJson(httpResponse.body(), LoginResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Welcome %s!%n%n", result.username());
            return result.authToken();
        } else if (httpResponse.statusCode() == 401) {
            throw new Exception("Wrong username or password.");
        } else {
            throw new Exception("Failed to log in.%n%n");
        }
    }

    public String register(RegisterRequest req) throws Exception {
        var json = new Gson().toJson(req);
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/user", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new Gson().fromJson(httpResponse.body(), LoginResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Welcome %s!%n%n", result.username());
            return result.authToken();
        } else {
            throw new Exception("Failed to register.%n%n");
        }
    }

    public void logout(String token) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/session", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Successfully logged out.%n%n");
        } else {
            throw new Exception("Failed to logout.%n%n");
        }
    }

    public static void clear(String host, int port) {
        try {
            String urlString = String.format(Locale.getDefault(), "http://%s:%d/db", host, port);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .DELETE()
                    .build();

            HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                System.out.printf("Successfully wiped database.%n%n");
            } else {
                System.out.printf("Failed to wipe database.%n%n");
            }
        } catch(Exception e) {
            System.out.println("Failed to wipe database." + e.getMessage());
        }
    }

    public int makeGame(String gameName, String token) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/game", host, port);
        var json = new Gson().toJson(new GameName(gameName));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        var result = httpResponse.body();
        var gameID = new Gson().fromJson(result, CreateResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Successfully added game.%n%n");
        } else {
            throw new Exception("Failed to logout.%n%n");
        }
        return gameID.gameID();
    }

    public GameList list(String token) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/game", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                .GET()
                .build();

        HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());


        if (httpResponse.statusCode() == 200) {
            return new Gson().fromJson(httpResponse.body(), GameList.class);
        } else {
            throw new Exception("Failed to list games.%n%n");
        }
    }

    public void joinGame(int gameID, ChessGame.TeamColor color, String token) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/game", host, port);
        var json = new Gson().toJson(new JoinRequest(gameID, color));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Joined game successfully.%n%n");
        } else {
            throw new Exception("Failed to join.%n%n");
        }
    }
}
