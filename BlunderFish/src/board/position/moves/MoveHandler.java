/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves;

import board.position.Piece;
import board.position.moves.helper.Move;
import board.position.moves.helper.MoveMaker;
import board.position.moves.helper.MoveUndoer;
import board.position.state.Board;
import board.position.state.GameState;
import utils.Constants;

public class MoveHandler {
    public static void undoMove (Board board, GameState state, long move, boolean switchSide) {
        if (switchSide) state.switchSide();

        int flag = Move.getFlag(move);
        int from = Move.getFromSquare(move);
        int to = Move.getToSquare(move);
        int previousEnPassantSquare = Move.getPreviousEnPassantSquare(move);

        int promotedPiece = Move.getPromotedPiece(move);

        int capturedPiece = Move.getCapturedPiece(move);

        int previousCastlingRights = Move.getCastlingRights(move);
        // MoveUndoer::unCastle(Board board, boolean white, boolean kingSide)
        switch(flag) {
            
        }

        if (flag == Constants.CASTLING_KINGSIDE_FLAG) {
            if (state.whiteToMove) {
                MoveUndoer.unCastle(board, true, true);
            } else {
                MoveUndoer.unCastle(board, false, true);
            }

            
        } 
        
        else if (flag == Constants.CASTLING_QUEENSIDE_FLAG) {
            if (state.whiteToMove) {
                MoveUndoer.unCastle(board, true, false);
            } else {
                MoveUndoer.unCastle(board, false, false);
            }

        
        } 
        
        else if (flag == Constants.EN_PASSANT_FLAG) {
            MoveUndoer.undoNormalMove(from, to, state, board);
            if (state.whiteToMove)
                MoveUndoer.setPiece(board, to - 8, Piece.BP);
            else
                MoveUndoer.setPiece(board, to + 8, Piece.WP);
            
            
        } 
        
        else if (flag == Constants.DOUBLE_PAWN_PUSH) {
            MoveUndoer.undoNormalMove(from, to, state, board);
        } 
        
        else { // Normal Move/ Capture/ Promotion
            MoveUndoer.undoNormalMove(from, to, state, board);
            MoveUndoer.setPiece(board, to, capturedPiece);
            if (promotedPiece != Piece.NONE) {
                MoveUndoer.switchPiece(state, board, from, state.whiteToMove ? Piece.WP : Piece.BP);
            }
        }
        state.enPassantSquare = previousEnPassantSquare;
        state.castlingRights = previousCastlingRights;
        
    }

    public static void makeMove (Board board, GameState state, long move, boolean switchSide) {

        int flag = Move.getFlag(move);
        int from = Move.getFromSquare(move);
        int to = Move.getToSquare(move);
        int pieceMoved = board.mailbox[from];

        int promotedPiece = Move.getPromotedPiece(move);

        if (flag == Constants.CASTLING_KINGSIDE_FLAG) {
            MoveMaker.castle(true, state, board);

            state.enPassantSquare = 64;
            
        } 
        
        else if (flag == Constants.CASTLING_QUEENSIDE_FLAG) {
            MoveMaker.castle(false, state, board);

            state.enPassantSquare = 64;
            
        } 
        
        else if (flag == Constants.EN_PASSANT_FLAG) {
            MoveMaker.normalMove(from, to, state, board);
            MoveMaker.clearSquare(board, state.whiteToMove ? to - 8 : to + 8);

            state.enPassantSquare = 64;
            
        } 
        
        else if (flag == Constants.DOUBLE_PAWN_PUSH) {
            MoveMaker.normalMove(from, to, state, board);

            state.enPassantSquare = state.whiteToMove ? to - 8 : to + 8;
            
        } 

        
        
        else { // Normal Move/ Capture
            // Remove Rights if moved king or rook
            if (pieceMoved == Piece.WR) {
                if (from == 0)
                    state.removeRight(Constants.WHITE_QUEENSIDE_CASTLING_MASK);
                if (from == 7)
                    state.removeRight(Constants.WHITE_KINGSIDE_CASTLING_MASK);
            } else if (pieceMoved == Piece.BR) {
                if (from == 56)
                    state.removeRight(Constants.BLACK_QUEENSIDE_CASTLING_MASK);
                if (from == 63) 
                    state.removeRight(Constants.BLACK_KINGSIDE_CASTLING_MASK);
            } else if (pieceMoved == Piece.WK)
                state.removeRight(Constants.WHITE_KINGSIDE_CASTLING_MASK | Constants.WHITE_QUEENSIDE_CASTLING_MASK);
            else if (pieceMoved == Piece.BK)
                state.removeRight(Constants.BLACK_KINGSIDE_CASTLING_MASK | Constants.BLACK_QUEENSIDE_CASTLING_MASK);
            
            // Remove rights if moved onto rook starting square

            if (to == 0) state.removeRight(Constants.WHITE_QUEENSIDE_CASTLING_MASK);
            if (to == 7) state.removeRight(Constants.WHITE_KINGSIDE_CASTLING_MASK);
            if (to == 56) state.removeRight(Constants.BLACK_QUEENSIDE_CASTLING_MASK);
            if (to == 63) state.removeRight(Constants.BLACK_KINGSIDE_CASTLING_MASK);


            if (board.mailbox[to] == Piece.NONE) {
                MoveMaker.normalMove(from, to, state, board);
            } else {
                MoveMaker.captureMove(from, to, state, board);
            }

            if (promotedPiece != Piece.NONE) {
                MoveMaker.switchPiece(to, promotedPiece, state, board);

                assert pieceMoved == Piece.WP || pieceMoved == Piece.BP;
            }
            state.enPassantSquare = 64;
            
        
        }
        
        if (switchSide) state.switchSide();
    }
}
