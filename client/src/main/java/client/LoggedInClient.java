package client;

import com.google.gson.Gson;
import chess.ChessGame.TeamColor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class LoggedInClient {
    private final String token;
    private final String host;
    private final int port;
    private final HttpClient client;
    private HashMap<Integer, Integer> gameIDs;

    public LoggedInClient(String token, String host, int port, HttpClient client) {
        this.token = token;
        this.host = host;
        this.port = port;
        this.client = client;
        gameIDs = new HashMap<>();
    }

    private static void listOptionsPostLogin() {
        String message = """
                Options:
                Logout: "lo", "logout"
                Create game: "c", "create" <GAME_NAME>
                List existing games: "l", "list"
                Play game: "p", "play" <GAME_NUMBER> <COLOR> (specify "w"/"white" or "b"/"black")
                Observe game: "o", "observe" <GAME_NUMBER>
                Print this message: "h", "help"
                """;
        System.out.println(message);
    }

    public void postLoginLoop() {
        while (true) {
            System.out.printf("What do you want to do?%n>>> ");
            var scanner = new Scanner(System.in);
            var args = scanner.nextLine().split(" ");
            if (args[0].equals("h") || args[0].equals("help")) {
                listOptionsPostLogin();
            } else if (args[0].equals("lo") || args[0].equals("logout")) {
                try {
                    logout();
                    break;
                } catch (Exception e) {
                    System.out.printf(e.getMessage());
                }
            } else if (args[0].equals("p") || args[0].equals("play")) {
                if (args.length < 3) {
                    System.out.printf("You need to provide a game number and a color to play.%n%n");
                } else if (args[2].equals("w") || args[2].equals("white")) {
                    try {
                        joinGame(parseInt(args[1]), TeamColor.WHITE);
                        //go to gameplay loop
                    } catch(Exception e) {
                        System.out.printf(e.getMessage());
                    }
                } else if (args[2].equals("b") || args[2].equals("black")) {
                    try {
                        joinGame(parseInt(args[1]), TeamColor.BLACK);
                        //go to gameplay loop
                    } catch(Exception e) {
                        System.out.printf(e.getMessage());
                    }
                } else {
                    System.out.printf("That's not a valid color.%n%n");


                    //join game
                    /*if (successful) {
                        gameplayLoop();
                    }*/
                }
            } else if (args[0].equals("c") || args[0].equals("create")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game name.%n%n");
                } else {
                    try {
                        makeGame(args[1]);
                    } catch (Exception e) {
                        System.out.printf(e.getMessage());
                    }
                }
            } else if (args[0].equals("o") || args[0].equals("observe")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game number.%n%n");
                } else {
                    int gameNumber = parseInt(args[1]);
                    //join game as spectator
                }
            } else if (args[0].equals("l") || args[0].equals("list")) {
                try {
                    printList(list());
                } catch(Exception e) {
                    System.out.printf(e.getMessage());
                }
            } else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
        }
    }

    public void logout() throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/session", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                //.timeout(java.time.Duration.ofMillis(5000))
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Successfully logged out.%n%n");
        } else {
            throw new Exception("Failed to logout.%n%n");
        }
    }

    public GameList list() throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/game", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                //.timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());


        if (httpResponse.statusCode() == 200) {
            return new Gson().fromJson(httpResponse.body(), GameList.class);
        } else {
            throw new Exception("Failed to list games.%n%n");
        }
    }

    public void printList(GameList list) {
        var games = list.games();
        for (int i = 0; i < games.size(); i++) {
            var curr = games.get(i);
            gameIDs.put(i+1, curr.gameID());
            System.out.printf("%d. Game: %s | White: %s | Black: %s%n", i+1, curr.gameName(), curr.whiteUsername(), curr.blackUsername());
        }
        System.out.printf("%n");
    }

    public void makeGame(String gameName) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/game", host, port);
        var json = new Gson().toJson(new GameName(gameName));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                //.timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = httpResponse.body();
        var gameID = new Gson().fromJson(result, CreateResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Successfully added game.%n%n");
        } else {
            throw new Exception("Failed to logout.%n%n");
        }
    }

    public void joinGame(int gameNumber, TeamColor color) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/game", host, port);
        int gameID = gameIDs.get(gameNumber);
        var json = new Gson().toJson(new JoinRequest(gameID, color));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .header("authorization", token)
                //.timeout(java.time.Duration.ofMillis(5000))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Joined game successfully.%n%n");
        } else {
            throw new Exception("Failed to join.%n%n");
        }
    }
}
