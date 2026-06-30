package chess;

import java.util.HashMap;
import java.util.Objects;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public HashMap<ChessPosition, ChessPiece> pieces = new HashMap<>();

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces.put(position, piece);
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return pieces.get(position);
    }

    private static final int[][] whitePawns = {{2,1}, {2,2}, {2,3},
            {2,4}, {2,5}, {2,6}, {2,7}, {2,8}};
    private static final int[][] blackPawns = {{7,1}, {7,2}, {7,3}, {7,4},
            {7,5}, {7,6}, {7,7}, {7,8}};
    private static final int[][] whiteRooks = {{1,1}, {1,8}};
    private static final int[][] blackRooks = {{8,1}, {8,8}};
    private static final int[][] whiteKnights = {{1,2}, {1,7}};
    private static final int[][] blackKnights = {{8,2}, {8,7}};
    private static final int[][] whiteBishops = {{1,3}, {1,6}};
    private static final int[][] blackBishops = {{8,3}, {8,6}};

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        pieces.clear();
        for (var coordinates : whitePawns) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        } for (var coordinates : blackPawns) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        } for (var coordinates : whiteRooks) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        } for (var coordinates : blackRooks) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        } for (var coordinates : whiteKnights) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        } for (var coordinates : blackKnights) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        } for (var coordinates : whiteBishops) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        } for (var coordinates : blackBishops) {
            addPiece(new ChessPosition(coordinates[0], coordinates[1]),
                    new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        } addPiece(new ChessPosition(1,5),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1,4),
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,4),
                new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ChessBoard other = (ChessBoard) o;
        return pieces.equals(other.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }
}
