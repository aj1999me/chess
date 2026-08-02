package client;

import chess.*;
import java.util.Scanner;

public class ClientMain {
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

    private static void preLoginLoop() {
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
                    String username = args[1];
                    String password = args[2];
                    //login
                    /*if (login successful) {
                        postLoginLoop();
                    }*/
                }
            } else if (args[0].equals("r") || args[0].equals("register")) {
                if (args.length < 4) {
                    System.out.printf("You need to provide a username and a password to login.%n%n");
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
}
