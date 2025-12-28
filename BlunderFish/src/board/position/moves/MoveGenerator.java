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

    public static void generateLegalMoves (Position pos) {
        pseudoLegalMoves.clear();
        legalMoves.clear();

        generatePseudoLegalMoves(pos);
        for (int index = 0; index < pseudoLegalMoves.size(); index++) {
            long move = pseudoLegalMoves.getElementByIndex(index);

            pos.makeMove(move);
            if (!pos.isKingCapturable()) {
                legalMoves.append(move);
            }
            pos.printPosition();
            pos.undoMove();
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
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WN, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WB, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WR, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WQ, false, false));
                    } else {
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                    }

                    // Now for double pawn push
                    if (fromRank == 1) {
                        int doublePushTo = from + 16;
                        if (!isOccupied(pos.board.occupied, doublePushTo))
                            pseudoLegalMoves.append(Move.createMove(pos, from, doublePushTo, Piece.NONE, false, false));
                    }
                    
                }

                // Captures
                long capturedBitboard = pos.board.blackPieces & Bitboards.WHITE_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank + 1 == 7) {
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WN, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WB, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WR, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.WQ, false, false));
                    }else 
                    pseudoLegalMoves.append(Move.createMove(pos, from, lsb, Piece.NONE, false, false));
                    capturedBitboard &= capturedBitboard - 1;
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.gameState.enPassantSquare;
                if ((enPassantBitboard & Bitboards.WHITE_PAWN_ATTACKS[from]) != 0L) {
                    pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, true));
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
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BN, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BB, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BR, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BQ, false, false));
                    } else {
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
                    }

                    // Double Push
                    if (fromRank == 6) {
                        int doublePushTo = from - 16;
                        if (!isOccupied(pos.board.occupied, doublePushTo)) {
                            pseudoLegalMoves.append(Move.createMove(pos, from, doublePushTo, Piece.NONE, false, false));
                        }
                    }
                }

                // Captures YAEY

                long capturedBitboard = pos.board.whitePieces & Bitboards.BLACK_PAWN_ATTACKS[from];
                while (capturedBitboard != 0L) {
                    int lsb = Long.numberOfTrailingZeros(capturedBitboard);
                    if (fromRank + 1 == 7) {
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BN, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BB, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BR, false, false));
                        pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.BQ, false, false));
                    }else 
                    pseudoLegalMoves.append(Move.createMove(pos, from, lsb, Piece.NONE, false, false));
                    capturedBitboard &= capturedBitboard - 1;
                }

                // Time for EN CROISSANT 🥐

                long enPassantBitboard = 1L << pos.gameState.enPassantSquare;
                if ((enPassantBitboard & Bitboards.BLACK_PAWN_ATTACKS[from]) != 0L) {
                    pseudoLegalMoves.append(Move.createMove(pos, from, pos.gameState.enPassantSquare, Piece.NONE, false, true));
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
                pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
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
                pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
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
                pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
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
                pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
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
                pseudoLegalMoves.append(Move.createMove(pos, from, to, Piece.NONE, false, false));
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
    

} 
