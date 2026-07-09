package chess;
import java.util.HashMap;

public class ChessMap extends HashMap<ChessPosition, ChessMove> {
    @Override
    public ChessMap clone() throws CloneNotSupportedException {
        var copy = new ChessMap();

    }
}
