package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

import chess.*;

public class DrawBoard {
    private static String[] columnMarks = {"  \u2003a   ", "  \u2003b   ", "  \u2003c   ", "  \u2003d   ",
            "  \u2003e   ", "  \u2003f   ", "  \u2003g   ", "  \u2003h   "};
    private static String[] rowMarks = {"  \u20031   ", "  \u20032   ", "  \u20033   ", "  \u20034   ",
            "  \u20035   ", "  \u20036   ", "  \u20037   ", "  \u20038   "};
    private ChessBoard board;
    private final boolean flipped;
    PrintStream out;
    private HashSet<ChessPosition> squares;

    public DrawBoard(boolean white, ChessGame game) {
        this.flipped = !white;
        board = game.getBoard();
        squares = new HashSet<ChessPosition>();
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawColumnHeaders();
        drawRows();
        drawColumnHeaders();
        resetColors();
    }

    public DrawBoard(boolean white, ChessGame game, ChessPosition pos) {
        this.flipped = !white;
        board = game.getBoard();
        squares = game.getHighlightSet(pos);
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawColumnHeaders();
        drawRows();
        drawColumnHeaders();
        resetColors();
    }

    public static void main(String[] args) {
        new DrawBoard(true, new ChessGame());
    }

    public void resetColors() {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    public void drawColumnHeaders() {
        printGrayRow();
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        drawColumnMarks();
        out.print(EMPTY);
        endLine();
        printGrayRow();
    }

    public void drawColumnMarks() {
        if (flipped) {
            for (int i = 7; i >= 0; i--) {
                out.print(columnMarks[i]);
            }
        } else {
            for (var str : columnMarks) {
                out.print(str);
            }
        }
    }

    public void drawRowMark(int i) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(rowMarks[i]);
    }

    public void printGrayRow() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EMPTY.repeat(10));
        endLine();
    }

    public void drawRows() {
        for (int i = 0; i < 8; ++i) {
            if (flipped) {
                drawRow(i);
            } else {
                drawRow(7 - i);
            }
        }
    }

    public void drawRow(int i) {
        int actualRow = i + 1;
        drawEmptyRow(actualRow);
        drawRowMark(i);
        drawSquares(actualRow, false);
        drawRowMark(i);
        endLine();
        drawEmptyRow(actualRow);
    }

    public void drawEmptyRow(int actualRow) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EMPTY);
        drawSquares(actualRow, true);
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EMPTY);
        endLine();
    }

    public void endLine() {
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    public void drawSquares(int actualRow, boolean skipPieces) {
        int actualCol;
        for (int j = 0; j < 8; ++j) {
            if (flipped) {
                actualCol = 8 - j;
            } else {
                actualCol = j + 1;
            }
            boolean white = (actualRow + actualCol) % 2 != 0;
            if (squares.contains(new ChessPosition(actualRow, actualCol))) {
                if (white) {
                    out.print(SET_BG_COLOR_YELLOW);
                } else {
                    out.print(SET_BG_COLOR_DARK_YELLOW);
                }
            } else {
                if (white) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
            }
            var piece = board.getPiece(new ChessPosition(actualRow, actualCol));
            if (skipPieces || piece == null) {
                out.print(EMPTY);
            } else {
                printPiece(piece);
            }
        }
    }

    public void printPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLUE);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                out.print(WHITE_KING);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                out.print(WHITE_QUEEN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                out.print(WHITE_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                out.print(WHITE_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                out.print(WHITE_ROOK);
            } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                out.print(WHITE_PAWN);
            }
        } else {
            out.print(SET_TEXT_COLOR_RED);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                out.print(BLACK_KING);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                out.print(BLACK_QUEEN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                out.print(BLACK_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                out.print(BLACK_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                out.print(BLACK_ROOK);
            } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                out.print(BLACK_PAWN);
            }
        }
    }
}
