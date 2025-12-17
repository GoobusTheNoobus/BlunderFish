package utils;
public class Constants {

    public static final int WP_BITBOARD = 0;
    public static final int WN_BITBOARD = 1;
    public static final int WB_BITBOARD = 2;
    public static final int WR_BITBOARD = 3;
    public static final int WQ_BITBOARD = 4;
    public static final int WK_BITBOARD = 5;

    public static final int BP_BITBOARD = 6;
    public static final int BN_BITBOARD = 7;
    public static final int BB_BITBOARD = 8;
    public static final int BR_BITBOARD = 9;
    public static final int BQ_BITBOARD = 10;
    public static final int BK_BITBOARD = 11;

    public static final char WHITE_PAWN_CHAR  = '♙';
    public static final char WHITE_KNIGHT_CHAR = '♘';
    public static final char WHITE_BISHOP_CHAR = '♗';
    public static final char WHITE_ROOK_CHAR   = '♖';
    public static final char WHITE_QUEEN_CHAR  = '♕';
    public static final char WHITE_KING_CHAR   = '♔';

    public static final char BLACK_PAWN_CHAR   = '♟';
    public static final char BLACK_KNIGHT_CHAR = '♞';
    public static final char BLACK_BISHOP_CHAR = '♝';
    public static final char BLACK_ROOK_CHAR   = '♜';
    public static final char BLACK_QUEEN_CHAR  = '♛';
    public static final char BLACK_KING_CHAR   = '♚';

    public static final long[] ROOK_MAGIC_NUMBERS = {
        0x4080002088104000L,
        0x40C0002004100040L,
        0x8080082000801000L,
        0x0180080010008095L,
        0x0300030004904800L,
        0x1100040012288300L,
        0x2A00008804020041L,
        0x1600102480410204L,
        0x9820800084204006L,
        0x0284400020100044L,
        0x1101802006100080L,
        0x4000801000080080L,
        0x0100800800801400L,
        0x2003000822840100L,
        0x0000800900802200L,
        0x404300004500008AL,
        0xA207818000294000L,
        0x0C40008042A0018AL,
        0x0220808020081002L,
        0x2088018008809002L,
        0x3020818004000800L,
        0x008880800C004200L,
        0x0020040010118812L,
        0x3420520001008154L,
        0xA8014000800A8022L,
        0x0410200040100240L,
        0x2D40100080200080L,
        0x0602003A00504020L,
        0x2001001100080004L,
        0x4002008200300804L,
        0x0005006100220004L,
        0x000040420000810CL,
        0x002C804009800024L,
        0x8218804003002100L,
        0x4202048042001020L,
        0x9002005022000A41L,
        0x0100800800801400L,
        0x01020050420004A8L,
        0x0000800900802200L,
        0x0402310082000144L,
        0x0408294008808000L,
        0x0710024460014000L,
        0x1020001008004040L,
        0x0102400A02A20010L,
        0x29A2002008920004L,
        0x21C2008004008100L,
        0x82B10A1851440010L,
        0x1880804084020001L,
        0x0411402580050100L,
        0x0043C00820009080L,
        0x8420084011006100L,
        0x00010900E0100100L,
        0x2003004802100500L,
        0x0C10106040044801L,
        0x0042880250014400L,
        0x2090012094004200L,
        0x1011801201004022L,
        0x1011801201004022L,
        0x0001084500102001L,
        0x0001003000208429L,
        0x002200041048600AL,
        0x0301000812040001L,
        0x0004021008451484L,
        0x04102C0180502502L
    };

    public static final long[] BISHOP_MAGIC_NUMBERS = {
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L,
        0xA010200004000000L
    };

    public static final int[] RELEVANT_ROOK_BITS = {
        12, 11, 11, 11, 11, 11, 11, 12,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        12, 11, 11, 11, 11, 11, 11, 12
    };

    public static final int[] RELEVANT_BISHOP_BITS = {
        6, 5, 5, 5, 5, 5, 5, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5,
        6, 5, 5, 5, 5, 5, 5, 6
    };

    

}
