/* ---------------------MY FIRST CHESS ENGINE--------------------- */

import board.*;
import gen.Move;
import gen.MoveGenerator;
import utils.*;


public class BlunderFish {
    public static void main(String[] args) throws Exception {
        Bitboards.init();

        final int WARMUP_ITERS = 200000;
        final int TEST_ITERS = 1_000_000;
        

        Position pos = new Position("8/8/3n4/R2R4/1B3Q2/8/4k3/8 w - - 0 1");
        for (int i = 0; i < WARMUP_ITERS; i++) {
            MoveGenerator.generatePseudoLegalMoves(pos);
            MoveGenerator.clearArrays();
        }
        
        Timer.start();


        
        for (int i = 0; i < TEST_ITERS; i ++) {
            // Operations Here
            pos.makeMove(Move.createNormalMove(pos, 49, 25));
            
        }

        Timer.stop();

        Timer.printAverageTime(TEST_ITERS);

    }
}
   