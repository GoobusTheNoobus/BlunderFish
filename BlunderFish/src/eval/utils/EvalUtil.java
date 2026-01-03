package eval.utils;

import board.position.Position;
import board.position.moves.MoveBuffer;

public class EvalUtil {

    // Status Codes for game status
    public static final int ONGOING = 2;
    public static final int WHITE_WIN = 10000;
    public static final int BLACK_WIN = -10000;
    public static final int DRAW_BY_STALEMATE = 0;


    /**
     * @param pos the position object
     * @param buff a pregenerated MoveBuffer, useful for only generating moves once per node
     * @return 67 if the game is on-going, 1 and -1 if checkmate, and 0 if a win
     */
    public static int currentGameStatus (Position pos) {

        // Expects buff to already by generated
        
            if (pos.isInCheck()) {
                return pos.gameState.whiteToMove ? BLACK_WIN : WHITE_WIN;
            } else {
                return DRAW_BY_STALEMATE;
            }
    }

    public static String evalIntToString (int eval) {
        if (Math.abs(eval) >= 100) {
            return (eval < 0 ? "-" : "+") + "Mx";
        }

        return (eval < 0 ? "-" : "+") + Math.abs(eval);
    }
}
