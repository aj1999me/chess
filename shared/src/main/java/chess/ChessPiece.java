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
            if (!myPosition.rightEdge()) {
                moves.add(new ChessMove(myPosition, myPosition.moveRight()));
            }
            if (!myPosition.leftEdge()) {
                moves.add(new ChessMove(myPosition, myPosition.moveLeft()));
            }
            if (!myPosition.topEdge()) {
                moves.add(new ChessMove(myPosition, myPosition.moveForward()));
                if (!myPosition.leftEdge()) {
                    moves.add(new ChessMove(myPosition, myPosition.moveForwardLeft()));
                }
                if (!myPosition.rightEdge()) {
                    moves.add(new ChessMove(myPosition, myPosition.moveForwardRight()));
                }
            }
            if (!myPosition.bottomEdge()) {
                moves.add(new ChessMove(myPosition, myPosition.moveBack()));
                if (!myPosition.leftEdge()) {
                    moves.add(new ChessMove(myPosition, myPosition.moveBackLeft()));
                }
                if (!myPosition.rightEdge()) {
                    moves.add(new ChessMove(myPosition, myPosition.moveBackRight()));
                }
            }
        }
        return moves;
    }

    private final ChessGame.TeamColor color;
    private final PieceType type;
}
