package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    private final int col;
    private final int row;

    private final static int[][] KnightMoves = {{2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}};

    public static int[][] getKnightMoves() {
        return KnightMoves;
    }

    private final static int[][] KingMoves = {{1,0}, {0,1}, {-1,0}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};

    public static int[][] getKingMoves() {
        return KingMoves;
    }

    private final static int[][] RookMoves = {{1,0}, {0,1}, {-1,0}, {0,-1}};

    public static int[][] getRookMoves() {
        return RookMoves;
    }

    private final static int[][] BishopMoves = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};

    public static int[][] getBishopMoves() {
        return BishopMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

}
