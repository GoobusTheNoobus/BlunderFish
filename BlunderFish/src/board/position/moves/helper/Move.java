/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves.helper;

import utils.Constants;
import utils.Utility;
import board.position.Piece;
import board.position.Position;

/*
     * Move encoding:
     * From Square 6 bits (0)
     * To Square 6 bits (6)
     * Piece Captured 4 bits (12)
     * Piece Promoted 4 bits (16)
     * Castling Rights Before (20)
     * Flags (24-26):
     * 0b0000 Normal
     * 0b0001 Castling Kingside
     * 0b0010 Castling Queenside
     * 0b0011 En Passant
     * 0b0100 Double Pawn Push
     * EnPassant Square (27-33)
*/
public class Move {
    public static long createMove (Position pos, int from, int to, int promotedPiece, boolean isCastling, boolean enPassant) {
        long move = 0;

        move |= from;
        move |= to << 6;
        move |= pos.pieceAt(to) << 12;
        move |= promotedPiece << 16;
        move |= pos.gameState.castlingRights << 20;

    
        
        if (isCastling) {
            if (Utility.parseFile(to) == 6)
                move |= Constants.CASTLING_KINGSIDE_FLAG << 24;
            if (Utility.parseFile(to) == 2) 
                move |= Constants.CASTLING_QUEENSIDE_FLAG << 24;
        }
        if (enPassant)
            move |= Constants.EN_PASSANT_FLAG << 24;

        if (((pos.pieceAt(from) == Piece.WP && to - from == 16) || (pos.pieceAt(from) == Piece.BP && from - to == 16)) && (pos.pieceAt(to) == Piece.NONE))
            move |= Constants.DOUBLE_PAWN_PUSH << 24;

       

        move = (((long)pos.gameState.enPassantSquare) << 27) | move;

        
        

        return move;
    }
    public static int getFromSquare (long move) {
        return ((int)(move & 63));
    }
    public static int getToSquare (long move) {
        return (((int)(move >>> 6) & 63));
    }
    public static int getCapturedPiece (long move) {
        return (((int)(move >>> 12) & 0b1111));
    }
    public static int getPromotedPiece (long move) {
        return (((int)(move >>> 16) & 0b1111));
    }
    public static int getCastlingRights (long move) {
        return (((int)(move >>> 20) & 0b1111));
    }
    public static int getFlag (long move) {
        return (((int)(move >>> 24) & 0b111));
    }
    public static int getPreviousEnPassantSquare (long move) {
        return (((int)(move >>> 27) & 0b1111111));
    }

    public static String toString(long move) {
        if (getFlag(move) == Constants.CASTLING_KINGSIDE_FLAG) return "O-O";
        if (getFlag(move) == Constants.CASTLING_QUEENSIDE_FLAG) return "O-O-O";

        return Utility.getStringFromSquareInt(getFromSquare(move)) + Utility.getStringFromSquareInt(getToSquare(move));
    }

    public static void main(String[] args) {
        System.out.println(createMove(new Position(), 4, 2, 0, true, false));
        System.out.println(createMove(new Position(), 4, 6, 0, true, false));
        System.out.println(createMove(new Position(), 60, 58, 0, true, false));
        System.out.println(createMove(new Position(), 60, 62, 0, true, false));
    }
}
