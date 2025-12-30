/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves;

import board.bitboards.Bitboards;
import board.position.Piece;
import board.position.Position;
import board.position.moves.helper.Move;


public class MoveGenerator {
    // Moves
    public static MoveBuffer pseudoLegalMoves = new MoveBuffer(512);
    public static MoveBuffer legalMoves = new MoveBuffer(512);

    public static void generateLegalMoves (Position pos, MoveBuffer buff) {
        pseudoLegalMoves.clear();
        buff.clear();

        generatePseudoLegalMoves(pos);
        for (int index = 0; index < pseudoLegalMoves.size(); index++) {
            long move = pseudoLegalMoves.getElementByIndex(index);

            pos.makeMove(move, false);
            if (!pos.isInCheck()) {
                buff.append(move);
            }
                
            pos.undoMove(false);
        }

        if (pos.isInCheck()) return;

        if (pos.canCastle(false)) buff.append(Move.createMove(pos, pos.gameState.whiteToMove ? 4 : 60, pos.gameState.whiteToMove ? 2 : 58, 0, true, false));

        if (pos.canCastle(true)) buff.append(Move.createMove(pos, pos.gameState.whiteToMove ? 4 : 60, pos.gameState.whiteToMove ? 6 : 62, 0, true, false));
    }

    public static void generateLegalMoves (Position pos) {
        MoveBuffer buff = legalMoves;

        pseudoLegalMoves.clear();
        buff.clear();

        generatePseudoLegalMoves(pos);
        for (int index = 0; index < pseudoLegalMoves.size(); index++) {
            long move = pseudoLegalMoves.getElementByIndex(index);

            pos.makeMove(move, false);

            boolean isStillCheck = pos.isInCheck();

            if (!isStillCheck) {
                buff.append(move);
                
            }  
            pos.undoMove(false);
        }

        
    }
    // PSEUDO-LEGAL MOVE GENERATION: CRINGE CODE AHEAD !!!

    private static void generatePawnMoves (Position pos, boolean white, MoveBuffer buff) {
        if (white) {
            long pieces = pos.fetchBitboard(Piece.WP);
            
            while (pieces != 0L) {
                // Get a pawn
                int from = Long.numberOfTrailingZeros(pieces);
                int fromRank = from >>> 3;
                

                // Single Pawn Push

                int to = from + 8;

                if (!isOccupied(pos.board.occupied, to)) { // Front square isnt occupied 
                    if (fromRank == 6) {
                        // Add promotion moves 
                        buff.append(Move.createMove(pos, from, to, Piece.WN, false, false));
                        buff.append(Move.createMove(pos, from, to, Piece.WB, false, false));
                        buff.append(Move.createMove(pos, from, to, Piece.WR, false, false));
                        buff.append(Move.createMove(pos, from, to, Piece.WQ, false, false));
                    } else {
                        buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                    }

                    // Now for double pawn push
                    if (fromRank == 1) {
                        int doublePushTo = from + 16;
                        if (!isOccupied(pos.board.occupied, doublePushTo))
                            buff.append(Move.createMove(pos, from, doublePushTo, Piece.NONE, false, false));
                    }
                    
                }

                // Captures
                long capturedBitboard = pos.board.blackPieces & Bitboards.WHITE_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank == 6) {
                        buff.append(Move.createMove(pos, from, lsb, Piece.WN, false, false));
                        buff.append(Move.createMove(pos, from, lsb, Piece.WB, false, false));
                        buff.append(Move.createMove(pos, from, lsb, Piece.WR, false, false));
                        buff.append(Move.createMove(pos, from, lsb, Piece.WQ, false, false));
                    }else {
                       buff.append(Move.createMove(pos, from, lsb, Piece.NONE, false, false)); 
                    }
                        capturedBitboard &= capturedBitboard - 1;
                }

                // Time for EN CROISSANT 🥐

                if (pos.gameState.enPassantSquare != 64) {
                    long enPassantBitboard = 1L << pos.gameState.enPassantSquare;
                    if ((enPassantBitboard & Bitboards.WHITE_PAWN_ATTACKS[from]) != 0L) {
                        buff.append(Move.createMove(pos, from, to, Piece.NONE, false, true));
                    }
                }

                

                pieces &= pieces - 1;
            }
        } else {
            long pieces = pos.fetchBitboard(Piece.BP);

            while (pieces != 0L) {
                // Get pawn
                int from = Long.numberOfTrailingZeros(pieces);
                int fromRank = from >>> 3;
                
                // Single Push

                int to = from - 8;

                if (!isOccupied(pos.board.occupied, to)) {
                    if (fromRank == 1) { // Promotion
                        buff.append(Move.createMove(pos, from, to, Piece.BN, false, false));
                        buff.append(Move.createMove(pos, from, to, Piece.BB, false, false));
                        buff.append(Move.createMove(pos, from, to, Piece.BR, false, false));
                        buff.append(Move.createMove(pos, from, to, Piece.BQ, false, false));
                    } else {
                        buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                    }

                    // Double Push
                    if (fromRank == 6) {
                        int doublePushTo = from - 16;
                        if (!isOccupied(pos.board.occupied, doublePushTo)) {
                            buff.append(Move.createMove(pos, from, doublePushTo, Piece.NONE, false, false));
                        }
                    }
                }

                // Captures YAEY

                long capturedBitboard = pos.board.whitePieces & Bitboards.BLACK_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank == 1) {
                        buff.append(Move.createMove(pos, from, lsb, Piece.BN, false, false));
                        buff.append(Move.createMove(pos, from, lsb, Piece.BB, false, false));
                        buff.append(Move.createMove(pos, from, lsb, Piece.BR, false, false));
                        buff.append(Move.createMove(pos, from, lsb, Piece.BQ, false, false));
                    }else {
                        buff.append(Move.createMove(pos, from, lsb, Piece.NONE, false, false));
                    }
                    capturedBitboard &= capturedBitboard - 1;
                }

                // Time for EN CROISSANT 🥐
                if (pos.gameState.enPassantSquare != 64) {
                    long enPassantBitboard = 1L << pos.gameState.enPassantSquare;
                    if ((enPassantBitboard & Bitboards.BLACK_PAWN_ATTACKS[from]) != 0L) {
                        buff.append(Move.createMove(pos, from, pos.gameState.enPassantSquare, Piece.NONE, false, true));
                    }
                }

                

                pieces &= pieces - 1;

            }
        }
    }

    private static void generateKnightMoves (Position pos, boolean white, MoveBuffer buff) {
        long pieces = white? pos.fetchBitboard(Piece.WN): pos.fetchBitboard(Piece.BN);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KNIGHT_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateKingMoves (Position pos, boolean white, MoveBuffer buff) {
        long pieces = white? pos.fetchBitboard(Piece.WK): pos.fetchBitboard(Piece.BK);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KING_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateRookMoves (Position pos, boolean white, MoveBuffer buff) {
        long pieces = white? pos.fetchBitboard(Piece.WR): pos.fetchBitboard(Piece.BR);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
    
            long attackBitboard = Bitboards.getRookAttack(pos.board.occupied, from) & ~friendlies;
            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    private static void generateBishopMoves (Position pos, boolean white, MoveBuffer buff) {
        long pieces = white? pos.fetchBitboard(Piece.WB): pos.fetchBitboard(Piece.BB);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
            long attackBitboard = Bitboards.getBishopAttack(pos.board.occupied, from) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    private static void generateQueenMoves (Position pos, boolean white, MoveBuffer buff) {
        long pieces = white? pos.fetchBitboard(Piece.WQ): pos.fetchBitboard(Piece.BQ);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);

            long attackBitboard = (Bitboards.getBishopAttack(pos.board.occupied, from) | Bitboards.getRookAttack(pos.board.occupied, from)) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                buff.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    public static void generatePseudoLegalMoves (Position pos, MoveBuffer buff) {
        pseudoLegalMoves.clear();
        if (pos.gameState.whiteToMove) {
            generatePawnMoves(pos, true, buff);
            generateBishopMoves(pos, true, buff);
            generateKnightMoves(pos, true, buff);
            generateRookMoves(pos, true, buff);
            generateQueenMoves(pos, true, buff);
            generateKingMoves(pos, true, buff);
        }

        else {
            generatePawnMoves(pos, false, buff);
            generateBishopMoves(pos, false, buff);
            generateKnightMoves(pos, false, buff);
            generateRookMoves(pos, false, buff);
            generateQueenMoves(pos, false, buff);
            generateKingMoves(pos, false, buff);
        }

    }

    public static void generatePseudoLegalMoves (Position pos) {
        MoveBuffer buff = pseudoLegalMoves;

        pseudoLegalMoves.clear();
        if (pos.gameState.whiteToMove) {
            generatePawnMoves(pos, true, buff);
            generateBishopMoves(pos, true, buff);
            generateKnightMoves(pos, true, buff);
            generateRookMoves(pos, true, buff);
            generateQueenMoves(pos, true, buff);
            generateKingMoves(pos, true, buff);
        }

        else {
            generatePawnMoves(pos, false, buff);
            generateBishopMoves(pos, false, buff);
            generateKnightMoves(pos, false, buff);
            generateRookMoves(pos, false, buff);
            generateQueenMoves(pos, false, buff);
            generateKingMoves(pos, false, buff);
        }

    }
    
    private static boolean isOccupied (long occupiedBoard, int square) {
        long mask = 1L << square;
        if ((occupiedBoard & mask) != 0) {
            return true;
        } return false;
    }
    

} 
