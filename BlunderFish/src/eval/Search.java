package eval;

import board.position.Position;
import board.position.moves.MoveBuffer;
import board.position.moves.MoveGenerator;
import eval.utils.EvalUtil;

public class Search {
    
    public static int minimax (Position pos, int depth) {
        
        if (depth == 0) {
            return Evaluation.evaluate(pos);
        }

        boolean maxing = pos.gameState.whiteToMove;

        MoveBuffer buff = new MoveBuffer(256);
        MoveGenerator.generatePseudoLegalMoves(pos, buff);

        int numPosition = 0;
        int bestEval = maxing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < buff.size(); i++) {
            long move = buff.getElementByIndex(i);

            pos.makeMove(move, false);
            if (pos.isInCheck()) {
                pos.undoMove(false);
                continue;
            }

            numPosition += 1;

            pos.gameState.switchSide();

            int eval = minimax(pos, depth - 1);
            
            
            bestEval = maxing ? Math.max(eval, bestEval) : Math.min(eval, bestEval);

            pos.undoMove(true);
        }

        if (numPosition == 0) { // No legal moves
            return EvalUtil.currentGameStatus(pos);
        }

        return bestEval;

    }

    public static long getBestMove (Position pos, int depth) {
        if (depth == 0) return 0L;

        boolean maxing = pos.gameState.whiteToMove;

        MoveBuffer buff = new MoveBuffer(256);
        MoveGenerator.generatePseudoLegalMoves(pos, buff);

        int numPosition = 0;

        int bestEval = maxing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        long bestMove = 0L;

        for (int i = 0; i < buff.size(); i++) {
            long move = buff.getElementByIndex(i);

            pos.makeMove(move, false);
            if (pos.isInCheck()) {
                pos.undoMove(false);
                continue;
            }

            numPosition += 1;

            pos.gameState.switchSide();

            int eval = minimax(pos, depth - 1);
            
            if (maxing && eval > bestEval) {
                bestEval = eval;
                bestMove = move;
            } else if (!maxing && eval < bestEval) {
                bestEval = eval;
                bestMove = move;
            }

            pos.undoMove(true);
        }

        if (numPosition == 0) { // No legal moves
            
            return -1L;
        }

        return bestMove;
    }
}
