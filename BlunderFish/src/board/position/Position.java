/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position;

import utils.Constants;
import board.position.state.*;
import board.position.helper.FENParser;
import board.position.helper.PositionPrinter;
import board.position.moves.MoveGenerator;
import board.position.moves.MoveHandler;
import board.position.moves.helper.AttackDetector;


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
        return isKingAttacked(gameState.whiteToMove);
    }

    public boolean isKingCapturable () {
        return isKingAttacked(!gameState.whiteToMove);
    }
    public boolean isKingAttacked (boolean isWhiteKing) {
        if (isWhiteKing) {
            int kingIndex = Long.numberOfTrailingZeros(board.bitboards[Piece.WK]);
            if (board.bitboards[Piece.WK] == 0L) {
                printPosition();
                moveStack.printStack();
                throw new IllegalArgumentException("no white king");
                
            }
            return isSquareAttacked(kingIndex, false);
        } else {
            int kingIndex = Long.numberOfTrailingZeros(board.bitboards[Piece.BK]);
            if (board.bitboards[Piece.BK] == 0L) {
                printPosition();
                throw new IllegalArgumentException("no black king");
            }
            return isSquareAttacked(kingIndex, true);
        }
    }

    public boolean canCastle (boolean kingSide) {
        if (kingSide) {
            if (gameState.whiteToMove) {
                if (gameState.hasRight(Constants.WHITE_KINGSIDE_CASTLING_MASK)) 
                    return false;

                if ((board.occupied & Constants.WK_CASTLING_BETWEEN_MASK) != 0L) 
                    return false;

                if (!(pieceAt(7) == Piece.WR) || !(pieceAt(4) == Piece.WK))
                    return false;

                if (isSquareAttacked(5, false)) 
                    return false;
                
                if (isSquareAttacked(6, false)) 
                    return false;


            } else {
                if (gameState.hasRight(Constants.BLACK_KINGSIDE_CASTLING_MASK)) 
                    return false;

                if ((board.occupied & Constants.BK_CASTLING_BETWEEN_MASK) != 0L) 
                    return false;

                if (!(pieceAt(63) == Piece.WR) || !(pieceAt(60) == Piece.WK))
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

                if ((board.occupied & Constants.WQ_CASTLING_BETWEEN_MASK) != 0L) 
                    return false;

                if (!(pieceAt(0) == Piece.WR) || !(pieceAt(4) == Piece.WK))
                    return false;

                if ((isSquareAttacked(2, false)))
                    return false;


                if ((isSquareAttacked(3, false)))
                    return false;
            } else {
                if (gameState.hasRight(Constants.BLACK_QUEENSIDE_CASTLING_MASK)) 
                    return false;

                if ((board.occupied & Constants.BQ_CASTLING_BETWEEN_MASK) != 0L) 
                    return false;

                if (!(pieceAt(56) == Piece.WR) || !(pieceAt(60) == Piece.WK))
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

    public void makeMove (long move, boolean switchSide) {
        moveStack.push(move);

        MoveHandler.makeMove(board, gameState, move, switchSide);
        
        updateOccupancy();
    }

    public void undoMove (boolean switchSide) {
        long move = moveStack.pop();

        MoveHandler.undoMove(board, gameState, move, switchSide);
        
        updateOccupancy();
    }

    public void updateOccupancy () {
        long[] bitboards = board.bitboards;
        board.whitePieces = bitboards[Piece.WP] | bitboards[Piece.WN] | bitboards[Piece.WB] | bitboards[Piece.WR] | bitboards[Piece.WQ] | bitboards[Piece.WK];
        board.blackPieces = bitboards[Piece.BP] | bitboards[Piece.BN] | bitboards[Piece.BB] | bitboards[Piece.BR] | bitboards[Piece.BQ] | bitboards[Piece.BK];

        board.occupied = board.whitePieces | board.blackPieces;
    }
    

    // helpers
    public void printPosition() {
        PositionPrinter.printBoardState(board);
        PositionPrinter.printGameState(gameState);
    }

    @Override
    public Position clone() throws CloneNotSupportedException {
        Position newPos = new Position();

        newPos.board = board.clone();
        newPos.gameState = gameState.clone();
        newPos.moveStack = moveStack.clone();

        return newPos;
    }

    // This is just for debugging purposes. This class is not meant to run on its own
    public static void main(String[] args) {
        Position pos = new Position("rnbqkbnr/pppp1p1p/8/6N1/4P3/8/PPPP3p/RNBQKBR1 b Qkq - 1 6");
        
        pos.printPosition();
        
        MoveGenerator.generateLegalMoves(pos);

        
        
    }
    
}
