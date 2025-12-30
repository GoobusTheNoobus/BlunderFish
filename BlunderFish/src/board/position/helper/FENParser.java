/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.helper;

import board.position.state.Board;
import board.position.state.GameState;

public class FENParser {
    public static void loadFEN (String fen, Board board, GameState state) {
        // Just in case I change Piece.NONE constant to something other than 0
        board.clear();

        String[] parts = fen.split(" ");

        String boardPart = parts[0];
        String sideToMovePart = parts[1];
        String castlingPart = parts[2];
        String enPassantPart = parts[3];
        String fiftyMovePart = parts[4];

        BoardParser.parseBoard(board, boardPart);
        StateParser.parseSideToMove(state, sideToMovePart);
        StateParser.parseCastling(state, castlingPart);
        StateParser.parseEnPassant(state, enPassantPart);
        
        // 50 Move Clock
        state.fiftyMoveClock = Integer.parseInt(fiftyMovePart);


        
    }
    
}
