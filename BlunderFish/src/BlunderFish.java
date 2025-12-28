/* ---------------------MY FIRST CHESS ENGINE--------------------- */

import board.*;
import board.bitboards.Bitboards;
import board.position.Piece;
import board.position.Position;
import board.position.moves.MoveGenerator;
import board.position.moves.helper.Move;
import utils.*;



public class BlunderFish {
    public static void main(String[] args) throws Exception {
        Bitboards.initialize();

        final int WARMUP_ITERS = 200000;
        final int TEST_ITERS = 10_000_000;
        
        /* 
        for (int i = 0; i < WARMUP_ITERS; i++) {
            
            Position pos = new Position();

            MoveGenerator.generateBishopMoves(pos, false);
            MoveGenerator.pseudoLegalMoves.clear();
        }
        
        */
        
        
        Position pos = new Position("rnbqkbn1/pppp1p1p/8/4rPp1/6K1/8/PPPP1PPP/RNBQ1BNR w q - 0 1");

        Timer.start();

        for (int i = 0; i < TEST_ITERS; i ++) {
            
        }

        Timer.stop();

        Bitboards.printBitboard(pos.board.bitboards[Piece.BK]);
        MoveGenerator.generateLegalMoves(pos);
        
        MoveGenerator.legalMoves.printMoveList();
        Timer.printAverageTime(TEST_ITERS);

    }
}
   