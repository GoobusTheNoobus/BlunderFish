/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards;

import board.bitboards.attacks.sliders.*;
import utils.Utility;
import board.bitboards.attacks.leapers.*;;


public class Bitboards {
    public static long[] KING_ATTACKS = new long[64];
    public static long[] KNIGHT_ATTACKS = new long[64];
    public static long[] WHITE_PAWN_ATTACKS = new long[64];
    public static long[] BLACK_PAWN_ATTACKS = new long[64];

    public static long[][] ROOK_ATTACKS = new long[64][];
    public static long[][] BISHOP_ATTACKS = new long[64][];

    public static void initialize() {
        AttackMasks.initializeMasks();
        KING_ATTACKS = KingAttacks.precompute();
        KNIGHT_ATTACKS = KnightAttacks.precompute();
        WHITE_PAWN_ATTACKS = PawnAttacks.precomputeWhite();
        BLACK_PAWN_ATTACKS = PawnAttacks.precomputeBlack();

        for (int square = 0; square < 64; square++) {
            ROOK_ATTACKS[square] = RookAttacks.precomputePerSquare(square);
            BISHOP_ATTACKS[square] = BishopAttacks.precomputePerSquare(square);
        }
    }

    public static long getBishopAttack(long occupied, int square) {
        int index = Hash.hash(occupied, square, false);
        return BISHOP_ATTACKS[square][index];
    }

    public static long getRookAttack(long occupied, int square) {
        int index = Hash.hash(occupied, square, true);
        return ROOK_ATTACKS[square][index];
    }

    /* ##### HELPER FUNCTIONS ##### */

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
    
    
    public static long setBit (long bitboard, int index) {
        return bitboard | (1L << index);
    }

    public static long resetBit (long bitboard, int index) {
        return bitboard & ~(1L << index);
    }
    public static void main(String[] args) {
        initialize();
        printBitboard(BishopAttacks.raycast(45, 0));
    }
}   
