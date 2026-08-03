package client;

import chess.*;
import java.net.URI;
import java.net.http.*;
import java.util.Scanner;
import java.util.Locale;
import static java.lang.Integer.parseInt;
import com.google.gson.Gson;

public class ClientMain {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static void listOptionsPreLogin() {
        String message = """
                Options:
                Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                Register new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                Exit the program: "q", "quit"
                Print this message: "h", "help"
                """;
        System.out.println(message);
    }

    private void preLoginLoop() {
        while (true) {
            System.out.printf("What do you want to do?%n>>> ");
            var scanner = new Scanner(System.in);
            var args = scanner.nextLine().split(" ");
            if (args[0].equals("h") || args[0].equals("help")) {
                listOptionsPreLogin();
            } else if (args[0].equals("q") || args[0].equals("quit")) {
                System.out.printf("Bye!%n");
                break;
            } else if (args[0].equals("l") || args[0].equals("login")) {
                if (args.length < 3) {
                    System.out.printf("You need to provide a username and a password to login.%n%n");
                } else {
                    var req = new LoginRequest(args[1], args[2]);

                    try{
                        login(host, port);
                    } catch (Exception e) {
                        System.out.printf("Something went wrong; try again.%n%n");
                    }
                }
            } else if (args[0].equals("r") || args[0].equals("register")) {
                if (args.length < 4) {
                    System.out.printf("You need to provide a username, password and email to register.%n%n");
                } else {
                    String username = args[1];
                    String password = args[2];
                    String email = args[3];
                    //register
                    /*if (register successful) {
                        postLoginLoop();
                    }*/
                }
            } else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
        }
    }

    public static void main(String[] args) {
        System.out.printf("Welcome to the best chess game implementation you've ever played!%n");
        listOptionsPreLogin();
        preLoginLoop();
    }

    private void login(String host, int port, LoginRequest req) throws Exception {
        var json = new Gson().toJson(req);
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/session", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new Gson().fromJson(httpResponse.body(), LoginResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Welcome %s!%n%n", result.username());
            new LoggedInClient(result.token()).postLoginLoop();
        }
    }
}
