/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves.helper;

import board.position.state.*;
import utils.Constants;
import board.position.Piece;;

public class MoveMaker {
    public static void castle (boolean kingside, GameState state, Board board) {
        switch (kingside) {
            case true:
                if (state.whiteToMove) {
                    clearSquare(board, 4);
                    clearSquare(board, 7);
                    setPiece(board, 5, Piece.WR);
                    setPiece(board, 6, Piece.WK);

                    state.removeRight(Constants.WHITE_CASTLING_RIGHTS);
                } else {
                    clearSquare(board, 60);
                    clearSquare(board, 63);
                    setPiece(board, 61, Piece.BR);
                    setPiece(board, 62, Piece.BK);

                    state.removeRight(Constants.BLACK_CASTLING_RIGHTS);
                }
                break;
            default:
                if (state.whiteToMove) {
                    clearSquare(board, 4);
                    clearSquare(board, 0);
                    setPiece(board, 3, Piece.WR);
                    setPiece(board, 2, Piece.WK);

                    state.removeRight(Constants.WHITE_CASTLING_RIGHTS);
                } else {
                    clearSquare(board, 60);
                    clearSquare(board, 56);
                    setPiece(board, 59, Piece.BR);
                    setPiece(board, 58, Piece.BK);

                    state.removeRight(Constants.BLACK_CASTLING_RIGHTS);
                }
        }

        
        
    }
    
    public static void normalMove (int from, int to, GameState state, Board board) {
        // Copy piece first
        setPiece(board, to, board.mailbox[from]);
        // Clear old square
        clearSquare(board, from);

    }

    public static void captureMove (int from, int to, GameState state, Board board) {
        // Delete captured piece
        clearSquare(board, to);
        // Copy piece
        setPiece(board, to, board.mailbox[from]);
        // Clear old square
        clearSquare(board, from);
    }

    public static void switchPiece (int square, int piece, GameState state, Board board) {
        clearSquare(board, square);

        setPiece(board, square, piece);
    }

    public static void setPiece (Board board, int square, int piece) {
        board.mailbox[square] = piece;
        board.bitboards[piece] |= Constants.SQUARE_MASKS[square];
    }
    public static void clearSquare (Board board, int square) {
        int index = board.mailbox[square];
        board.mailbox[square] = Piece.NONE;
        board.bitboards[index] &= ~Constants.SQUARE_MASKS[square]; 
    }
}
