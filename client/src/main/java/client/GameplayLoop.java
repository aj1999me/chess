package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GameplayLoop {
    private final GameplayClient client;

    private static Map<String, Integer> columns = Map.of("a", 1, "b", 2,
            "c", 3, "d", 4, "e", 5, "f", 6, "g", 7, "h", 8);

    public GameplayLoop(String host, int port, String token, int id, boolean white) throws Exception {
        try {
            client = new GameplayClient(host, port, token, id, white);
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
            try {
                var scanner = new Scanner(System.in);
                var args = scanner.nextLine().split(" ");
                if (args[0].equals("h") || args[0].equals("help")) {
                    listOptions();
                } else if (args[0].equals("l") || args[0].equals("leave")) {
                    client.leave();
                    break;
                } else if (args[0].equals("hl") || args[0].equals("highlight")) {
                    if (args.length < 2) {
                        throw new Exception("You need to provide a piece to highlight.%n%n");
                    } else if (args.length > 2) {
                        throw new Exception("You only need one piece location.%n%n");
                    } else {
                        client.highlight(getSquare(args[1]));
                    }
                } else if (args[0].equals("r") || args[0].equals("redraw")) {
                    client.refresh();
                    System.out.printf("Something went wrong, try again.%n%n");
                } else if (args[0].equals("rs") || args[0].equals("resign")) {
                    System.out.printf("Do you really want to resign?%n");
                    var scanner1 = new Scanner(System.in);
                    var answer = scanner1.nextLine().split(" ")[0];
                    if (answer.equals("yes") || answer.equals("y")) {
                        client.resign();
                    }
                } else if (args[0].equals("m") || args[0].equals("move")) {
                    if (args.length < 3) {
                        throw new Exception("You need to provide a piece location and destination square, and a promotion type if applicable.%n%n");
                    } else if (args.length > 4) {
                        throw new Exception("You only need a piece location and destination square (and maybe a promotion type.)%n%n");
                    } else if (args.length == 4) {
                        client.makeMove(getMove(args[1], args[2], args[3]));
                    } else {
                        client.makeMove(getMove(args[1], args[2], null));
                    }
                } else {
                    System.out.printf("Sorry, that input was invalid.%n%n");
                }
            } catch(Exception e) {
                System.out.printf(e.getMessage());
            }
        }
    }

    public ChessPosition getSquare(String square) throws Exception {
        Integer col = columns.get(square.substring(0, 1));
        try {
            var row = new Integer(parseInt(square.substring(1)));
            if (col == null || row > 8 || row < 1) {
                throw new Exception("Invalid square%n%n");
            } else {
                return new ChessPosition(row, col);
            }
        } catch(Exception e) {
            throw new Exception("Invalid square%n%n");
        }


    }

    public ChessMove getMove(String loc, String dest, String prom) throws Exception {
        ChessPiece.PieceType type = null;
        if (prom != null) {
            if (prom.equals("q") || prom.equals("queen")) {
                type = ChessPiece.PieceType.QUEEN;
            } else if (prom.equals("b") || prom.equals("bishop")) {
                type = ChessPiece.PieceType.BISHOP;
            } else if (prom.equals("k") || prom.equals("knight")) {
                type = ChessPiece.PieceType.KNIGHT;
            } else if (prom.equals("r") || prom.equals("rook")) {
                type = ChessPiece.PieceType.ROOK;
            } else {
                throw new Exception("Promotion type is invalid.%n%n");
            }
        }
        return new ChessMove(getSquare(loc), getSquare(dest), type);
    }
}
