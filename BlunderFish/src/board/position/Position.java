/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position;

import javax.print.attribute.standard.PrinterInfo;

import board.bitboards.Bitboards;
import board.position.state.MoveStack;
import utils.Constants;
import utils.Utility;
import board.position.state.*;
import board.position.helper.FENParser;
import board.position.helper.PositionPrinter;
import board.position.moves.MoveHandler;
import board.position.moves.helper.AttackDetector;
import board.position.moves.helper.Move;
import board.position.moves.helper.MoveMaker;
import board.position.moves.helper.MoveUndoer;

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
        Bitboards.printBitboard(board.whitePieces);
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
    public boolean isKingCapturable () {
        int kingIndex = (gameState.whiteToMove) ? Long.numberOfTrailingZeros(board.bitboards[Piece.BK]) : Long.numberOfTrailingZeros(board.bitboards[Piece.WK]);
        return isSquareAttacked(kingIndex, gameState.whiteToMove);
    }

    public boolean canCastle (boolean kingSide) {
        if (isInCheck()) {
            return false;
        }

        if (kingSide) {
            if (gameState.whiteToMove) {
                if (gameState.hasRight(Constants.WHITE_KINGSIDE_CASTLING_MASK)) 
                    return false;

                if (isSquareAttacked(5, false)) 
                    return false;
                
                if (isSquareAttacked(6, false)) 
                    return false;
            } else {
                if (gameState.hasRight(Constants.BLACK_KINGSIDE_CASTLING_MASK)) 
                    return false;

                if (isSquareAttacked(61, true))
                    return false;
                
                if (isSquareAttacked(62, true))
                    return false;
            }
        } else {
            if (gameState.whiteToMove) {
                if (gameState.hasRight(Constants.WHITE_QUEENSIDE_CASTLING_MASK))
                    return false;
                if ((isSquareAttacked(2, false)))
                    return false;
                if ((isSquareAttacked(3, false)))
                    return false;
            } else {
                if (gameState.hasRight(Constants.BLACK_QUEENSIDE_CASTLING_MASK)) 
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

    public void makeMove (long move) {
        moveStack.push(move);

        MoveHandler.makeMove(board, gameState, move);
        
    }

    public void undoMove () {
        long move = moveStack.pop();

        MoveHandler.undoMove(board, gameState, move);
        
    }
    

    // helpers
    public void printPosition() {
        PositionPrinter.printBoardState(board);
        PositionPrinter.printGameState(gameState);
    }

    // This is just for debugging purposes. This class is not meant to run on its own
    public static void main(String[] args) {
        Position pos = new Position("rnbqkbnr/pppp1p1p/8/6N1/4P3/8/PPPP3p/RNBQKBR1 b Qkq - 1 6");
        
        pos.printPosition();
        pos.makeMove(Move.createMove(pos, Utility.getSquareIntFromString("h2"), Utility.getSquareIntFromString("g1"), Piece.BQ, false, false));
        
        pos.printPosition();

        pos.undoMove();

        pos.printPosition();
        
    }
    
}
