package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        if (type == PieceType.KING) {
            for (var direction : ChessPosition.getKingMoves()) {
                int newRow = myPosition.getRow() + direction[0];
                int newCol = myPosition.getColumn() + direction[1];
                if (newRow > 0 && newRow < 9 && newCol > 0 && newCol < 9) {
                    ChessPosition dest = new ChessPosition(newRow, newCol);
                    if (board.getPiece(dest) == null || board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                    }
                }
            }

        } else if (type == PieceType.KNIGHT) {
            for (var direction : ChessPosition.getKnightMoves()) {
                int newRow = myPosition.getRow() + direction[0];
                int newCol = myPosition.getColumn() + direction[1];
                if (newRow > 0 && newRow < 9 && newCol > 0 && newCol < 9) {
                    ChessPosition dest = new ChessPosition(newRow, newCol);
                    if (board.getPiece(dest) == null || board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                    }
                }
            }
        } else if (type == PieceType.PAWN) {
            int rowChange;
            if (color == ChessGame.TeamColor.WHITE) {
                rowChange = 1;
            } else {
                rowChange = -1;
            }
            int newRow = myPosition.getRow() + rowChange;
            ChessPosition inFront = new ChessPosition(newRow, myPosition.getColumn());
            if (board.getPiece(inFront) == null) {
                if (newRow == 1 || newRow == 8) {
                    for (var piece : PieceType.values()) {
                        moves.add(new ChessMove(myPosition, inFront, piece));
                    }
                } else {
                    moves.add(new ChessMove(myPosition, inFront));
                }
            }
            if ()
        }
        return moves;
    }

    private final ChessGame.TeamColor color;
    private final PieceType type;
}
