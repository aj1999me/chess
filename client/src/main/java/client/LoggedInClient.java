package client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Locale;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class LoggedInClient {
    private final String token;

    public LoggedInClient(String token) {
        this.token = token;
    }

    private static void listOptionsPostLogin() {
        String message = """
                Options:
                Logout: "lo", "logout"
                Create game: "c", "create" <GAME_NAME>
                List existing games: "l", "list"
                Play game: "p", "play" <GAME_NUMBER> <COLOR>
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
                //logout
                break;
            } else if (args[0].equals("p") || args[0].equals("play")) {
                if (args.length < 3) {
                    System.out.printf("You need to provide a game number and a color to play.%n%n");
                } else {
                    int gameNumber = parseInt(args[1]);
                    String color = args[2];

                    //join game
                    /*if (successful) {
                        gameplayLoop();
                    }*/
                }
            } else if (args[0].equals("c") || args[0].equals("create")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game name.%n%n");
                } else {
                    String gameName = args[1];
                    //create game
                }
            } else if (args[0].equals("o") || args[0].equals("observe")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game nnumber.%n%n");
                } else {
                    int gameNumber = parseInt(args[1]);
                    //join game as spectator
                }
            }else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
        }
    }
}
