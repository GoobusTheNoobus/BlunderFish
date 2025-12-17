package utils;

import gen.Move;
import gen.MoveGenerator;

public class Printing {
    public static void printMoveList () {
        // 64-bit move encoding:
    // Bits 0-5:   from square (0-63)
    // Bits 6-11:  to square (0-63)
    // Bits 12-14: moving piece type (1-6: P, N, B, R, Q, K)
    // Bits 15-17: captured piece type (1-6, 0 = none)
    // Bits 18-20: promotion piece (0-3: N, B, R, Q)
    // Bit 21:     capture flag
    // Bit 22:     double pawn push flag
    // Bit 23:     en passant flag
    // Bit 24:     castling flag
    // Bit 25:     promotion flag
    // Bits 26-29: castle rights before move (2 bits each side)
    // Bits 30-35: en passant square before move (0-63, 63 = none)
    
        for (int i = 0; i < MoveGenerator.pseudoLegalMovesCounter; i ++) {
            System.out.println(Move.toString(MoveGenerator.pseudoLegalMoves[i]));
        }
    }
}
