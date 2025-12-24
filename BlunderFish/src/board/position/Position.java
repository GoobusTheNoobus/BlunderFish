/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position;

import java.util.ArrayDeque;
import java.util.Arrays;

import board.Piece;
import board.bitboards.Bitboards;
import board.bitboards.masks.*;
import board.position.state.MoveStack;
import utils.Utility;
import utils.Constants;
import utils.Timer;
import board.position.state.*;
import board.position.helper.FENParser;
import board.position.moves.AttackDetector;
import board.position.moves.Move;;
/**
 * Represents a chess position with board state, move number, and a move history "stack"
 * Uses both bitboard and mailbox representations for efficiency and speed
 * The bitboard index is the same as the piece constant (White Pawn's bitboard is bitboards[Piece.WP])
 */


public class Position {
    public String defaultPositionFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    // Move history for undoing moves
    public MoveStack moveStack = new MoveStack(256);

    // Board 
    public Board board = new Board();

    // Game states
    public GameState gameState = new GameState();

    /**
     * Loads a position from a FEN string
     * Note that invalid positions will still be accepted
     * @param fen is the FEN string or null for starting position
     */
    public Position(String fen) {
        if (fen.isEmpty() || fen == null) {
            FENParser.loadFEN(defaultPositionFen, board, gameState);
        } else {
            FENParser.loadFEN(fen, board, gameState);
        }
    }

    public Position() {
        FENParser.loadFEN(defaultPositionFen, board, gameState);
    }    

    public int pieceAt(int square) {
        return board.mailbox[square];
    }

    public boolean isSquareAttacked (int square, boolean white) {
        return AttackDetector.isSquareAttacked(board, square, white);
    }

    public boolean isInCheck() {
        int kingIndex = (gameState.whiteToMove) ? Long.numberOfTrailingZeros(board.bitboards[Piece.WK]) : Long.numberOfTrailingZeros(board.bitboards[Piece.BK]);
        return isSquareAttacked(kingIndex, !gameState.whiteToMove);
    }

    public boolean canCastle (boolean kingSide) {
        if (isInCheck()) {
            return false;
        }

        if (kingSide) {
            if (gameState.whiteToMove) {
                if (CastlingRights.hasRight(gameState.castlingRights, CastlingRights.WHITE_KINGSIDE_CASTLING_MASK)) 
                    return false;

                if (isSquareAttacked(5, false)) 
                    return false;
                
                if (isSquareAttacked(6, false)) 
                    return false;
            } else {
                if (CastlingRights.hasRight(gameState.castlingRights, CastlingRights.BLACK_KINGSIDE_CASTLING_MASK)) 
                    return false;

                if (isSquareAttacked(61, true))
                    return false;
                
                if (isSquareAttacked(62, true))
                    return false;
            }
        } else {
            if (gameState.whiteToMove) {
                if (CastlingRights.hasRight(gameState.castlingRights, CastlingRights.WHITE_QUEENSIDE_CASTLING_MASK))
                    return false;
                if ((isSquareAttacked(2, false)))
                    return false;
                if ((isSquareAttacked(3, false)))
                    return false;
            } else {
                if (CastlingRights.hasRight(gameState.castlingRights, CastlingRights.BLACK_QUEENSIDE_CASTLING_MASK)) 
                    return false;
                if ((isSquareAttacked(58, true)))
                    return false;
                if ((isSquareAttacked(59, true)))
                    return false;
            }
        }
        return true;
    }

    public long fetchBitboard (int index) {
        return board.bitboards[index];
    }
    

    // helpers
    public void printPosition() {
        for (int rank = 7; rank >= 0; rank--) {
            System.out.print((rank + 1) + " ");
            for (int file = 0; file < 8; file++) {
                switch (board.mailbox[rank << 3 | file]) {
                    case Piece.WP:
                        System.out.print(Constants.WHITE_PAWN_CHAR);
                        break;
                    case Piece.BP:
                        System.out.print(Constants.BLACK_PAWN_CHAR);
                        break;
                    case Piece.WN:
                        System.out.print(Constants.WHITE_KNIGHT_CHAR);
                        break;
                    case Piece.BN:
                        System.out.print(Constants.BLACK_KNIGHT_CHAR);
                        break;
                    case Piece.WB:
                        System.out.print(Constants.WHITE_BISHOP_CHAR);
                        break;
                    case Piece.BB:
                        System.out.print(Constants.BLACK_BISHOP_CHAR);
                        break;
                    case Piece.WR:
                        System.out.print(Constants.WHITE_ROOK_CHAR);
                        break;
                    case Piece.BR:
                        System.out.print(Constants.BLACK_ROOK_CHAR);
                        break;
                    case Piece.WQ:
                        System.out.print(Constants.WHITE_QUEEN_CHAR);
                        break;
                    case Piece.BQ:
                        System.out.print(Constants.BLACK_QUEEN_CHAR);
                        break;
                    case Piece.WK:
                        System.out.print(Constants.WHITE_KING_CHAR);
                        break;
                    case Piece.BK:
                        System.out.print(Constants.BLACK_KING_CHAR);
                        break;
                    case Piece.NONE:
                        System.out.print(".");
                        break;
                    default:
                        // Yeah about that
                        throw new IllegalStateException("Oh noeserz!!! it seems like this position has a bit of weird stuff in it, and therefore, putting me in a bit of a pickle. I love pickles by the way, its really good. if you disagree, i will eat you instead. anyways, basically, in this position's mailbox, there is a weird piece. it looks, like, uhhh, like an elephant. wait, there is an elephant in chess right? oh right wrong variants. elephants are w tho. in chess pretty sure its a bishop. idk, btw since im so nice, here is the piece: " + board.mailbox[rank << 3 | file]);
                    
                }
                System.out.print(" ");
            }
            
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
        System.out.println("Side to move: " + (gameState.whiteToMove ? "White" : "Black"));
        System.out.println("En Croissant Square: " + gameState.enPassantSquare);
    }

    public void setSquareToPiece(int piece, int square) {

        int oldPiece = board.mailbox[square];
        long squareMask = 1L << square;

        // 1. Remove old piece (if any)
        if (oldPiece != Piece.NONE) {
            board.bitboards[oldPiece - 1] &= ~squareMask;

            if (oldPiece <= Piece.WK) {
                board.whitePieces &= ~squareMask;
            } else {
                board.blackPieces &= ~squareMask;
            }

            board.occupied &= ~squareMask;
        }

        // 2. Place new piece (if any)
        board.mailbox[square] = piece;

        if (piece != Piece.NONE) {
            board.bitboards[piece - 1] |= squareMask;

            if (piece <= Piece.WK) {
                board.whitePieces |= squareMask;
            } else {
                board.blackPieces |= squareMask;
            }

            board.occupied |= squareMask;
        }
    }


    

    // This is just for debugging purposes. This class is not meant to run on its own
    public static void main(String[] args) {
        
        
    }
    
}
