package board.position.helper;

import board.position.state.*;
import utils.Constants;
import utils.Utility;;

public class StateParser {
    public static void parseSideToMove (GameState state, String str) {
        switch (str) {
            case "w":
                state.whiteToMove = true;
                break;
            case "b":
                state.whiteToMove = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid FEN character in side to move field: " + str);
        }
    }

    public static void parseCastling (GameState state, String str) {
        if (!(str.equals("-"))) {
            for (char i: str.toCharArray()) {
                switch (i) {
                    case 'K':
                        state.addRight(Constants.WHITE_KINGSIDE_CASTLING_MASK);
                        break;
                    case 'Q':
                        state.addRight(Constants.WHITE_QUEENSIDE_CASTLING_MASK);
                        break;
                    case 'k':
                        state.addRight(Constants.BLACK_KINGSIDE_CASTLING_MASK);
                        break;
                    case 'q':
                        state.addRight(Constants.BLACK_QUEENSIDE_CASTLING_MASK);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FEN character in castling rights field" + i);
                }
            }
        }
       
    }
    public static void parseEnPassant (GameState state, String str) {
        if (str.equals("-")) {
            state.enPassantSquare = 64;
        } else {
           state.enPassantSquare = Utility.getSquareIntFromString(str);
        }
    }
}
