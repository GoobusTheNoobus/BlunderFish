/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves.helper;

import board.bitboards.Bitboards;
import board.bitboards.masks.FileMasks;
import board.position.Piece;
import board.position.state.Board;

public class AttackDetector {
    public static boolean isSquareAttacked (Board board, int square, boolean white) {
        if (square >= 64 ) {
            throw new IllegalArgumentException("square is " + square);
        }

        long mask = 1L << square;
        long pawns = white ? board.bitboards[Piece.WP] : board.bitboards[Piece.BP];
        long knights = white ? board.bitboards[Piece.WN] : board.bitboards[Piece.BN];
        long bishops = white ? board.bitboards[Piece.WB] : board.bitboards[Piece.BB];
        long rooks = white ? board.bitboards[Piece.WR] : board.bitboards[Piece.BR];
        long queens = white ? board.bitboards[Piece.WQ] : board.bitboards[Piece.BQ];
        long king = white ? board.bitboards[Piece.WK] : board.bitboards[Piece.BK];

        // Pawn Attacks

        if (white){
            if (((mask >>> 7) & pawns & ~FileMasks.A_FILE) != 0) return true;
            if (((mask >>> 9) & pawns & ~FileMasks.H_FILE) != 0) return true;
        } else {
            if (((mask << 7) & pawns & ~FileMasks.H_FILE) != 0) return true;
            if (((mask << 9) & pawns & ~FileMasks.A_FILE) != 0) return true;
        }

        // Knights Attacks
        if ((Bitboards.KNIGHT_ATTACKS[square] & knights) != 0) return true;

        // King Attacks
        if ((Bitboards.KING_ATTACKS[square] & king) != 0) return true;

        // Sliding Pieces: Magic (not actually magic just a big database) Tables
        long rookAttacks = Bitboards.getRookAttack(board.occupied, square);
        long bishopAttacks = Bitboards.getBishopAttack(board.occupied, square);

        if (((rookAttacks & rooks) != 0L) || ((rookAttacks & queens)  != 0L)) {
            return true;
        }

        if (((bishopAttacks & bishops) != 0L) || ((bishopAttacks & queens)  != 0L)) {
            return true;
        }
        
        
        return false;
    }
}
