/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.sliders;

import utils.Utility;

public class AttackMasks {
    public static final long[] ROOK_MASKS = new long[64];
    public static final long[] BISHOP_MASKS = new long[64];

    public static void initializeMasks() {
        for (int square = 0; square < 64; square++) {
            long mask = 0L;

            int rank = Utility.parseRank(square);
            int file = Utility.parseFile(square);

            for (int newRank = rank + 1; newRank <= 6; newRank++) mask |= 1L << Utility.parseSquare(newRank, file); // Upwards

            for (int newRank = rank - 1; newRank >= 1; newRank--) mask |= 1L << Utility.parseSquare(newRank, file); // Downwards

            for (int newFile = file + 1; newFile <= 6; newFile++) mask |= 1L << Utility.parseSquare(rank, newFile); // Right

            for (int newFile = file - 1; newFile >= 1; newFile--) mask |= 1L << Utility.parseSquare(rank, newFile); // Left

            // Put it into ROOK_MASK
            ROOK_MASKS[square] = mask;

            mask = 0L;

            for (int newRank = rank + 1, newFile = file + 1; newRank <= 6 && newFile <= 6; newRank++, newFile++) mask |= 1L << Utility.parseSquare(newRank, newFile); // NE

            for (int newRank = rank + 1, newFile = file - 1; newRank <= 6 && newFile >= 1; newRank++, newFile--) mask |= 1L << Utility.parseSquare(newRank, newFile); // NW

            for (int newRank = rank - 1, newFile = file + 1; newRank >= 1 && newFile <= 6; newRank--, newFile++) mask |= 1L << Utility.parseSquare(newRank, newFile); // SE

            for (int newRank = rank - 1, newFile = file - 1; newRank >= 1 && newFile >= 1; newRank--, newFile--) mask |= 1L << Utility.parseSquare(newRank, newFile); // SW

            // Put it into BISHOP_MASKS
            BISHOP_MASKS[square] = mask;
        }
    }
}
