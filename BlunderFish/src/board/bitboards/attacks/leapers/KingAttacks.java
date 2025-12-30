/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.leapers;

import utils.Constants;
import utils.Utility;

public class KingAttacks {
    public static long[] precompute() {
        long[] attacks = new long[64];
        int[][] kingDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int i = 0; i < 64; i ++) {
            long mask = 0L;

            // Iterate direction
            for (int[] kingDir : kingDirections) {
                int rank = Utility.parseRank(i) + kingDir[0];
                int file = Utility.parseFile(i) + kingDir[1];

                if (rank < 0 || rank > 7 || file < 0 || file > 7) {
                    continue;
                }

                int square = Utility.parseSquare(rank, file);

                mask |= Constants.SQUARE_MASKS[square];

            }
            // Append to array
            attacks[i] = mask;
        }
        return attacks;
    }
}
