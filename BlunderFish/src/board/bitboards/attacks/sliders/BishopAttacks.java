/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.sliders;

import board.bitboards.Bitboards;
import utils.Utility;

public class BishopAttacks {
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

    public static long raycast (int square, long blockers) {
        // Generate Bishop Attacks for square and given blockers
        long mask = 0L;

        // Parse Rank and File 
        int rank = square / 8;
        int file = square % 8;

        // Iterate
        for (int newRank = rank + 1, newFile = file + 1; newRank <= 7 && newFile <= 7; newRank++, newFile++){
            long newSquareMask = 1L << Utility.parseSquare(newRank, newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked

                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newRank = rank + 1, newFile = file - 1; newRank <= 7 && newFile >= 0; newRank++, newFile--){
            long newSquareMask = 1L << Utility.parseSquare(newRank, newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newRank = rank - 1, newFile = file + 1; newRank >= 0 && newFile <= 7; newRank--, newFile++){
            long newSquareMask = 1L << Utility.parseSquare(newRank, newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newRank = rank - 1, newFile = file - 1; newRank >= 0 && newFile >= 0; newFile--, newRank--){
            long newSquareMask = 1L << Utility.parseSquare(newRank, newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }
        return mask;
    }

    public static long[] precomputePerSquare(int square) {
        long[] attacks = new long[1 << RELEVANT_BISHOP_BITS[square]];
        
        for (int index = 0; index < (1 << RELEVANT_BISHOP_BITS[square]); index++) {
            long blockers = SliderHelper.indexToBlockers(index, AttackMasks.BISHOP_MASKS[square]);

            int hashIndex = Hash.hash(blockers, square, false);

            attacks[hashIndex] = raycast(square, blockers);

            /*if (hashIndex == 0) {

            System.out.println("The hash is: " + hashIndex);
            System.out.println("The blocker config is");
            Bitboards.printBitboard(blockers);
            System.out.println("Attack in that array is: ");
            Bitboards.printBitboard(attacks[hashIndex]);
            }*/
            
        }

        return attacks;

    }

    public static void main(String[] args) {
        AttackMasks.initializeMasks();

        precomputePerSquare(45);
    }
}