package eval;

import board.position.Position;
import eval.criteria.MaterialEval;

public class Evaluation {
    
    // Gives a static evaluation, meaning it does not search.
    public static int evaluate (Position pos) {
        int score = 0;
        
        score += MaterialEval.evaluateMaterial(pos); // PSTs are used

        return score;
    }
}
