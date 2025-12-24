/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.leapers;

import utils.Utility;

public class KnightAttacks {
    

    public static long[] precompute() {
        long[] attacks = new long[64];
        int[][] knightDirections = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}}; // Knight Attacks

        for (int i = 0; i < 64; i ++) {
            // Init Mask
            long mask = 0L;

            // Iterate through each direction
            for (int[] knightDir : knightDirections) {
                int rank = Utility.parseRank(i) + knightDir[0];
                int file = Utility.parseFile(i) + knightDir[1];

                if (rank < 0 || rank > 7 || file < 0 || file > 7) {
                    continue;
                }

                int square = Utility.parseSquare(rank, file);

                mask |= 1L << square;

            }
            // Append to array
            attacks[i] = mask;
        }
        return attacks;
    }
}
