/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.state;

import java.util.Arrays;

import board.position.Piece;

public class Board {
    // Bitboards for each piece type: 0th bitboard is empty
    public long[] bitboards = new long[13];

    // Mailbox array repr for piece lookup
    public int[] mailbox = new int[64];

    // Extra bitboards for efficiency
    public long whitePieces;
    public long blackPieces;
    public long occupied;

    // This is for both bitboards and mailbox
    public void setSquareToPiece(int piece, int square) {

        int oldPiece = mailbox[square];
        long squareMask = 1L << square;

        // 1. Remove old piece (if any)
        if (oldPiece != Piece.NONE) {
            bitboards[oldPiece - 1] &= ~squareMask;

            if (oldPiece <= Piece.WK) {
                whitePieces &= ~squareMask;
            } else {
                blackPieces &= ~squareMask;
            }

            occupied &= ~squareMask;
        }

        // 2. Place new piece (if any)
        mailbox[square] = piece;

        if (piece != Piece.NONE) {
            bitboards[piece - 1] |= squareMask;

            if (piece <= Piece.WK) {
                whitePieces |= squareMask;
            } else {
                blackPieces |= squareMask;
            }

            occupied |= squareMask;
        }
    }
    public void clear() {
        Arrays.fill(bitboards, 0L);
        Arrays.fill(mailbox, Piece.NONE);

        whitePieces = 0L;
        blackPieces = 0L;
        
        occupied = 0L;
    }
    public void updateOccupancy () {
        whitePieces = bitboards[Piece.WP] | bitboards[Piece.WN] | bitboards[Piece.WB] | bitboards[Piece.WR] | bitboards[Piece.WQ] | bitboards[Piece.WK];
        blackPieces = bitboards[Piece.BP] | bitboards[Piece.BN] | bitboards[Piece.BB] | bitboards[Piece.BR] | bitboards[Piece.BQ] | bitboards[Piece.BK];

        occupied = whitePieces | blackPieces;
    }
}
