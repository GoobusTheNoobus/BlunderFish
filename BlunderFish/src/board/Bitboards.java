package board;


public class Bitboards {
    public static final long A_FILE = 0x0101010101010101L;
    public static final long B_FILE = A_FILE << 1;
    public static final long C_FILE = A_FILE << 2;
    public static final long D_FILE = A_FILE << 3;
    public static final long E_FILE = A_FILE << 4;
    public static final long F_FILE = A_FILE << 5;
    public static final long G_FILE = A_FILE << 6;
    public static final long H_FILE = A_FILE << 7;

    public static final long RANK_1 = 0x00000000000000FFL;
    public static final long RANK_2 = RANK_1 << 8;
    public static final long RANK_3 = RANK_2 << 8;
    public static final long RANK_4 = RANK_3 << 8;
    public static final long RANK_5 = RANK_4 << 8;
    public static final long RANK_6 = RANK_5 << 8;
    public static final long RANK_7 = RANK_6 << 8;
    public static final long RANK_8 = RANK_7 << 8;

    public static final long[] KNIGHT_ATTACKS = new long[64];
    public static final long[] KING_ATTACKS = new long[64];
    public static final long[] WHITE_PAWN_ATTACKS = new long[64];
    public static final long[] BLACK_PAWN_ATTACKS = new long[64];

    public static final long[] ROOK_MASKS = new long[64];
    public static final long[] BISHOP_MASKS = new long[64];

    public static final long[][] ROOK_ATTACKS = new long[64][];
    public static final long[][] BISHOP_ATTACKS = new long[64][];

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
    

    public static void initalizeKnightTable() {
        int[][] knightDirections = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
        for (int i = 0; i < 64; i ++) {
            long schlong = 0L;
            for (int[] knightDir : knightDirections) {
                int x = (i >> 3) + knightDir[0];
                int y = (i & 7) + knightDir[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                schlong |= 1L << square;

            }
            KNIGHT_ATTACKS[i] = schlong;
        }
    }

    public static void initalizeKingTable() {
        int[][] kingDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int i = 0; i < 64; i ++) {
            long schlong = 0L;
            for (int[] kingDir : kingDirections) {
                int x = (i >> 3) + kingDir[0];
                int y = (i & 7) + kingDir[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                schlong |= 1L << square;

            }
            KING_ATTACKS[i] = schlong;
        }
    }

    public static void intializePawnAttackTables() {
        int[][] whitePawnAttacks = {{1, 1}, {1, -1}};
        int[][] blackPawnAttacks = {{-1, 1}, {-1, -1}};
        for (int i = 0; i < 64; i ++) {
            long schlong1 = 0L;
            long schlong2 = 0L;
            for (int[] whitePawnAttack : whitePawnAttacks) {
                int x = (i >> 3) + whitePawnAttack[0];
                int y = (i & 7) + whitePawnAttack[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                schlong1 |= 1L << square;

            }
            for (int[] blackPawnAttack: blackPawnAttacks) {
                int x = (i >> 3) + blackPawnAttack[0];
                int y = (i & 7) + blackPawnAttack[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                schlong2 |= 1L << square;
            } 

            WHITE_PAWN_ATTACKS[i] = schlong1;
            BLACK_PAWN_ATTACKS[i] = schlong2;
        }
        
        
    }



    private static long generateRookAttack(int square, long occupied) {
        long mask = 0L;
        int rank = square >> 3;
        int file = square & 7;

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir: directions) {

            for (int j = 1; j < 8; j ++) {
                int newr = rank + j * dir[0];
                int newf = file + j * dir[1];

                if (newf > 7 || newf < 0 || newr > 7 || newr < 0) {
                    break;
                }

                if (((1L << ((newr << 3) | newf)) & occupied) != 0L) {
                    mask |= 1L << ((newr << 3) | newf);
                    break;
                }

                    // SIX SEVEN
                mask |= 1L << ((newr << 3) | newf);

            }
        }

        return mask;
    }

    private static long generateBishopAttack(int square, long occupied) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        long attacks = 0;

        int r = square >>> 3;
        int f = square & 7;

        for (int[] dir: directions) {

            for (int i = 1; i < 8; i ++) {
                int newr = r + dir[0] * i;
                int newf = f + dir[1] * i;

                if (newr < 0 || newr > 7 || newf < 0 || newf > 7) {
                    break;
                }

                int newSquare = newr << 3 | newf;
                long newSquareMask = 1L << newSquare;

                if ((occupied & newSquareMask) != 0L) {
                    attacks |= newSquareMask;
                    break;
                }

                attacks |= newSquareMask;

                
            }
        }
        return attacks;
    }

    private static void generateBishopAttacks() {
        for (int i = 0; i < 64; i ++) {
            BISHOP_ATTACKS[i] = new long[1 << RELEVANT_BISHOP_BITS[i]];

            for (int j = 0; j < (1 << RELEVANT_BISHOP_BITS[i]); j++) {
                long blockers = indexToBlockers(j, BISHOP_MASKS[i]);

                BISHOP_ATTACKS[i][j] = generateBishopAttack(i, blockers);
            }
        }
    }

    private static void generateRookAttacks() {
        for (int i = 0; i < 64; i ++) {

            ROOK_ATTACKS[i] = new long[1 << RELEVANT_ROOK_BITS[i]];

            for (int j = 0; j < (1 << RELEVANT_ROOK_BITS[i]); j++) {
                long blockers = indexToBlockers(j, ROOK_MASKS[i]);

                ROOK_ATTACKS[i][j] = generateRookAttack(i, blockers);
            }
        }
    }


    private static long indexToBlockers (int index, long mask) {
        long blockers = 0L;
        int bitPos = 0;

        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            mask &= mask - 1;

            if ((index & (1 << bitPos)) != 0) {
                blockers |= 1L << square;
            }

            bitPos ++;
        }

        return blockers;
    }


    private static void generateBishopMasks() {
        for (int i = 0; i < 64; i++) {
            long mask = 0L;
            int rank = i >> 3;
            int file = i & 7;

            int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

            for (int[] dir: directions) {
                int r = rank + dir[0];
                int f = file + dir[1];

                while (r > 0 && r < 7 && f > 0 && f < 7) {
                    mask |= (1L << (r * 8 + f));
                    r += dir[0];
                    f += dir[1];
                }
            }

            BISHOP_MASKS[i] = mask;
        }
    }

    private static void generateRookMasks() {
        for (int i = 0; i < 64; i ++) {
            long mask = 0L;
            int rank = i >> 3;
            int file = i & 7;

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            for (int[] dir: directions) {

                for (int j = 1; j < 8; j ++) {
                    int newr = rank + j * dir[0];
                    int newf = file + j * dir[1];

                    int nextr = newr + dir[0];
                    int nextf = newf + dir[1];

                    if (nextf > 7 || nextf < 0 || nextr > 7 || nextr < 0) {
                        break;
                    }

                    // SIX SEVEN
                    mask |= 1L << ((newr << 3) | newf);

                }
            }

            ROOK_MASKS[i] = mask;
        }
    }

    // Helper: prints a bitboard as a visual 8x8 board

    public static void printBitboard(long bb) {
        for (int rank = 7; rank >= 0; rank--) {

            System.out.print((rank + 1) + " ");

            for (int file = 0; file < 8; file++) {
                int sq = rank * 8 + file;
                System.out.print(((bb >>> sq) & 1L) != 0 ? "■ " : ". ");
            }

            System.out.println();
        }

        System.out.println("  a b c d e f g h\n");
    }

    public static int magicHash(long blockers, long magic, int relevantBits) {
        return (int)((blockers * magic) >>> (64 - relevantBits));
    }


    public static void init() {
        initalizeKingTable();
        initalizeKnightTable();
        intializePawnAttackTables();
        generateBishopMasks();
        generateRookMasks();
        generateBishopAttacks();
        generateRookAttacks();
    }

    public static void main(String[] args) {
        init();
        printBitboard(indexToBlockers(1, ROOK_MASKS[0]));
        printBitboard(ROOK_ATTACKS[0][1]);
    }
}
