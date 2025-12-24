package board.position.helper;

import board.position.state.*;
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
                        CastlingRights.addRight(state.castlingRights, 1);
                        break;
                    case 'Q':
                        CastlingRights.addRight(state.castlingRights, 2);
                        break;
                    case 'k':
                        CastlingRights.addRight(state.castlingRights, 4);
                        break;
                    case 'q':
                        CastlingRights.addRight(state.castlingRights, 8);
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
