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

    /*private void movePawn(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, ChessPosition moveTo, int row) {
        if (board.getPiece(moveTo) == null) {
            if (row == 1 || row == 8) {
                for (var piece : PieceType.values()) {
                    moves.add(new ChessMove(myPosition, inFront, piece));
                }
            } else {
                moves.add(new ChessMove(myPosition, inFront));
            }
        }
    }*/

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

        } else if (type == PieceType.QUEEN) {
            for (var direction : ChessPosition.getKingMoves()) {
                int row = myPosition.getRow() + direction[0];
                int col = myPosition.getColumn() + direction[1];
                while (row < 9 && row > 0 && col < 9 && col > 0) {
                    ChessPosition dest = new ChessPosition(row, col);
                    if (board.getPiece(dest) == null) {
                        moves.add(new ChessMove(myPosition, dest));
                        row = row + direction[0];
                        col = col + direction[1];
                    } else if (board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                        break;
                    } else {
                        break;
                    }
                }
            }

        } else if (type == PieceType.ROOK) {
            for (var direction : ChessPosition.getRookMoves()) {
                int row = myPosition.getRow() + direction[0];
                int col = myPosition.getColumn() + direction[1];
                while (row < 9 && row > 0 && col < 9 && col > 0) {
                    ChessPosition dest = new ChessPosition(row, col);
                    if (board.getPiece(dest) == null) {
                        moves.add(new ChessMove(myPosition, dest));
                        row = row + direction[0];
                        col = col + direction[1];
                    } else if (board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                        break;
                    } else {
                        break;
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
            int doubleMoveRow = newRow + rowChange;
            boolean promotionTime = newRow == 1 || newRow == 8;
            ChessPosition inFront = new ChessPosition(newRow, myPosition.getColumn());
            ChessPosition doubleStart = new ChessPosition(doubleMoveRow, myPosition.getColumn());
            ChessPosition diag1 = new ChessPosition(newRow, myPosition.getColumn() + 1);
            ChessPosition diag2 = new ChessPosition(newRow, myPosition.getColumn() - 1);

            boolean canGoForward = board.getPiece(inFront) == null;
            boolean canGoDouble = (rowChange > 0 && myPosition.getRow() == 2 && canGoForward
                    && board.getPiece(doubleStart) == null) || (rowChange < 0 && myPosition.getRow() == 7 && canGoForward
                    && board.getPiece(doubleStart) == null);
            boolean canTake1 = board.getPiece(diag1) != null && board.getPiece(diag1).getTeamColor() != color;
            boolean canTake2 = board.getPiece(diag2) != null && board.getPiece(diag2).getTeamColor() != color;
            if (promotionTime) {
                if (canGoForward) {
                    for (var piece : PieceType.values()) {
                        if (piece != PieceType.PAWN && piece != PieceType.KING) {
                            moves.add(new ChessMove(myPosition, inFront, piece));
                        }
                    }
                }
                if (canTake1) {
                    for (var piece : PieceType.values()) {
                        if (piece != PieceType.PAWN && piece != PieceType.KING) {
                            moves.add(new ChessMove(myPosition, diag1, piece));
                        }
                    }
                }
                if (canTake2) {
                    for (var piece : PieceType.values()) {
                        if (piece != PieceType.PAWN && piece != PieceType.KING) {
                            moves.add(new ChessMove(myPosition, diag2, piece));
                        }
                    }
                }
            } else {
                if (canGoForward) {
                    moves.add(new ChessMove(myPosition, inFront));
                }
                if (canTake1) {
                    moves.add(new ChessMove(myPosition, diag1));
                }
                if (canTake2) {
                    moves.add(new ChessMove(myPosition, diag2));
                }
                if (canGoDouble) {
                    moves.add(new ChessMove(myPosition, doubleStart));
                }
            }
        }
        return moves;
    }

    private final ChessGame.TeamColor color;
    private final PieceType type;
}
