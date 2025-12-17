package board;

import utils.Constants;
import utils.Timer;

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

    public static final int[] RELEVANT_ROOK_BITS = new int[64];
    public static final int[] RELEVANT_BISHOP_BITS = new int[64];
    

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

    static long rookMask(int square) {
        long mask = 0L;
        int r = square >>> 3;
        int f = square & 7;

        for (int i = r + 1; i <= 6; i++) mask |= 1L << (i * 8 + f);
        for (int i = r - 1; i >= 1; i--) mask |= 1L << (i * 8 + f);
        for (int i = f + 1; i <= 6; i++) mask |= 1L << (r * 8 + i);
        for (int i = f - 1; i >= 1; i--) mask |= 1L << (r * 8 + i);

        return mask;
    }   
    static long bishopMask(int square) {
        long mask = 0L;
        int r = square >>> 3;
        int f = square & 7;

        for (int i = 1; r+i <= 6 && f+i <= 6; i++) mask |= 1L << ((r+i)*8 + f+i);
        for (int i = 1; r+i <= 6 && f-i >= 1; i++) mask |= 1L << ((r+i)*8 + f-i);
        for (int i = 1; r-i >= 1 && f+i <= 6; i++) mask |= 1L << ((r-i)*8 + f+i);
        for (int i = 1; r-i >= 1 && f-i >= 1; i++) mask |= 1L << ((r-i)*8 + f-i);

        return mask;
    }

    static long rookAttacks(int square, long blockers) {
        long attacks = 0L;
        int r = square >>> 3;
        int f = square & 7;

        for (int i = r+1; i < 8; i++) {
            int sq = i*8 + f;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        for (int i = r-1; i >= 0; i--) {
            int sq = i*8 + f;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        for (int i = f+1; i < 8; i++) {
            int sq = r*8 + i;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        for (int i = f-1; i >= 0; i--) {
            int sq = r*8 + i;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        return attacks;
    }

    static long bishopAttacks(int square, long blockers) {
        long attacks = 0L;
        int r = square >>> 3;
        int f = square & 7;

        for (int i=1; r+i<8 && f+i<8; i++) {
            int sq = (r+i)*8 + f+i;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        for (int i=1; r+i<8 && f-i>=0; i++) {
            int sq = (r+i)*8 + f-i;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        for (int i=1; r-i>=0 && f+i<8; i++) {
            int sq = (r-i)*8 + f+i;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        for (int i=1; r-i>=0 && f-i>=0; i++) {
            int sq = (r-i)*8 + f-i;
            attacks |= 1L << sq;
            if ((blockers & (1L << sq)) != 0) break;
        }
        return attacks;
    }

    static void initMagic() {
        for (int sq = 0; sq < 64; sq++) {

            ROOK_MASKS[sq] = rookMask(sq);
            BISHOP_MASKS[sq] = bishopMask(sq);

            int rookBits = Long.bitCount(ROOK_MASKS[sq]);
            int bishopBits = Long.bitCount(BISHOP_MASKS[sq]);

            ROOK_ATTACKS[sq] = new long[1 << rookBits];
            BISHOP_ATTACKS[sq] = new long[1 << bishopBits];

            for (int i = 0; i < (1 << rookBits); i++) {
                long blockers = indexToBlockers(i, rookBits, ROOK_MASKS[sq]);
                int index = (int)((blockers * Constants.ROOK_MAGIC_NUMBERS[sq]) >>> (64 - Constants.RELEVANT_ROOK_BITS[sq]));
                ROOK_ATTACKS[sq][index] = rookAttacks(sq, blockers);
            }

            for (int i = 0; i < (1 << bishopBits); i++) {
                long blockers = indexToBlockers(i, bishopBits, BISHOP_MASKS[sq]);
                int index = (int)((blockers * Constants.BISHOP_MAGIC_NUMBERS[sq]) >>> (64 - Constants.RELEVANT_BISHOP_BITS[sq]));
                BISHOP_ATTACKS[sq][index] = bishopAttacks(sq, blockers);
            }
        }
    }

    static long indexToBlockers(int index, int bits, long mask) {
        long blockers = 0L;
        for (int i = 0; i < bits; i++) {
            int sq = Long.numberOfTrailingZeros(mask);
            mask &= mask - 1;
            if ((index & (1 << i)) != 0)
                blockers |= 1L << sq;
        }
        return blockers;
    }

    

    static {
        for (int i = 0; i < 64; i++) {
            ROOK_MASKS[i] = rookMask(i);
            BISHOP_MASKS[i] = bishopMask(i);
        }

        initalizeKnightTable();
        initalizeKingTable();
        intializePawnAttackTables();
        initMagic();
    }

    public static int magicHash (long blockers, long magicNumber, int relevantBits) {
        int shift = 64 - relevantBits;
        return (int)((blockers * magicNumber) >>> shift);
    }

    public static void init() {
        for (int i = 0; i < 64; i++) {
            ROOK_MASKS[i] = rookMask(i);
            BISHOP_MASKS[i] = bishopMask(i);
        }

        initalizeKnightTable();
        initalizeKingTable();
        intializePawnAttackTables();
        initMagic();
    }

    public static void main(String[] args) {
        Timer.start();
        
        Timer.stop();
        Timer.printTime();
    }
    
}
