package client;

import chess.ChessGame;

import java.util.Scanner;

public class GameplayLoop {
    private final String host;
    private final int port;
    private final String token;
    private final GameplayClient client;
    private final int ID;
    private final boolean WHITE;

    public GameplayLoop(String host, int port, String token, int ID, boolean WHITE) throws Exception {
        this.host = host;
        this.port = port;
        this.token = token;
        this.ID = ID;
        this.WHITE = WHITE;
        try {
            client = new GameplayClient(host, port, token, ID, WHITE);
            client.connect();
        } catch(Exception e) {
            throw new Exception("Failed to connect to server.%n%n");
        }
    }

    private static void listOptions() {
        String message = """
                Options:
                Leave game: "l", "leave"
                Redraw board: "r", "redraw"
                Make a move: "m", "move" <PIECE_LOCATION> <PIECE_DESTINATION>
                Resign: "rs", "resign"
                Highlight legal moves: "hl", "highlight" <PIECE_LOCATION>
                Print this message: "h", "help"
                """;
        System.out.println(message);
    }

    public void loop() {
        while (true) {
            System.out.printf("What do you want to do?%n>>> ");
            var scanner = new Scanner(System.in);
            var args = scanner.nextLine().split(" ");
            if (args[0].equals("h") || args[0].equals("help")) {
                listOptions();
            } else if (args[0].equals("l") || args[0].equals("leave")) {
                try {
                    client.leave();
                    break;
                } catch(Exception e) {
                    System.out.printf(e.getMessage());
                }
            } else if (args[0].equals("hl") || args[0].equals("highlight")) {
                if (args.length < 2) {
                    System.out.printf("You need to provide a piece to highlight.%n%n");
                } else if (args.length > 2) {
                    System.out.printf("You only need one piece location.%n%n");
                } else {
                    //highlight valid moves
                }
            } else if (args[0].equals("r") || args[0].equals("redraw")) {
                //refresh
            } else if (args[0].equals("rs") || args[0].equals("resign")) {
                //resign
            } else if (args[0].equals("m") || args[0].equals("move")) {
                if (args.length < 3) {
                    System.out.printf("You need to provide a piece location and destination square.%n%n");
                } else if (args.length > 3) {
                    System.out.printf("You only need a piece location and destination square.%n%n");
                } else {
                    //make move
                }
            } else {
                System.out.printf("Sorry, that input was invalid.%n%n");
            }
        }
    }
}
