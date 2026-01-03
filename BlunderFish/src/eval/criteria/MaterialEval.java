package eval.criteria;

import board.position.Piece;
import board.position.Position;

public class MaterialEval {

    // Piece Square Tables :D
    public static final int[] pawnTable = {
         0,  0,  0,  0,  0,  0,  0,  0,
         5, 10, 10,-15,-15, 10, 10,  5,
         5,  0,-10,  0,  0,-10,  0,  5,
         0,  0, 10, 20, 20, 10,  0,  0,
         5,  5, 10, 25, 25, 10,  5,  5,
        10, 10, 20, 30, 30, 20, 10, 10,
        50, 50, 55, 55, 55, 55, 50, 50,
         0,  0,  0,  0,  0,  0,  0,  0
    };

    public static final int[] knightTable = {
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    };


    public static final int[] bishopTable = {
        -30,-20,-10,  0,  0,-10,-20,-30,
        -20,-10,  0,  5,  5,  0,-10,-20,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5, 10, 25, 25, 10,  5,-10,
        -10,  5, 10, 25, 25, 10,  5,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -20,-10,  0,  5,  5,  0,-10,-20,
        -30,-20,-10,  0,  0,-10,-20,-30
    };

    public static final int[] rookTable = {
         0,  0,  5, 10, 10,  5,  0,  0,
         0,  0,  5, 10, 10,  5,  0,  0,
         0,  0,  5, 10, 10,  5,  0,  0,
         0,  0,  5, 10, 10,  5,  0,  0,
         0,  0,  5, 10, 10,  5,  0,  0,
         0,  0,  5, 10, 10,  5,  0,  0,
         5,  5, 10, 15, 15, 10,  5,  5,
         0,  0,  5, 10, 10,  5,  0,  0
    };

    public static final int[] queenTable = {
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
        -5,  0,  5, 10, 10,  5,  0, -5,
        0,  0,  5, 10, 10,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20
    };

    public static int evaluateMaterial (Position pos) {
        int score = 0;
        for (int square = 0; square < 64; square++) {
            int piece = pos.pieceAt(square);

            switch (piece) {
                case Piece.WP:
                    score += 100 + pawnTable[square];
                    break;
                case Piece.WN:
                    score += 320 + knightTable[square];
                    break;
                case Piece.WB:
                    score += 330 + bishopTable[square];
                    break;
                case Piece.WR:
                    score += 500 + rookTable[square];
                    break;
                case Piece.WQ:
                    score += 900 + queenTable[square];
                    break;
                case Piece.BP:
                    score -= 100 + pawnTable[63 - square];
                    break;
                case Piece.BN:
                    score -= 320 + knightTable[63 - square];
                    break;
                case Piece.BB:
                    score -= 330 + bishopTable[63 - square];
                    break;
                case Piece.BR:
                    score -= 500 + rookTable[63 - square];
                    break;
                case Piece.BQ:
                    score -= 900 + queenTable[63 - square];
                    break;
            
                default:
                    break;
            }
        }

        return score;
    }

    

    
}
