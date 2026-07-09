package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType type;
    private ChessGame.TeamColor color;
    private static final int[][] kingMoves = {{1,-1},  {1,0},  {1,1},
            {0,-1},          {0,1},
            {-1,-1}, {-1,0}, {-1,1}};
    private static final int[][] rookMoves = {{1,0}, {0,1}, {-1,0}, {0,-1}};
    private static final int[][] bishopMoves = {{1,1}, {1,-1}, {-1,-1}, {-1,1}};
    private static final int[][] knightMoves = {{1,2}, {2,1}, {2,-1},
            {1,-2}, {-1,-2}, {-2,-1}, {-2,1}, {-1,2}};

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        color = pieceColor;
    }

    public ChessPiece(int color, int type) {
        if (color == 0) {
            this.color = ChessGame.TeamColor.WHITE;
        } else if (color == 1) {
            this.color = ChessGame.TeamColor.BLACK;
        }
        if (type == 0) {
            this.type = ChessPiece.PieceType.KING;
        } else if (type == 1) {
            this.type = ChessPiece.PieceType.QUEEN;
        } else if (type == 2) {
            this.type = ChessPiece.PieceType.ROOK;
        } else if (type == 3) {
            this.type = ChessPiece.PieceType.BISHOP;
        } else if (type == 4) {
            this.type = ChessPiece.PieceType.KNIGHT;
        } else if (type == 5) {
            this.type = ChessPiece.PieceType.PAWN;
        }
    }

    public ChessPiece(ChessPiece copy) {
        type = copy.type;
        color = copy.color;
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
            for (var dir : kingMoves) {
                int row = myPosition.getRow() + dir[0];
                int col = myPosition.getColumn() + dir[1];
                ChessPosition dest = new ChessPosition(row, col);
                if (row > 0 && row < 9 && col > 0 && col < 9 && (board.getPiece(dest) == null || board.getPiece(dest).getTeamColor() != color)) {
                    moves.add(new ChessMove(myPosition, dest));
                }
            }
        } else if (type == PieceType.KNIGHT) {
            for (var dir : knightMoves) {
                int row = myPosition.getRow() + dir[0];
                int col = myPosition.getColumn() + dir[1];
                ChessPosition dest = new ChessPosition(row, col);
                if (row > 0 && row < 9 && col > 0 && col < 9 && (board.getPiece(dest) == null || board.getPiece(dest).getTeamColor() != color)) {
                    moves.add(new ChessMove(myPosition, dest));
                }
            }
        } else if (type == PieceType.QUEEN) {
            for (var dir : kingMoves) {
                int row = myPosition.getRow() + dir[0];
                int col = myPosition.getColumn() + dir[1];
                ChessPosition dest = new ChessPosition(row, col);
                while (row > 0 && row < 9 && col > 0 && col < 9) {
                    if (board.getPiece(dest) == null) {
                        moves.add(new ChessMove(myPosition, dest));
                        row += dir[0];
                        col += dir[1];
                        dest = new ChessPosition(row, col);
                    } else if (board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                        break;
                    } else {
                        break;
                    }

                }
            }
        } else if (type == PieceType.ROOK) {
            for (var dir : rookMoves) {
                int row = myPosition.getRow() + dir[0];
                int col = myPosition.getColumn() + dir[1];
                ChessPosition dest = new ChessPosition(row, col);
                while (row > 0 && row < 9 && col > 0 && col < 9) {
                    if (board.getPiece(dest) == null) {
                        moves.add(new ChessMove(myPosition, dest));
                        row += dir[0];
                        col += dir[1];
                        dest = new ChessPosition(row, col);
                    } else if (board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                        break;
                    } else {
                        break;
                    }

                }
            }
        } else if (type == PieceType.BISHOP) {
            for (var dir : bishopMoves) {
                int row = myPosition.getRow() + dir[0];
                int col = myPosition.getColumn() + dir[1];
                ChessPosition dest = new ChessPosition(row, col);
                while (row > 0 && row < 9 && col > 0 && col < 9) {
                    if (board.getPiece(dest) == null) {
                        moves.add(new ChessMove(myPosition, dest));
                        row += dir[0];
                        col += dir[1];
                        dest = new ChessPosition(row, col);
                    } else if (board.getPiece(dest).getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, dest));
                        break;
                    } else {
                        break;
                    }

                }
            }
        } else if (type == PieceType.PAWN) {
            int moveDir;
            if (color == ChessGame.TeamColor.WHITE) {
                moveDir = 1;
            } else {
                moveDir = -1;
            }
            int newRow = myPosition.getRow() + moveDir;
            int col = myPosition.getColumn();
            ChessPosition forward = new ChessPosition(newRow, col);
            ChessPosition doubleForward = new ChessPosition(newRow + moveDir, col);
            ChessPosition takeLeft = new ChessPosition(newRow, col-1);
            ChessPosition takeRight = new ChessPosition(newRow, col+1);
            boolean starting = color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2 ||
                    color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7;
            boolean promTime;
            promTime = color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7 ||
                    color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2;
            boolean canGoForward = board.getPiece(forward) == null;
            if (canGoForward) {
                if (promTime) { // move forward and promote
                    for (var piece : PieceType.values()) {
                        if (piece != PieceType.KING && piece != PieceType.PAWN) {
                            moves.add(new ChessMove(myPosition, forward, piece));
                        }
                    }
                } else if (starting && board.getPiece(doubleForward) == null) { // move two squares
                    moves.add(new ChessMove(myPosition, forward));
                    moves.add(new ChessMove(myPosition, doubleForward));
                } else {
                    moves.add(new ChessMove(myPosition, forward)); // move one square
                }
            }
            if (board.getPiece(takeLeft) != null && board.getPiece(takeLeft).getTeamColor() != color) {
                if (promTime) {
                    for (var piece : PieceType.values()) {
                        if (piece != PieceType.KING && piece != PieceType.PAWN) {
                            moves.add(new ChessMove(myPosition, takeLeft, piece));
                        }
                    }
                } else {
                    moves.add(new ChessMove(myPosition, takeLeft));
                }
            }
            if (board.getPiece(takeRight) != null && board.getPiece(takeRight).getTeamColor() != color) {
                if (promTime) {
                    for (var piece : PieceType.values()) {
                        if (piece != PieceType.KING && piece != PieceType.PAWN) {
                            moves.add(new ChessMove(myPosition, takeRight, piece));
                        }
                    }
                } else {
                    moves.add(new ChessMove(myPosition, takeRight));
                }
            }

        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ChessPiece other = (ChessPiece) o;
        return type == other.type && color == other.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
