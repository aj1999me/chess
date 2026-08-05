package client;

import chess.ChessGame.TeamColor;
import java.util.HashMap;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class LoggedInClient {
    private final String token;
    private final String host;
    private final int port;
    private HashMap<Integer, Integer> gameIDs;

    public LoggedInClient(String token, String host, int port) {
        this.token = token;
        this.host = host;
        this.port = port;
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
                    new ServerFacade(host, port).logout(token);
                    break;
                } catch (Exception e) {
                    System.out.printf(e.getMessage());
                }
            } else if (args[0].equals("p") || args[0].equals("play")) {
                if (args.length < 3) {
                    System.out.printf("You need to provide a game number and a color to play.%n%n");
                } else if (args[2].equals("w") || args[2].equals("white")) {
                    int gameID = gameIDs.get(parseInt(args[1]));
                    try {
                        new ServerFacade(host, port).joinGame(gameID, TeamColor.WHITE, token);
                        new DrawBoard(false);
                        //go to gameplay loop
                    } catch(Exception e) {
                        System.out.printf(e.getMessage());
                    }
                } else if (args[2].equals("b") || args[2].equals("black")) {
                    int gameID = gameIDs.get(parseInt(args[1]));
                    try {
                        new ServerFacade(host, port).joinGame(gameID, TeamColor.BLACK, token);
                        new DrawBoard(true);
                        //go to gameplay loop
                    } catch(Exception e) {
                        System.out.printf(e.getMessage());
                    }
                } else {
                    System.out.printf("That's not a valid color.%n%n");
                }
            } else if (args[0].equals("c") || args[0].equals("create")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game name.%n%n");
                } else {
                    try {
                        new ServerFacade(host, port).makeGame(args[1], token);
                    } catch (Exception e) {
                        System.out.printf(e.getMessage());
                    }
                }
            } else if (args[0].equals("o") || args[0].equals("observe")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game number.%n%n");
                } else {
                    int gameNumber = parseInt(args[1]);
                    new DrawBoard(false);
                    //join game as spectator
                }
            } else if (args[0].equals("l") || args[0].equals("list")) {
                try {
                    printList(new ServerFacade(host, port).list(token));
                } catch(Exception e) {
                    System.out.printf(e.getMessage());
                }
            } else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
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
}
