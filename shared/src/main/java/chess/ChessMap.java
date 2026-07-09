package chess;
import java.util.HashMap;

public class ChessMap extends HashMap<ChessPosition, ChessPiece> {
    @Override
    public ChessMap clone() {
        var copy = new ChessMap();
        for (var key : this.keySet()) {
            copy.put(new ChessPosition(key), new ChessPiece(this.get(key)));
        }
        return copy;
    }
}
