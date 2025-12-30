/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves.helper;

import board.position.Piece;
import board.position.state.*;

public class MoveUndoer {
    public static void unCastle(Board board, boolean white, boolean kingside) {
        if (white) {
            if (kingside) {
                clearSquare(board, 6);
                clearSquare(board, 5);
                setPiece(board, 4, Piece.WK);
                setPiece(board, 7, Piece.WR);
            } else {
                clearSquare(board, 2);
                clearSquare(board, 3);
                setPiece(board, 4, Piece.WK);
                setPiece(board, 0, Piece.WR); 
            }
        } else {
            if (kingside) {
                clearSquare(board, 62);
                clearSquare(board, 61);
                setPiece(board, 60, Piece.BK);
                setPiece(board, 63, Piece.BR);
            } else {
                clearSquare(board, 58);
                clearSquare(board, 59);
                setPiece(board, 60, Piece.BK);
                setPiece(board, 56, Piece.BR);
            }
        }
    }

    public static void undoNormalMove (int from, int to, GameState state, Board board) {
        // Copy piece first
        setPiece(board, from, board.mailbox[to]);
        // Clear old square
        clearSquare(board, to);

    }

    public static void undoCapture (int from, int to, int piece, GameState state, Board board) {
        // Copy piece first
        setPiece(board, from, board.mailbox[to]);
        // Clear old square
        clearSquare(board, to);
        // Set new piece
        setPiece(board, to, piece);

    }

    public static void setPiece (Board board, int square, int piece) {
        board.mailbox[square] = piece;
        board.bitboards[piece] |= 1L << square;
    }
    public static void clearSquare (Board board, int square) {
        int index = board.mailbox[square];
        board.mailbox[square] = Piece.NONE;
        board.bitboards[index] &= ~(1L << square); 
    }
    public static void switchPiece (GameState state, Board board, int square, int piece ) {
        clearSquare(board, square);

        setPiece(board, square, piece);
    }
}
