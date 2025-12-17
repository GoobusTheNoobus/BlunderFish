package gen;

import java.util.Arrays;
import board.*;
import utils.Constants;


public class MoveGenerator {
    public static long[] pseudoLegalMoves = new long[256];
    public static int pseudoLegalMovesCounter;
    public static long[] legalMoves = new long[256];
    public static int legalMovesCounter;
    
    public static void generateLegalMoves (Position pos) {

    }

    // PSEUDO-LEGAL MOVE GENERATION: CRINGE CODE AHEAD !!!

    private static void generatePawnMoves (Position pos, boolean white) {
        if (white) {
            long pieces = pos.bitboards[Constants.WP_BITBOARD];
            
            while (pieces != 0L) {
                // Get a pawn
                int from = Long.numberOfTrailingZeros(pieces);
                int fromRank = from >> 3;
                

                // Single Pawn Push

                int to = from + 8;

                if (!isOccupied(pos.occupied, to)) { // Front square isnt occupied 
                    if (fromRank == 6) {
                        addLongPL(createPromotionMove(pos, from, to, 0));
                        addLongPL(createPromotionMove(pos, from, to, 1));
                        addLongPL(createPromotionMove(pos, from, to, 2));
                        addLongPL(createPromotionMove(pos, from, to, 3));
                    } else {
                        addLongPL(createNormalMove(pos, from, to));
                    }

                    // Now for double pawn push
                    if (fromRank == 1) {
                        int doublePushTo = from + 16;
                        if (!isOccupied(pos.occupied, doublePushTo))
                            addLongPL(createNormalMove(pos, from, doublePushTo));
                    }
                    
                }

                // Captures
                long capturedBitboard = pos.blackPieces & Bitboards.WHITE_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    addLongPL(createNormalMove(pos, from, lsb));
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.enPassantSquare;
                if ((enPassantBitboard & Bitboards.WHITE_PAWN_ATTACKS[from]) != 0L) {
                    addLongPL(createEnPassantMove(pos, from, pos.enPassantSquare));
                }

                pieces &= pieces - 1;
            }
        } else {
            long pieces = pos.bitboards[Constants.BP_BITBOARD];

            while (pieces != 0L) {
                // Get pawn
                int from = Long.numberOfTrailingZeros(pieces);
                int fromRank = from >> 3;
                
                // Single Push

                int to = from - 8;

                if (!isOccupied(pos.occupied, to)) {
                    if (fromRank == 1) { // Promotion
                        addLongPL(createPromotionMove(pos, from, to, 0));
                        addLongPL(createPromotionMove(pos, from, to, 1));
                        addLongPL(createPromotionMove(pos, from, to, 2));
                        addLongPL(createPromotionMove(pos, from, to, 3));
                    } else {
                        addLongPL(createNormalMove(pos, from, to));
                    }

                    // Double Push
                    if (fromRank == 6) {
                        int doublePushTo = from - 16;
                        if (!isOccupied(pos.occupied, doublePushTo)) {
                            addLongPL(createNormalMove(pos, from, doublePushTo));
                        }
                    }
                }

                // Captures YAEY

                long capturedBitboard = pos.whitePieces & Bitboards.BLACK_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    addLongPL(createNormalMove(pos, from, lsb));
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.enPassantSquare;
                if ((enPassantBitboard & Bitboards.BLACK_PAWN_ATTACKS[from]) != 0L) {
                    addLongPL(createEnPassantMove(pos, from, pos.enPassantSquare));
                }

                pieces &= pieces - 1;

            }

            

            
        }
    }

    private static void generateKnightMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Constants.WN_BITBOARD]: pos.bitboards[Constants.BN_BITBOARD];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KNIGHT_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                addLongPL(createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateKingMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Constants.WK_BITBOARD]: pos.bitboards[Constants.BK_BITBOARD];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
                
            long attackBitboard = Bitboards.KING_ATTACKS[from] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                addLongPL(createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }

            pieces &= pieces - 1;
        }
        
    }

    private static void generateRookMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Constants.WR_BITBOARD]: pos.bitboards[Constants.BR_BITBOARD];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
            int index = Bitboards.magicHash(pos.occupied & Bitboards.ROOK_MASKS[from], Constants.ROOK_MAGIC_NUMBERS[from], Long.bitCount(Bitboards.ROOK_MASKS[from]));
            long attackBitboard = Bitboards.ROOK_ATTACKS[from][index] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                addLongPL(createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    private static void generateBishopMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Constants.WB_BITBOARD]: pos.bitboards[Constants.BB_BITBOARD];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
            int index = Bitboards.magicHash(pos.occupied & Bitboards.BISHOP_MASKS[from], Constants.BISHOP_MAGIC_NUMBERS[from], Long.bitCount(Bitboards.BISHOP_MASKS[from]));
            long attackBitboard = Bitboards.BISHOP_ATTACKS[from][index] & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                addLongPL(createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    private static void generateQueenMoves (Position pos, boolean white) {
        long pieces = white? pos.bitboards[Constants.WQ_BITBOARD]: pos.bitboards[Constants.BQ_BITBOARD];
        long friendlies = white? pos.whitePieces: pos.blackPieces;

        while (pieces != 0L) {
            int from = Long.numberOfTrailingZeros(pieces);
            int rookIndex = Bitboards.magicHash(pos.occupied & Bitboards.ROOK_MASKS[from], Constants.ROOK_MAGIC_NUMBERS[from], Long.bitCount(Bitboards.ROOK_MASKS[from]));
            int bishopIndex = Bitboards.magicHash(pos.occupied & Bitboards.BISHOP_MASKS[from], Constants.BISHOP_MAGIC_NUMBERS[from], Long.bitCount(Bitboards.BISHOP_MASKS[from]));

            long attackBitboard = (Bitboards.BISHOP_ATTACKS[from][bishopIndex] | Bitboards.ROOK_ATTACKS[from][rookIndex]) & ~friendlies;

            while (attackBitboard != 0L) {
                int to = Long.numberOfTrailingZeros(attackBitboard);
                addLongPL(createNormalMove(pos, from, to));
                attackBitboard &= attackBitboard - 1;
            }
            pieces &= pieces - 1;
        }
    }

    public static void generatePsuedoLegalMoves (Position pos) {
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
    
    private static long createNormalMove (Position pos, int from, int to) {
        int pieceMoved = Math.abs(pos.pieceAt(from));
        int pieceCaptured = Math.abs(pos.pieceAt(to));
        boolean isCaptured = pieceCaptured != 0;
        boolean isDoublePush = (pieceMoved == 1 && ((to >>> 3) - (from >>> 3)) == 2) || (pieceMoved == -1 && ((from >>> 3) - (to >>> 3) == 2));
        long move = Move.createMove(from,
             to, 
             pieceMoved, 
             pieceCaptured, 
             isCaptured, 
             isDoublePush,
             false, 
             false, 
             false,
             0, 
             pos.castlingRights, 
             pos.enPassantSquare
            );
        return move;
    }

    private static long createPromotionMove (Position pos, int from, int to, int promotion) {
        int piecemoved = pos.pieceAt(from);
        int pieceCaptured = pos.pieceAt(to);
        boolean isCaptured = pieceCaptured != 0;
        long move = Move.createMove(from, 
            to, 
            piecemoved, 
            pieceCaptured, 
            isCaptured, 
            false,
            false, 
            false, 
            true, 
            promotion, 
            pos.castlingRights, 
            pos.enPassantSquare
        );
        return move;
    }

    private static long createEnPassantMove (Position pos, int from, int to) {
        int piecemoved = pos.pieceAt(from);
        int pieceCaptured = (pos.whiteToMove) ? -1: 1;
        boolean isCaptured = true;
        long move = Move.createMove(
            from, 
            to, 
            piecemoved, 
            pieceCaptured, 
            isCaptured, 
            false, 
            true, 
            false, 
            false, 
            0, 
            pos.castlingRights, 
            pos.enPassantSquare
        );
        return move;
    }

    private static long createCastlingMove (Position pos, boolean kingSide) {
        boolean sideToMove = pos.whiteToMove;
        int toRank = sideToMove ? 0: 7;
        int toFile = kingSide ? 6 : 2;
        int toSquare = (toRank << 3) | toFile; 
        int fromSquare = sideToMove ? 4 : 59;
        long move = Move.createMove(
            fromSquare, 
            toSquare, 
            sideToMove ? 6 : -6, 
            0, 
            false, 
            false, 
            false, 
            true, 
            false, 
            0, 
            pos.castlingRights, 
            pos.enPassantSquare);
        return move;
    }

    private static void addLongPL (long schlong) {
        pseudoLegalMoves[pseudoLegalMovesCounter] = schlong;
        pseudoLegalMovesCounter ++;
    }

    private static void addLongL (long schlong) {
        legalMoves[legalMovesCounter] = schlong;
        legalMovesCounter ++;
    }

    public static void clearArrays () {
        Arrays.fill(pseudoLegalMoves, 0L);
        pseudoLegalMovesCounter = 0;
        Arrays.fill(legalMoves, 0L);
        legalMovesCounter = 0;
    }

    
    
} 
