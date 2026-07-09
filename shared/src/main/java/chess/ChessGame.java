package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.Iterator;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        turn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    public ChessGame(ChessGame copy) {
        turn = copy.turn;
        board = new ChessBoard(copy.board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Iterator<ChessMove> iter = moves.iterator();
        while (iter.hasNext()) {
            ChessMove move = iter.next();
            var test = new ChessGame(this);
            test.tryMove(move);
            if (test.isInCheck(turn)) {
                iter.remove();
            }
        }
        return moves;
    }

    public void tryMove(ChessMove move) {
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        } else {
            ChessPiece.PieceType type = move.getPromotionPiece();
            TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
            board.addPiece(move.getEndPosition(), new ChessPiece(color, type));
        }
        board.removePiece(move.getStartPosition());
        if (turn == TeamColor.WHITE) {
            turn = TeamColor.BLACK;
        } else {
            turn = TeamColor.WHITE;
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */


    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("no piece there");
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != turn) {
            throw new InvalidMoveException("not your turn");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("invalid move");
        }
        tryMove(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition ownKing;
        if (teamColor == TeamColor.WHITE) {
            ownKing = board.whiteKingSquare;
        } else {
            ownKing = board.blackKingSquare;
        }
        for (var entry : board.pieces.entrySet()) {
            if (entry.getValue().getTeamColor() != teamColor) {
                for (var move : entry.getValue().pieceMoves(board, entry.getKey())) {
                    if (move.getEndPosition().equals(ownKing)) {
                        return true;
                    }
                }
            }
        }
        return false;
//        throw new RuntimeException("Not implemented");
    }

    public int countMoves(TeamColor team) {
        int moves = 0;
        for (var square : board.pieces.keySet()) {
            if (board.pieces.get(square).getTeamColor() == team) {
                for (var move : validMoves(square)) {
                    moves++;
                }
            }
        }
        return moves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && countMoves(teamColor) == 0;
        /*throw new RuntimeException("Not implemented");*/
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && countMoves(teamColor) == 0;
        /*throw new RuntimeException("Not implemented");*/
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ChessGame other = (ChessGame) o;
        return turn == other.turn && board.equals(other.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}
