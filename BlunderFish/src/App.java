import board.*;
import gen.Move;
import gen.MoveGenerator;
import utils.*;


public class App {
    public static void main(String[] args) throws Exception {
        Bitboards.init();

        final int WARMUP_ITERS = 200000;
        final int TEST_ITERS = 2_000_000;
        long x = 0;

        for (int i = 0; i < WARMUP_ITERS; i++) {
            x = x ^ (i * 31) + (x << 1);
        }
        Position p = new Position("8/8/3n4/R2R4/1B3Q2/8/4k3/8 w - - 0 1");
        Timer.start();
        
        for (int i = 0; i < TEST_ITERS; i ++) {
            // Operations Here
            MoveGenerator.generatePsuedoLegalMoves(p);
            MoveGenerator.clearArrays();
        }
        
        

        Timer.stop();
        MoveGenerator.generatePsuedoLegalMoves(p);

        Printing.printMoveList();

    
        Timer.printAverageTime(TEST_ITERS);

    }
}
   