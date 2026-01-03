/* ---------------------MY FIRST CHESS ENGINE--------------------- */

import java.util.Arrays;

import board.bitboards.Bitboards;
import board.position.Piece;
import board.position.Position;
import board.position.moves.MoveBuffer;
import board.position.moves.MoveGenerator;
import board.position.moves.helper.Move;
import eval.Search;
import eval.utils.EvalUtil;
import utils.*;



public class BlunderFish {
    public static void main(String[] args) throws Exception {
        initialize();

        final int WARMUP_ITERS = 200000;
        final int TEST_ITERS = 1_000_000;
        
        for (int i = 0; i < WARMUP_ITERS; i++) {
            Position pos = new Position();
            MoveBuffer buff = new MoveBuffer(67);
            MoveGenerator.generatePseudoLegalMoves(pos, buff);
            buff.clear();
        }
        
        // Original: rnbqkbnr/pppp2pp/5p2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 1 3
        // Blocked: rnbqkbnr/pppp3p/5pp1/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 4

        Position pos = new Position("1k1rr3/ppp2Q1p/6p1/8/2P5/P2qp3/1P1P1bPP/RNBK3n w - - 0 29");

        Timer.start();

        System.out.println(Move.toString(Search.getBestMove(pos, 4)));
        System.out.println();

        Timer.stop();
        
        
        Timer.printTime();
        

    }

    static void initialize() {
        Constants.initialize();
        Bitboards.initialize();
        
    }

    static void playSelf (Position pos, int depth) {
        while (true) {
            long bestMove = Search.getBestMove(pos, depth);

            if (bestMove == -1L) {
                System.out.println("The game has ended!");
                break;
            }

            System.out.println(Move.toString(bestMove));

            pos.makeMove(bestMove, true);

        }
    }
}
   