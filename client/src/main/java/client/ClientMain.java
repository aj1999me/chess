package client;

import chess.*;
import java.net.http.*;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class ClientMain {
    private final String host;
    private final int port;

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
                } else if (args.length > 3) {
                    System.out.printf("You only need a username and a password.%n%n");
                } else {
                    var req = new LoginRequest(args[1], args[2]);
                    try{
                        enterLoginLoop(new ServerFacade(host, port).login(req));
                    } catch (Exception e) {
                        System.out.printf(e.getMessage());
                    }
                }
            } else if (args[0].equals("r") || args[0].equals("register")) {
                if (args.length < 4) {
                    System.out.printf("You need to provide a username, password and email to register.%n%n");
                } else if (args.length > 4) {
                    System.out.printf("You need to provide a username, password and email to register.%n%n");

                } else {
                    var req = new RegisterRequest(args[1], args[2], args[3]);
                    try{
                        enterLoginLoop(new ServerFacade(host, port).register(req));
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
        new ClientMain(args[0], parseInt(args[1])).preLoginLoop();
    }

    public void enterLoginLoop(String token) {
        new LoggedInClient(token, host, port).postLoginLoop();
    }
}
