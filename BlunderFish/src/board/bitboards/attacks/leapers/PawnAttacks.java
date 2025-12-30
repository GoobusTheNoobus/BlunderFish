/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.leapers;

import board.bitboards.Bitboards;
import utils.Constants;
import utils.Utility;

public class PawnAttacks {
    public static long[] precomputeWhite() {
        // Precompute Pawn Attacks (Not moves, just captures)
        int[][] whitePawnAttackDirections = {{1, 1}, {1, -1}};
        long[] attacks = new long[64];

        // iterate squares
        for (int i = 0; i < 64; i ++) {

            long mask = 0L;
            
            for (int[] whitePawnAttack : whitePawnAttackDirections) {
                int rank = Utility.parseRank(i) + whitePawnAttack[0];
                int file = Utility.parseFile(i) + whitePawnAttack[1];

                if (rank < 0 || rank > 7 || file < 0 || file > 7) {
                    continue;
                }

                int square = Utility.parseSquare(rank, file);

                mask |= 1L << square;

            }
            

            // Append to the attacks
            attacks[i] = mask;
            
        }
        return attacks;
        
    }

    public static long[] precomputeBlack() {
        int[][] blackPawnAttackDirections = {{-1, 1}, {-1, -1}};
        long[] attacks = new long[64];

        // iterate squares
        for (int i = 0; i < 64; i ++) {

            long mask = 0L;
            
            for (int[] blackPawnAttack : blackPawnAttackDirections) {
                int rank = Utility.parseRank(i) + blackPawnAttack[0];
                int file = Utility.parseFile(i) + blackPawnAttack[1];

                if (rank < 0 || rank > 7 || file < 0 || file > 7) {
                    continue;
                }

                int square = Utility.parseSquare(rank, file);

                mask |= 1L << square;

            }

            // Append to the attacks
            attacks[i] = mask;
        }
        return attacks;
    }

    public static void main(String[] args) {
        long [] a = precomputeBlack();

        for (int i = 0; i < 64; i++) {

            System.out.println("Pawn square");
            Bitboards.printBitboard(1L << i);
            System.out.println("\nAttacks");
            Bitboards.printBitboard(a[i]);
        }
    }
}
