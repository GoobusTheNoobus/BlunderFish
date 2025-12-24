/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves;

import utils.Utility;
import board.*;
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
*/
public class Move {
    public static final int CASTLING_KINGSIDE_FLAG = 1;
    public static final int CASTLING_QUEENSIDE_FLAG = 2;
    public static final int EN_PASSANT_FLAG = 3;
    public static final int DOUBLE_PAWN_PUSH = 4;

    public static int createMove (Position pos, int from, int to, int promotedPiece, boolean isCastling, boolean enPassant) {
        int move = 0;

        move |= from;
        move |= to << 6;
        move |= pos.pieceAt(to) << 12;
        move |= promotedPiece << 16;
        move |= pos.gameState.castlingRights << 20;

        if (isCastling) {
            if (Utility.parseFile(to) == 6)
                move |= CASTLING_KINGSIDE_FLAG << 24;
            if (Utility.parseFile(to) == 2) 
                move |= CASTLING_QUEENSIDE_FLAG << 24;
        }
        if (enPassant)
            move |= EN_PASSANT_FLAG << 24;

        if ((pos.pieceAt(from) == Piece.WP && to - from == 16) && (pos.pieceAt(from) == Piece.BP && from - to == 16))
            move |= DOUBLE_PAWN_PUSH << 24;
            
        return move;
    }
    public static int getFromSquare (int move) {
        return move & 63;
    }
    public static int getToSquare (int move) {
        return (move >>> 6) & 63;
    }
    public static int getCapturedPiece (int move) {
        return (move >>> 12) & 0b1111;
    }
    public static int getPromotedPiece (int move) {
        return (move >>> 16) & 0b1111;
    }
    public static int getCastlingRights (int move) {
        return (move >>> 20) & 0b1111;
    }
    public static int getFlag (int move) {
        return (move >>> 24) & 0b111;
    }

    public static String toString(int move) {
        return "A move";
    }
}
