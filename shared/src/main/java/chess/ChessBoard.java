package chess;

import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    protected ChessMap pieces = new ChessMap();
    protected ChessPosition whiteKingSquare;
    protected ChessPosition blackKingSquare;
    private final static int[][] defaultBoard = {/*white king*/{1,5,0,0},
            /*black king*/{8,5,1,0}, /*white queen*/{1,4,0,1},
            /*black queen*/{8,4,1,1}, /*white rooks*/{1,1,0,2}, {1,8,0,2},
            /*black rooks*/{8,1,1,2}, {8,8,1,2}, /*white bishops*/{1,3,0,3},
            {1,6,0,3}, /*black bishops*/{8,3,1,3}, {8,6,1,3},
            /*white knights*/{1,2,0,4}, {1,7,0,4}, /*black knights*/
            {8,2,1,4}, {8,7,1,4}, /*white pawns*/{2,1,0,5}, {2,2,0,5},
            {2,3,0,5}, {2,4,0,5}, {2,5,0,5}, {2,6,0,5}, {2,7,0,5},
            {2,8,0,5}, /*black pawns*/{7,1,1,5}, {7,2,1,5},
            {7,3,1,5}, {7,4,1,5}, {7,5,1,5}, {7,6,1,5}, {7,7,1,5},
            {7,8,1,5}};

    public ChessBoard() {

    }

    public ChessBoard(ChessBoard copy) {
        pieces = copy.pieces.clone();
        whiteKingSquare = new ChessPosition(copy.whiteKingSquare);
        blackKingSquare = new ChessPosition(copy.blackKingSquare);
    }

    /*public ChessBoard(ChessBoard copy) {

    }*/

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece == null) {
            return;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whiteKingSquare = position;
            } else {
                blackKingSquare = position;
            }
        }
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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void removePiece(ChessPosition position) {
        pieces.remove(position);
    }

    public void resetBoard() {
        for (var info : defaultBoard) {
            addPiece(new ChessPosition(info[0],info[1]), new ChessPiece(info[2], info[3]));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ChessBoard other = (ChessBoard) o;
        return pieces.equals(other.pieces) &&
                whiteKingSquare.equals(other.whiteKingSquare)
                && blackKingSquare.equals(other.blackKingSquare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces, whiteKingSquare, blackKingSquare);
    }
}
