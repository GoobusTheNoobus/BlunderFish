package gen;

import utils.Utility;
import board.*;

public class Move {
    // 64-bit move encoding:
    // Bits 0-5:   from square (0-63)
    // Bits 6-11:  to square (0-63)
    // Bits 12-15: moving piece type 
    // Bits 16-19: captured piece type 
    // Bits 20-23: promotion piece (0-3: N, B, R, Q)
    // Bit 24:     capture flag
    // Bit 25:     double pawn push flag
    // Bit 26:     en passant flag
    // Bit 27:     castling flag
    // Bit 28:     promotion flag
    // Bits 29-32: castle rights before move (2 bits each side)
    // Bits 33-39: en passant square before move (0-63, 64 = none)
    

    public static long createMove (int from, int to, int pieceMoved, int pieceCaptured, boolean isCaptured, boolean isDoublePush, boolean isEnPassant, boolean isCastling, boolean isPromotion, int promotedPiece, int prevCastlingRights, int prevEPSquare) {
        long move = 0L;

        // Basic Stuff
        move |= (long)from;
        move |= (long)to << 6;
        move |= (long)pieceMoved << 12;
        move |= (long)pieceCaptured << 16;
        move |= (long)promotedPiece << 20;
        
        // Flags
        if (isCaptured) move |= 1L << 24;
        if (isDoublePush) move |= 1L << 25;
        if (isEnPassant) move |= 1L << 26;
        if (isCastling) move |= 1L << 27;
        if (isPromotion) move |= 1L << 28;

        // Previous Game State for undoing
        move |= (long) prevCastlingRights << 29;
        move |= (long) prevEPSquare << 33;

        

        return move;
    }

    public static int getFromSquare (long move) {
        return (int)(move & 63);
    }

    public static int getToSquare (long move) {
        return (int)((move >>> 6) & 63);
    }

    public static int getMovedPiece (long move) {
        return (int)((move >>> 12) & 0b1111);
    }

    public static int getCapturedPiece (long move) {
        return (int)((move >>> 16) & 0b1111);
    }

    public static int getPromotionPiece (long move) {
        return (int)((move >>> 20) & 0b1111);
    }

    public static boolean getCaptureFlag (long move) {
        return (move & (1L << 24)) != 0L;
    }

    public static boolean getDoublePushFlag (long move) {
        return (move & (1L << 25)) != 0L;
    }

    public static boolean getEnPassantFlag (long move) {
        return (move & (1L << 26)) != 0L;
    }

    public static boolean getCastlingFlag (long move) {
        return (move & (1L << 27)) != 0L;
    }
    
    public static boolean getPromotionFlag (long move) {
        return (move & (1L << 28)) != 0L;
    }

    public static int getPreviousCastlingRights (long move) {
        return (int)((move >>> 29) & 0b1111);
    }

    public static int getPreviousEnPassantSquare (long move) {
        return (int)((move >>> 33) & 63);
    }

    public static long createNormalMove (Position pos, int from, int to) {
        int pieceMoved = Math.abs(pos.pieceAt(from));
        int pieceCaptured = Math.abs(pos.pieceAt(to));
        boolean isCaptured = pieceCaptured != 0;
        boolean isDoublePush = (pieceMoved == Piece.WP && ((to >>> 3) - (from >>> 3)) == 2) || (pieceMoved == Piece.BP && ((from >>> 3) - (to >>> 3) == 2));
        long move = Move.createMove(from,
             to, 
             pieceMoved, 
             pieceCaptured, 
             isCaptured, 
             isDoublePush,
             false, 
             false, 
             false,
             0, 
             pos.castlingRights, 
             pos.enPassantSquare
            );
        return move;
    }

    public static long createPromotionMove (Position pos, int from, int to, int promotion) {
        int piecemoved = pos.pieceAt(from);
        int pieceCaptured = pos.pieceAt(to);
        boolean isCaptured = pieceCaptured != 0;
        long move = Move.createMove(from, 
            to, 
            piecemoved, 
            pieceCaptured, 
            isCaptured, 
            false,
            false, 
            false, 
            true, 
            promotion, 
            pos.castlingRights, 
            pos.enPassantSquare
        );
        return move;
    }

    public static long createEnPassantMove (Position pos, int from, int to) {
        int piecemoved = pos.pieceAt(from);
        int pieceCaptured = (pos.whiteToMove) ? Piece.BP: Piece.WP;
        boolean isCaptured = true;
        long move = Move.createMove(
            from, 
            to, 
            piecemoved, 
            pieceCaptured, 
            isCaptured, 
            false, 
            true, 
            false, 
            false, 
            0, 
            pos.castlingRights, 
            pos.enPassantSquare
        );
        return move;
    }

    public static long createCastlingMove (Position pos, boolean kingSide) {
        boolean sideToMove = pos.whiteToMove;
        int toRank = sideToMove ? 0: 7;
        int toFile = kingSide ? 6 : 2;
        int toSquare = (toRank << 3) | toFile; 
        int fromSquare = sideToMove ? 4 : 59;
        long move = Move.createMove(
            fromSquare, 
            toSquare, 
            sideToMove ? 6 : -6, 
            0, 
            false, 
            false, 
            false, 
            true, 
            false, 
            0, 
            pos.castlingRights, 
            pos.enPassantSquare);
        return move;
    }
    
    public static String toString (long move) {
        if (move == 0L) {
            return "-";
        }

        if (getCastlingFlag(move)) {
            if ((getToSquare(move) & 7) == 2)
                return "O-O-O";
            else if ((getToSquare(move) & 7) == 6) 
                return "O-O";
            else 
                throw new IllegalArgumentException("reprMove: the given move has castling flag, but the to square does not match kingside or queenside squares, the file is: " + (getToSquare(move) & 7));
        } 

        String from = Utility.getStringFromSquareInt(getFromSquare(move));
        String to = Utility.getStringFromSquareInt(getToSquare(move));

        if (getPromotionFlag(move)) {
            char promoChar = "NBRQ".charAt(getPromotionPiece(move));
            return from + to + promoChar;
        }
        return from + to;
    }

    public static void main(String[] args) {
        System.out.println(Long.toBinaryString(-35815));
    }
}
