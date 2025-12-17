package gen;

import utils.Utility;

public class Move {
    // 64-bit move encoding:
    // Bits 0-5:   from square (0-63)
    // Bits 6-11:  to square (0-63)
    // Bits 12-14: moving piece type (1-6: P, N, B, R, Q, K)
    // Bits 15-17: captured piece type (1-6, 0 = none)
    // Bits 18-20: promotion piece (0-3: N, B, R, Q)
    // Bit 21:     capture flag
    // Bit 22:     double pawn push flag
    // Bit 23:     en passant flag
    // Bit 24:     castling flag
    // Bit 25:     promotion flag
    // Bits 26-29: castle rights before move (2 bits each side)
    // Bits 30-35: en passant square before move (0-63, 63 = none)
    

    public static long createMove (int from, int to, int pieceMoved, int pieceCaptured, boolean isCaptured, boolean isDoublePush, boolean isEnPassant, boolean isCastling, boolean isPromotion, int promotedPiece, int prevCastlingRights, int prevEPSquare) {
        long move = 0L;

        // Basic Stuff
        move |= (long)from;
        move |= (long)to << 6;
        move |= (long)pieceMoved << 12;
        move |= (long)pieceCaptured << 15;
        move |= (long)promotedPiece << 18;
        
        // Flags
        if (isCaptured) move |= 1L << 21;
        if (isDoublePush) move |= 1L << 22;
        if (isEnPassant) move |= 1L << 23;
        if (isCastling) move |= 1L << 24;
        if (isPromotion) move |= 1L << 25;

        // Previous Game State for undoing
        move |= (long) prevCastlingRights << 26;
        move |= (long) prevEPSquare << 30;


        return move;
    }

    public static int getFromSquare (long move) {
        return (int)(move & 63);
    }

    public static int getToSquare (long move) {
        return (int)((move >>> 6) & 63);
    }

    public static int getMovedPiece (long move) {
        return (int)((move >>> 12) & 0b111);
    }

    public static int getCapturedPiece (long move) {
        return (int)((move >>> 15) & 0b111);
    }

    public static int getPromotionPiece (long move) {
        return (int)((move >>> 18) & 0b111);
    }

    public static boolean getCaptureFlag (long move) {
        return (move & (1L << 21)) != 0L;
    }

    public static boolean getDoublePushFlag (long move) {
        return (move & (1L << 22)) != 0L;
    }

    public static boolean getEnPassantFlag (long move) {
        return (move & (1L << 23)) != 0L;
    }

    public static boolean getCastlingFlag (long move) {
        return (move & (1L << 24)) != 0L;
    }
    
    public static boolean getPromotionFlag (long move) {
        return (move & (1L << 25)) != 0L;
    }

    public static int getPreviousCastlingRights (long move) {
        return (int)((move >>> 26) & 0b1111);
    }

    public static int getPreviousEnPassantSquare (long move) {
        return (int)((move >>> 30) & 0b111111);
    }

    
    public static String toString (long move) {
        if (move == 0L) {
            return "-";
        }

        /* if (getCastlingFlag(move)) {
            if ((getToSquare(move) & 7) == 2)
                return "O-O-O";
            else if ((getToSquare(move) & 7) == 6) 
                return "O-O";
            else 
                throw new IllegalArgumentException("reprMove: the given move has castling flag, but the to square does not match kingside or queenside squares, the file is: " + (getToSquare(move) & 7));
        } */

        String from = Utility.getStringFromSquareInt(getFromSquare(move));
        String to = Utility.getStringFromSquareInt(getToSquare(move));

        if (getPromotionFlag(move)) {
            char promoChar = "NBRQ".charAt(getPromotionPiece(move));
            return from + to + promoChar;
        }
        return from + to;
    }
}
