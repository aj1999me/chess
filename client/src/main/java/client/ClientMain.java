package client;

import chess.*;
import java.net.URI;
import java.net.http.*;
import java.util.Scanner;
import java.util.Locale;
import com.google.gson.Gson;

import static java.lang.Integer.parseInt;

public class ClientMain {
    private final String host;
    private final int port;
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

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
                        enterLoginLoop(login(req));
                    } catch (Exception e) {
                        System.out.printf("Something went wrong; try again.%n%n");
                    }
                }
            } else if (args[0].equals("r") || args[0].equals("register")) {
                if (args.length < 4) {
                    System.out.printf("You need to provide a username, password and email to register.%n%n");
                } else {
                    var req = new RegisterRequest(args[1], args[2], args[3]);
                    try{
                        enterLoginLoop(register(req));
                    } catch (Exception e) {
                        System.out.printf(e.getMessage());
                    }
                }
            } else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
        }
    }

    public static void main(String[] args) {
        System.out.printf("Welcome to the best chess game implementation you've ever played!%n%nType 'help' if you need the available commands.%n");
        new ClientMain(args[1], parseInt(args[2])).preLoginLoop();
    }

    public String login(LoginRequest req) throws Exception {
        var json = new Gson().toJson(req);
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/session", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                //.timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new Gson().fromJson(httpResponse.body(), LoginResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Welcome %s!%n%n", result.username());
            return result.authToken();
        } else {
            throw new Exception("Failed to log in.%n%n");
        }
    }

    public String register(RegisterRequest req) throws Exception {
        var json = new Gson().toJson(req);
        String urlString = String.format(Locale.getDefault(), "http://%s:%d/user", host, port);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                //.timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new Gson().fromJson(httpResponse.body(), LoginResult.class);

        if (httpResponse.statusCode() == 200) {
            System.out.printf("Welcome %s!%n%n", result.username());
            return result.authToken();
        } else {
            throw new Exception("Failed to register.%n%n");
        }
    }

    public static void clear(String host, int port) {
        try {
            String urlString = String.format(Locale.getDefault(), "http://%s:%d/db", host, port);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    //.timeout(java.time.Duration.ofMillis(5000))
                    .DELETE()
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                System.out.printf("Successfully wiped database.%n%n");
            } else {
                System.out.printf("Failed to wipe database.%n%n");
            }
        } catch(Exception e) {
            System.out.println("Failed to wipe database." + e.getMessage());
        }
    }

    public void enterLoginLoop(String token) {
        new LoggedInClient(token, host, port, httpClient).postLoginLoop();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
