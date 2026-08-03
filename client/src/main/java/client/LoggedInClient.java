package client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Locale;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class LoggedInClient {
    private final int token;

    public LoggedInClient(int token) {
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

                    try {
                        String urlString = String.format(Locale.getDefault(), "http://%s:%d/session", host, port);

                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI(urlString))
                                .timeout(java.time.Duration.ofMillis(5000))
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                .build();
                        if (get(host, 8080)) {

                        }
                    } catch (Exception e) {
                        System.out.printf("Something went wrong; try again.%n%n");
                    }
                    //login
                    /*if (login successful) {
                        postLoginLoop();
                    }*/
                }
            } else if (args[0].equals("c") || args[0].equals("create")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a game name.%n%n");
                } else {
                    String gameName = args[1];
                    //create game
                }
            } else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
        }
    }
}
