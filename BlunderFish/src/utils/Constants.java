/* ---------------------MY FIRST CHESS ENGINE--------------------- */

// Literally the most useless class
// It used to be full of random stuff, but all the constants got moved to other classes. 

package utils;


public class Constants {


    public static final char WHITE_PAWN_CHAR  = 'P';
    public static final char WHITE_KNIGHT_CHAR = 'N';
    public static final char WHITE_BISHOP_CHAR = 'B';
    public static final char WHITE_ROOK_CHAR   = 'R';
    public static final char WHITE_QUEEN_CHAR  = 'Q';
    public static final char WHITE_KING_CHAR   = 'K';

    public static final char BLACK_PAWN_CHAR   = 'p';
    public static final char BLACK_KNIGHT_CHAR = 'n';
    public static final char BLACK_BISHOP_CHAR = 'b';
    public static final char BLACK_ROOK_CHAR   = 'r';
    public static final char BLACK_QUEEN_CHAR  = 'q';
    public static final char BLACK_KING_CHAR   = 'k';

    public static final int WHITE_KINGSIDE_CASTLING_MASK = 0b0001;
    public static final int WHITE_QUEENSIDE_CASTLING_MASK = 0b0010;
    public static final int BLACK_KINGSIDE_CASTLING_MASK = 0b0100;
    public static final int BLACK_QUEENSIDE_CASTLING_MASK = 0b1000;

    public static final int WHITE_CASTLING_RIGHTS = 0b0011;
    public static final int BLACK_CASTLING_RIGHTS = 0b1100;

    public static final int CASTLING_KINGSIDE_FLAG = 0b1;
    public static final int CASTLING_QUEENSIDE_FLAG = 0b10;
    public static final int EN_PASSANT_FLAG = 0b11;
    public static final int DOUBLE_PAWN_PUSH = 0b100 ;

    public static final long[] SQUARE_MASKS = new long[64];

    public static final long WK_CASTLING_BETWEEN_MASK = 0b11 << 5;
    public static final long WQ_CASTLING_BETWEEN_MASK = 0b111 << 1;
    public static final long BK_CASTLING_BETWEEN_MASK = (1L << 61) | (1L << 62);
    public static final long BQ_CASTLING_BETWEEN_MASK = (1L << 57) | (1L << 58) | (1L << 59);

    
    public static void initialize() {
        for (int i = 0; i < 64; i++) {
            SQUARE_MASKS[i] = 1L << i;
        }
    }
}
