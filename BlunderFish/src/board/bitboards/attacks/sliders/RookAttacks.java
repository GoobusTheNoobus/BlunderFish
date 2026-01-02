/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.sliders;

import board.bitboards.Bitboards;
import utils.Utility;

public class RookAttacks {
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

    public static long raycast (int square, long blockers) {
        // Generate Rook Attacks for square and given blockers
        long mask = 0L;

        // Parse Rank and File 
        int rank = Utility.parseRank(square);
        int file = Utility.parseFile(square);

        // Iterate in each direction
        for (int newRank = rank + 1; newRank <= 7; newRank++){ // Upwards
            long newSquareMask = 1L << Utility.parseSquare(newRank, file);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        } 

        for (int newRank = rank - 1; newRank >= 0; newRank--){ // Downwards
            long newSquareMask = 1L << Utility.parseSquare(newRank, file);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newFile = file + 1; newFile <= 7; newFile++) { // Right
            long newSquareMask = 1L << Utility.parseSquare(rank, newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        } 
        for (int newFile = file - 1; newFile >= 0; newFile--){ // Left
            long newSquareMask = 1L << Utility.parseSquare(rank, newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask;
        } 

        return mask;
    }

    public static long[] precomputePerSquare(int square) {
        long[] attacks = new long[1 << RELEVANT_ROOK_BITS[square]];
        
        for (int index = 0; index < (1 << RELEVANT_ROOK_BITS[square]); index++) {
            long blockers = SliderHelper.indexToBlockers(index, AttackMasks.ROOK_MASKS[square]);

            int hashIndex = Hash.hash(blockers, square, true);
            attacks[hashIndex] = raycast(square, blockers);

            /*System.out.println("The hash is: " + hashIndex);
            System.out.println("Attack in that array is: ");
            Bitboards.printBitboard(attacks[hashIndex]);*/
        }

        return attacks;

    }
    public static void main(String[] args) {
        AttackMasks.initializeMasks();

        precomputePerSquare(45);
    }
}
