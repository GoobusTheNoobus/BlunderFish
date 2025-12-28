package board.position.helper;

import board.position.Piece;
import board.position.state.*;
import utils.Constants;
import utils.Utility;

public class PositionPrinter {
    public static void printBoardState (Board board) {
        System.out.println();
        for (int rank = 7; rank >= 0; rank--) {
            System.out.print((rank + 1) + " ");
            for (int file = 0; file < 8; file++) {
                switch (board.mailbox[rank << 3 | file]) {
                    case Piece.WP:
                        System.out.print(Constants.WHITE_PAWN_CHAR);
                        break;
                    case Piece.BP:
                        System.out.print(Constants.BLACK_PAWN_CHAR);
                        break;
                    case Piece.WN:
                        System.out.print(Constants.WHITE_KNIGHT_CHAR);
                        break;
                    case Piece.BN:
                        System.out.print(Constants.BLACK_KNIGHT_CHAR);
                        break;
                    case Piece.WB:
                        System.out.print(Constants.WHITE_BISHOP_CHAR);
                        break;
                    case Piece.BB:
                        System.out.print(Constants.BLACK_BISHOP_CHAR);
                        break;
                    case Piece.WR:
                        System.out.print(Constants.WHITE_ROOK_CHAR);
                        break;
                    case Piece.BR:
                        System.out.print(Constants.BLACK_ROOK_CHAR);
                        break;
                    case Piece.WQ:
                        System.out.print(Constants.WHITE_QUEEN_CHAR);
                        break;
                    case Piece.BQ:
                        System.out.print(Constants.BLACK_QUEEN_CHAR);
                        break;
                    case Piece.WK:
                        System.out.print(Constants.WHITE_KING_CHAR);
                        break;
                    case Piece.BK:
                        System.out.print(Constants.BLACK_KING_CHAR);
                        break;
                    case Piece.NONE:
                        System.out.print(".");
                        break;
                    default:
                        // Yeah about that
                        throw new IllegalStateException("Oh noeserz!!! it seems like this position has a bit of weird stuff in it, and therefore, putting me in a bit of a pickle. I love pickles by the way, its really good. if you disagree, i will eat you instead. anyways, basically, in this position's mailbox, there is a weird piece. it looks, like, uhhh, like an elephant. wait, there is an elephant in chess right? oh right wrong variants. elephants are w tho. in chess pretty sure its a bishop. idk, btw since im so nice, here is the piece: " + board.mailbox[rank << 3 | file]);
                    
                }
                System.out.print(" ");
            }
            
            System.out.println();
        }
        System.out.println("  a b c d e f g h\n");
    }

    public static void printGameState (GameState state) {
        // Side to move
        System.out.println("Side to move: " + (state.whiteToMove ? "White" : "Black" + "\n"));

        // Castling Rights

        if (state.hasRight(Constants.WHITE_KINGSIDE_CASTLING_MASK)) System.out.println("White Kingside Castling");

        if (state.hasRight(Constants.WHITE_QUEENSIDE_CASTLING_MASK)) System.out.println("White Queenside Castling");

        if (state.hasRight(Constants.BLACK_KINGSIDE_CASTLING_MASK)) System.out.println("Black Kingside Castling");

        if (state.hasRight(Constants.BLACK_QUEENSIDE_CASTLING_MASK)) System.out.println("Black Queenside Castling");

        if (state.castlingRights == 0) System.out.println("No Castling Rights");

        System.out.println('\n');

        // En passant square
        System.out.println("En Passant Square: " + ((state.enPassantSquare != 64) ? (Utility.getStringFromSquareInt(state.enPassantSquare)) : "None") + "\n");

        System.out.println("Fifty Move Clock: " + (int)(state.fiftyMoveClock / 2));

    }
}
