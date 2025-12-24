/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package gen;

import java.util.Arrays;
import board.*;
import board.bitboards.Bitboards;


public class MoveGenerator {
    // Moves
    public static long[] pseudoLegalMoves = new long[512];
    public static long[] legalMoves = new long[512];

    // Counters
    public static int pseudoLegalMovesCounter;
    public static int legalMovesCounter;

    private static void appendPseudoLegalMove (long move) {
        pseudoLegalMoves[pseudoLegalMovesCounter++] = move;
    }

    /* private static void appendLegalMove (long move) {
        legalMoves[legalMovesCounter++] = move;
    } */

    public static void clearArrays() {
        Arrays.fill(pseudoLegalMoves, 0L);
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
            long pieces = pos.bitboards[Piece.WP];
            
            while (pieces != 0L) {
                // Get a pawn
                int from = Long.numberOfTrailingZeros(pieces);
                int fromRank = from >>> 3;
                

                // Single Pawn Push

                int to = from + 8;

                if (!isOccupied(pos.occupied, to)) { // Front square isnt occupied 
                    if (fromRank == 6) {
                        // Add promotion moves 
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WN));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WB));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WR));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WQ));
                    } else {
                        appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                    }

                    // Now for double pawn push
                    if (fromRank == 1) {
                        int doublePushTo = from + 16;
                        if (!isOccupied(pos.occupied, doublePushTo))
                            appendPseudoLegalMove(Move.createNormalMove(pos, from, doublePushTo));
                    }
                    
                }

                // Captures
                long capturedBitboard = pos.blackPieces & Bitboards.WHITE_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank + 1 == 7) {
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WN));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WB));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WR));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.WQ));
                    }else 
                    appendPseudoLegalMove(Move.createNormalMove(pos, from, lsb));
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.enPassantSquare;
                if ((enPassantBitboard & Bitboards.WHITE_PAWN_ATTACKS[from]) != 0L) {
                    appendPseudoLegalMove(Move.createEnPassantMove(pos, from, pos.enPassantSquare));
                }

                pieces &= pieces - 1;
            }
        } else {
            long pieces = pos.bitboards[Piece.BP];

            while (pieces != 0L) {
                // Get pawn
                int from = Long.numberOfTrailingZeros(pieces);
                int fromRank = from >>> 3;
                
                // Single Push

                int to = from - 8;

                if (!isOccupied(pos.occupied, to)) {
                    if (fromRank == 1) { // Promotion
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BN));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BB));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BR));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BQ));
                    } else {
                        appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                    }

                    // Double Push
                    if (fromRank == 6) {
                        int doublePushTo = from - 16;
                        if (!isOccupied(pos.occupied, doublePushTo)) {
                            appendPseudoLegalMove(Move.createNormalMove(pos, from, doublePushTo));
                        }
                    }
                }

                // Captures YAEY

                long capturedBitboard = pos.whitePieces & Bitboards.BLACK_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank + 1 == 7) {
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BN));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BB));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BR));
                        appendPseudoLegalMove(Move.createPromotionMove(pos, from, to, Piece.BQ));
                    }else 
                    appendPseudoLegalMove(Move.createNormalMove(pos, from, lsb));
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.enPassantSquare;
                if ((enPassantBitboard & Bitboards.BLACK_PAWN_ATTACKS[from]) != 0L) {
                    appendPseudoLegalMove(Move.createEnPassantMove(pos, from, pos.enPassantSquare));
                }

                pieces &= pieces - 1;

            }

            

            
        }
    }

    private static void generateKnightMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Piece.WN]: pos.bitboards[Piece.BN];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KNIGHT_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateKingMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Piece.WK]: pos.bitboards[Piece.BK];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KING_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateRookMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Piece.WR]: pos.bitboards[Piece.BR];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
    
            long attackBitboard = Bitboards.getRookAttack(pos.occupied, from) & ~friendlies;
            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    public static void generateBishopMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Piece.WB]: pos.bitboards[Piece.BB];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
            long attackBitboard = Bitboards.getBishopAttack(pos.occupied, from) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    private static void generateQueenMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Piece.WQ]: pos.bitboards[Piece.BQ];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);

            long attackBitboard = (Bitboards.getBishopAttack(pos.occupied, from) | Bitboards.getRookAttack(pos.occupied, from)) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                appendPseudoLegalMove(Move.createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    public static void generatePseudoLegalMoves (Position pos) {
        if (pos.whiteToMove) {
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
