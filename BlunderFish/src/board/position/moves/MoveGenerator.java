/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves;

import java.util.Arrays;
import board.*;
import board.bitboards.Bitboards;
import board.position.Position;


public class MoveGenerator {
    // Moves
    public static int[] pseudoLegalMoves = new int[512];
    public static int[] legalMoves = new int[512];

    // Counters
    public static int pseudoLegalMovesCounter;
    public static int legalMovesCounter;

    private static void appendPseudoLegalMove (int move) {
        pseudoLegalMoves[pseudoLegalMovesCounter++] = move;
    }

    /* private static void appendLegalMove (long move) {
        legalMoves[legalMovesCounter++] = move;
    } */

    public static void clearArrays() {
        Arrays.fill(pseudoLegalMoves, 0);
        Arrays.fill(legalMoves, 0);

        pseudoLegalMovesCounter = 0;
        legalMovesCounter = 0;
    }
    public static void generateLegalMoves (Position pos) {
        generatePseudoLegalMoves(pos);
        for (int i = 0; i < pseudoLegalMovesCounter; i++) {
            
        }
    }

    // PSEUDO-LEGAL MOVE GENERATION: CRINGE CODE AHEAD !!!

    private static void generatePawnMoves (Position pos, boolean white) {
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
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WN, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WB, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WR, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WQ, false, false));
                    } else {
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                    }

                    // Now for double pawn push
                    if (fromRank == 1) {
                        int doublePushTo = from + 16;
                        if (!isOccupied(pos.board.occupied, doublePushTo))
                            appendPseudoLegalMove(Move.createMove(pos, from, doublePushTo, Piece.NONE, false, false));
                    }
                    
                }

                // Captures
                long capturedBitboard = pos.board.blackPieces & Bitboards.WHITE_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank + 1 == 7) {
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WN, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WB, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WR, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.WQ, false, false));
                    }else 
                    appendPseudoLegalMove(Move.createMove(pos, from, lsb, Piece.NONE, false, false));
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.gameState.enPassantSquare;
                if ((enPassantBitboard & Bitboards.WHITE_PAWN_ATTACKS[from]) != 0L) {
                    appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, true));
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
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BN, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BB, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BR, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BQ, false, false));
                    } else {
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                    }

                    // Double Push
                    if (fromRank == 6) {
                        int doublePushTo = from - 16;
                        if (!isOccupied(pos.board.occupied, doublePushTo)) {
                            appendPseudoLegalMove(Move.createMove(pos, from, doublePushTo, Piece.NONE, false, false));
                        }
                    }
                }

                // Captures YAEY

                long capturedBitboard = pos.board.whitePieces & Bitboards.BLACK_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank + 1 == 7) {
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BN, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BB, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BR, false, false));
                        appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.BQ, false, false));
                    }else 
                    appendPseudoLegalMove(Move.createMove(pos, from, lsb, Piece.NONE, false, false));
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.gameState.enPassantSquare;
                if ((enPassantBitboard & Bitboards.BLACK_PAWN_ATTACKS[from]) != 0L) {
                    appendPseudoLegalMove(Move.createMove(pos, from, pos.gameState.enPassantSquare, Piece.NONE, false, true));
                }

                pieces &= pieces - 1;

            }

            

            
        }
    }

    private static void generateKnightMoves (Position pos, boolean white) {
        long pieces = white? pos.fetchBitboard(Piece.WN): pos.fetchBitboard(Piece.BN);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KNIGHT_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateKingMoves (Position pos, boolean white) {
        long pieces = white? pos.fetchBitboard(Piece.WK): pos.fetchBitboard(Piece.BK);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KING_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateRookMoves (Position pos, boolean white) {
        long pieces = white? pos.fetchBitboard(Piece.WR): pos.fetchBitboard(Piece.BR);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
    
            long attackBitboard = Bitboards.getRookAttack(pos.board.occupied, from) & ~friendlies;
            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    public static void generateBishopMoves (Position pos, boolean white) {
        long pieces = white? pos.fetchBitboard(Piece.WB): pos.fetchBitboard(Piece.BB);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
            long attackBitboard = Bitboards.getBishopAttack(pos.board.occupied, from) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    private static void generateQueenMoves (Position pos, boolean white) {
        long pieces = white? pos.fetchBitboard(Piece.WQ): pos.fetchBitboard(Piece.BQ);
        long friendlies = white? pos.board.whitePieces: pos.board.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);

            long attackBitboard = (Bitboards.getBishopAttack(pos.board.occupied, from) | Bitboards.getRookAttack(pos.board.occupied, from)) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createMove(pos, from, to, Piece.NONE, false, false));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    public static void generatePseudoLegalMoves (Position pos) {
        if (pos.gameState.whiteToMove) {
            generatePawnMoves(pos, true);
            generateBishopMoves(pos, true);
            generateKnightMoves(pos, true);
            generateRookMoves(pos, true);
            generateQueenMoves(pos, true);
            generateKingMoves(pos, true);
        }

        else {
            generatePawnMoves(pos, false);
            generateBishopMoves(pos, false);
            generateKnightMoves(pos, false);
            generateRookMoves(pos, false);
            generateQueenMoves(pos, false);
            generateKingMoves(pos, false);
        }

    }
    
    
    

    private static boolean isOccupied (long occupiedBoard, int square) {
        long mask = 1L << square;
        if ((occupiedBoard & mask) != 0) {
            return true;
        } return false;
    }
    
    /* Helper Functions for creating and formatting move binary */
    
    
    public static void printMoveList () {
    
        for (int i = 0; i < pseudoLegalMovesCounter; i ++) {
            System.out.println(Move.toString(pseudoLegalMoves[i]));
            
        }
        System.out.printf("There are %d moves in PseudoLegalMove", pseudoLegalMovesCounter);
    }

} 
