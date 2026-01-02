import java.util.Arrays;

import board.bitboards.Bitboards;
import board.position.Position;
import board.position.moves.MoveBuffer;
import board.position.moves.MoveGenerator;
import board.position.moves.helper.Move;
import utils.Constants;

public class PerftGenTest {
    public static void main(String[] args) throws Exception{
        initialize();
        
        printGenerationTest(5, new Position("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"));
        
    }

    protected static long recurseGeneration (int depth, Position pos) throws Exception {
        if (depth == 0) {
            return 1;
        }

        MoveBuffer buff = new MoveBuffer(512);
        MoveGenerator.generateLegalMoves(pos, buff);
        
        long numPosition = 0;

        for (int i = 0; i < buff.size(); i++) {
            long move = buff.getElementByIndex(i);


            
            pos.makeMove(move, false);

            

            pos.gameState.switchSide();

            // log.makeMove(move);
            numPosition += recurseGeneration(depth - 1, pos);
            pos.undoMove(true);
            // log.undoMove(move);

            
        }

        return numPosition;
    }

    protected static void printGenerationTest(int depth, Position pos) throws Exception {
        MoveBuffer buff = new MoveBuffer(512);
        MoveGenerator.generateLegalMoves(pos, buff);
        System.out.println("There are " + buff.size() + " number of legal moves");
        long num = 0;
        for (int i = 0; i < buff.size(); i++) {
            pos.makeMove(buff.getElementByIndex(i), true);
            long numPosition = recurseGeneration(depth - 1, pos);
            num += numPosition;
            System.out.println(Move.toString(buff.getElementByIndex(i)) + ": " + numPosition);
            pos.undoMove(true);
        }
        System.out.println("Total nodes searched: " + num);
    }

    static void initialize() {
        Constants.initialize();
        Bitboards.initialize();
        
    }
}
