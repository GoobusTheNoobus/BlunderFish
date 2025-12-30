/* ---------------------MY FIRST CHESS ENGINE--------------------- */

import java.util.Arrays;

import board.bitboards.Bitboards;
import board.position.Position;
import board.position.moves.MoveBuffer;
import board.position.moves.MoveGenerator;

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

        Position pos = new Position();
        
        Timer.start();

        for (int i = 0; i < TEST_ITERS; i++) {
            // MoveGenerator.generateLegalMoves(pos);
        }

        Timer.stop();
        
        System.out.println(generationTest(5, pos));
        MoveGenerator.legalMoves.printMoveList();

        Timer.printAverageTime(TEST_ITERS);
        

    }

    static int generationTest (int depth, Position pos) {
        if (depth == 0) {
            return 1;
        }

        MoveBuffer buff = new MoveBuffer(512);
        MoveGenerator.generateLegalMoves(pos, buff);
        
        int numPosition = 0;

        for (int i = 0; i < buff.size(); i++) {
            long move = buff.getElementByIndex(i);
            Position oldPos = new Position();
            oldPos.gameState = pos.gameState.
            pos.makeMove(move, true);
            numPosition += generationTest(depth - 1, pos);
            pos.undoMove(true);
        }

        return numPosition;
    }

    static void initialize() {
        Constants.initialize();
        Bitboards.initialize();
        
    }
}
   