/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board;

import java.util.ArrayDeque;
import java.util.Arrays;

import board.bitboards.Bitboards;
import board.bitboards.masks.*;
import gen.Move;
import utils.Utility;
import utils.Constants;
import utils.Timer;

/**
 * Represents a chess position with board state, move number, and a move history "stack"
 * Uses both bitboard and mailbox representations for efficiency and speed
 * The bitboard index is the same as the piece constant (White Pawn's bitboard is bitboards[Piece.WP])
 */


public class Position {
    // Move history for undoing moves
    public ArrayDeque<Long> moveHistory = new ArrayDeque<>();

    // Bitboards for each type of pieces, bitboards[0] is empty
    public long[] bitboards = new long[13];

    // Mailbox array for piece lookup
    public int[] mailbox = new int[64];

    // Other bitboards for efficiency and checking for friendly and enemy captures
    public long whitePieces;
    public long blackPieces;
    public long occupied;

    // Side to move
    public boolean whiteToMove;

     // Bit 0: KingsideW | Bit 1: QueensideW | Bit 2: KingsideB | Bit 3: QueensideB
    public int castlingRights;

    // En passant target square 0-63, 64 = No Square
    public int enPassantSquare;

    // Fifty Move Clock
    public int fiftyMoveClock;

    /**
     * Loads a position from a FEN string
     * Note that invalid positions will still be accepted
     * @param fen is the FEN string or null for starting position
     */
    public Position(String fen) {
        if (fen.isEmpty() || fen == null) {
            loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } else {
            loadFEN(fen);
        }
    }

    public Position() {
        loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    // Load FEN function
    public void loadFEN (String fen) {
        // Just in case I change Piece.NONE constant to something other than 0
        Arrays.fill(mailbox, Piece.NONE);
        
        // Make sure all bitboards are empty for safety measure
        for (int i = 0; i < 13; i ++) {
            bitboards[i] = 0L;
        }

        String[] parts = fen.split(" ");

        // Handling Board
        String boardPart = parts[0];
        int pointer = 56;
        for (char car: boardPart.toCharArray()) {
            // Next rank
            if (car == '/')
                pointer = pointer - 16;

            // If its a number, create that many empty spaces
            else if (Character.isDigit(car)) {
                pointer = pointer + (car - '0');
            }

            // Load a piece
            else {
                long mask = 1L << pointer;

                // Do NOT question the naming, it is for the greater good 
                switch (car) {
                    case 'P':
                        bitboards[Piece.WP] |= mask;
                        mailbox[pointer] = Piece.WP;
                        break;
                    case 'p':
                        bitboards[Piece.BP] |= mask;
                        mailbox[pointer] = Piece.BP;
                        break;
                    case 'N':
                        bitboards[Piece.WN] |= mask;
                        mailbox[pointer] = Piece.WN;
                        break;
                    case 'n':
                        bitboards[Piece.BN] |= mask;
                        mailbox[pointer] = Piece.BN;
                        break;
                    case 'B':
                        bitboards[Piece.WB] |= mask;
                        mailbox[pointer] = Piece.WB;
                        break;
                    case 'b':
                        bitboards[Piece.BB] |= mask;
                        mailbox[pointer] = Piece.BB;
                        break;
                    case 'R':
                        bitboards[Piece.WR] |= mask;
                        mailbox[pointer] = Piece.WR;
                        break;
                    case 'r':
                        bitboards[Piece.BR] |= mask;
                        mailbox[pointer] = Piece.BR;
                        break;
                    case 'Q':
                        bitboards[Piece.WQ] |= mask;
                        mailbox[pointer] = Piece.WQ;
                        break;
                    case 'q':
                        bitboards[Piece.BQ] |= mask;
                        mailbox[pointer] = Piece.BQ;
                        break;
                    case 'K':
                        bitboards[Piece.WK] |= mask;
                        mailbox[pointer] = Piece.WK;
                        break;
                    case 'k':
                        bitboards[Piece.BK] |= mask;
                        mailbox[pointer] = Piece.BK;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FEN character in board field: " + car);
                    
                }

                pointer ++;
            }
        }

        updateOccupancy();

        // Side to Move
        switch (parts[1]) {
            case "w":
                this.whiteToMove = true;
                break;
            case "b":
                this.whiteToMove = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid FEN character in side to move field: " + parts[1]);
        }

        // Castling

        if (!(parts[2].equals("-"))) {
            for (char i: parts[2].toCharArray()) {
                switch (i) {
                    case 'K':
                        castlingRights |= 1;
                        break;
                    case 'Q':
                        castlingRights |= 2;
                        break;
                    case 'k':
                        castlingRights |= 4;
                        break;
                    case 'q':
                        castlingRights |= 8;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FEN character in castling rights field" + i);
                }
            }
        }

        // En Passant

        String enPassantPart = parts[3];

        
        if (enPassantPart.equals("-")) {
            enPassantSquare = 64;
        } else {
           enPassantSquare = Utility.getSquareIntFromString(enPassantPart);
        }

        // 50 Move Clock
        fiftyMoveClock = Integer.parseInt(parts[4]);

        
    }

    public int pieceAt(int square) {
        return mailbox[square];
    }

    public boolean isSquareAttacked (int square, boolean white) {
        long mask = 1L << square;
        long pawns = white ? bitboards[Piece.WP] : bitboards[Piece.BP];
        long knights = white ? bitboards[Piece.WN] : bitboards[Piece.BN];
        long bishops = white ? bitboards[Piece.WB] : bitboards[Piece.BB];
        long rooks = white ? bitboards[Piece.WR] : bitboards[Piece.BR];
        long queens = white ? bitboards[Piece.WQ] : bitboards[Piece.BQ];
        long king = white ? bitboards[Piece.WK] : bitboards[Piece.BK];

        // Pawn Attacks

        if (white){
            if (((mask >>> 7) & pawns & ~FileMasks.A_FILE) != 0) return true;
            if (((mask >>> 9) & pawns & ~FileMasks.H_FILE) != 0) return true;
        } else {
            if (((mask << 7) & pawns & ~FileMasks.H_FILE) != 0) return true;
            if (((mask << 9) & pawns & ~FileMasks.A_FILE) != 0) return true;
        }

        // Knights Attacks
        if ((Bitboards.KNIGHT_ATTACKS[square] & knights) != 0) return true;

        // King Attacks
        if ((Bitboards.KING_ATTACKS[square] & king) != 0) return true;

        // Sliding Pieces: Magic (not actually magic just a big database) Tables
        long rookAttacks = Bitboards.getRookAttack(occupied, square);
        long bishopAttacks = Bitboards.getBishopAttack(occupied, square);

        if (((rookAttacks & rooks) != 0L) || ((rookAttacks & queens)  != 0L)) {
            return true;
        }

        if (((bishopAttacks & bishops) != 0L) || ((bishopAttacks & queens)  != 0L)) {
            return true;
        }
        
        
        return false;
    }

    public boolean isInCheck() {
        int kingIndex = (whiteToMove) ? Long.numberOfTrailingZeros(bitboards[Piece.WK]) : Long.numberOfTrailingZeros(bitboards[Piece.BK]);
        return isSquareAttacked(kingIndex, !whiteToMove);
    }

    public boolean canCastle (boolean kingSide) {
        if (isInCheck()) {
            return false;
        }

        if (kingSide) {
            if (whiteToMove) {
                if ((castlingRights & 1) == 0) 
                    return false;

                if (isSquareAttacked(5, false)) 
                    return false;
                
                if (isSquareAttacked(6, false)) 
                    return false;
            } else {
                if ((castlingRights & 4) == 0) 
                    return false;

                if (isSquareAttacked(61, true))
                    return false;
                
                if (isSquareAttacked(62, true))
                    return false;
            }
        } else {
            if (whiteToMove) {
                if ((castlingRights & 2) == 0)
                    return false;
                if ((isSquareAttacked(2, false)))
                    return false;
                if ((isSquareAttacked(3, false)))
                    return false;
            } else {
                if ((castlingRights & 8) == 0) 
                    return false;
                if ((isSquareAttacked(58, true)))
                    return false;
                if ((isSquareAttacked(59, true)))
                    return false;
            }
        }
        return true;
    }

    public void makeMove (long move) {

        // Push it into the stack (technically a dequeArray :D)
        moveHistory.push(move);

        boolean isCastling = Move.getCastlingFlag(move);
        boolean isEnPassant = Move.getEnPassantFlag(move);
        int from = Move.getFromSquare(move);
        int to = Move.getToSquare(move);
        int pieceMoved = Move.getMovedPiece(move);
        int promotionPiece =  Move.getPromotionPiece(move);

        // Make sure the information is accurate
        assert pieceAt(from) == pieceMoved;

        // This is assuming that castling is even legal. If it isn't it can create duplicate kings and stuff
        if (isCastling) {

            // Clear King first
            setSquareToPiece(Piece.NONE, from);

            // Handle different cases

            // White Queenside Castling
            if (to == 2 && whiteToMove) { 
                setSquareToPiece(Piece.NONE, 0); // Clear corner rook
                setSquareToPiece(Piece.WK, to); 
                setSquareToPiece(Piece.WR, 3);
                castlingRights &= 0b1100;

            } 
            
            // White Kingside Castling
            else if (to == 6 && whiteToMove) { 
                setSquareToPiece(Piece.NONE, 7); // Clear corner rook
                setSquareToPiece(Piece.WK, to);
                setSquareToPiece(Piece.WR, 5);
                castlingRights &= 0b1100;
            } 
            
            // Black Queenside Castling
            else if (to == 58 && !whiteToMove) { 
                setSquareToPiece(Piece.NONE, 56); // Clear corner rook
                setSquareToPiece(Piece.BK, to);
                setSquareToPiece(Piece.BR, 59);
                castlingRights &= 0b11;
            }

            // Black Kingside Castling
            else if (to == 62 && !whiteToMove) {
                setSquareToPiece(Piece.NONE, 63); // Clear corner rook
                setSquareToPiece(Piece.BK, to);
                setSquareToPiece(Piece.BR, 61);
                castlingRights &= 0b11;
            } else {
                // Don't question this either, thats just me at 3AM losing my sanity
                throw new IllegalArgumentException("Woah slow down there buddy ur move is a castling move duh 🙄 but its incorrect but since im so nice here is the move so you can analyze it cuz i am so nice wow i am so nice haha anyways where was i oh yeah the castling move number is, wait what was it, oh yeah, it was 67 HAHAHAHAH sorry that was so unfunny my bad bro ill give you sum robux later but the number is " + move);
            }
            whiteToMove = !whiteToMove;
            enPassantSquare = 64; // Clear En Croissant sqaure

            updateOccupancy();
            return;
        }

        if (isEnPassant) {
            // Check if the move contridicts itself
            if (Move.getPreviousEnPassantSquare(move) == 64) 
                throw new IllegalArgumentException("Illegal Move: the en passant square is none, though the move is en passant. Croissant!!!");


            setSquareToPiece(Piece.NONE, from);
            setSquareToPiece(pieceMoved, to);

            if (whiteToMove)
                // Clear square one rank behind the target square
                setSquareToPiece(Piece.NONE, enPassantSquare - 8);
            else 
                // Same thing but ahead
                setSquareToPiece(Piece.NONE, enPassantSquare + 8);

            
            whiteToMove = !whiteToMove;
            enPassantSquare = 64;

            updateOccupancy();
            return;
        }

        // Clear from bit
        setSquareToPiece(Piece.NONE, from);

        // Check if promotion
        if (Move.getPromotionFlag(move)) {
            setSquareToPiece(promotionPiece, to);
            enPassantSquare = 64;
        }

        // Check if double push (if it is, set en passant square)
        else if (Move.getDoublePushFlag(move)) {
            if (whiteToMove) 
                enPassantSquare = to - 8;
            else
                enPassantSquare = to + 8;
            setSquareToPiece(pieceMoved, to);
        }
        
        // Normal Move
        else {
            setSquareToPiece(pieceMoved, to);
            enPassantSquare = 64;
        }

        updateOccupancy();
        whiteToMove = !whiteToMove;
    }

    public void undoMove () {
        // Taking a sneak peak 
        long move = moveHistory.pop();
        int to = Move.getToSquare(move);
        int from = Move.getFromSquare(move);

        if (Move.getCastlingFlag(move)) {
            // Delete King
            setSquareToPiece(Piece.NONE, to);

            // Check different cases

            // White Queenside
            if (to == 2) {
                setSquareToPiece(Piece.NONE, 3);
                setSquareToPiece(Piece.WR, 0);
                setSquareToPiece(Piece.WK, from);

            }
        }
        

    }


    // helpers
    public void printPosition() {
        for (int rank = 7; rank >= 0; rank--) {
            System.out.print((rank + 1) + " ");
            for (int file = 0; file < 8; file++) {
                switch (mailbox[rank << 3 | file]) {
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
                        throw new IllegalStateException("Oh noeserz!!! it seems like this position has a bit of weird stuff in it, and therefore, putting me in a bit of a pickle. I love pickles by the way, its really good. if you disagree, i will eat you instead. anyways, basically, in this position's mailbox, there is a weird piece. it looks, like, uhhh, like an elephant. wait, there is an elephant in chess right? oh right wrong variants. elephants are w tho. in chess pretty sure its a bishop. idk, btw since im so nice, here is the piece: " + mailbox[rank << 3 | file]);
                    
                }
                System.out.print(" ");
            }
            
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
        System.out.println("Side to move: " + (whiteToMove ? "White" : "Black"));
        System.out.println("En Croissant Square: " + enPassantSquare);
    }

    public void setSquareToPiece(int piece, int square) {

        int oldPiece = mailbox[square];
        long squareMask = 1L << square;

        // 1. Remove old piece (if any)
        if (oldPiece != Piece.NONE) {
            bitboards[oldPiece - 1] &= ~squareMask;

            if (oldPiece <= Piece.WK) {
                whitePieces &= ~squareMask;
            } else {
                blackPieces &= ~squareMask;
            }

            occupied &= ~squareMask;
        }

        // 2. Place new piece (if any)
        mailbox[square] = piece;

        if (piece != Piece.NONE) {
            bitboards[piece - 1] |= squareMask;

            if (piece <= Piece.WK) {
                whitePieces |= squareMask;
            } else {
                blackPieces |= squareMask;
            }

            occupied |= squareMask;
        }
    }


    public void updateOccupancy () {
        whitePieces = bitboards[0] | bitboards[1] | bitboards[2] | bitboards[3] | bitboards[4] | bitboards[5];
        blackPieces = bitboards[6] | bitboards[7] | bitboards[8] | bitboards[9] | bitboards[10] | bitboards[11];

        occupied = whitePieces | blackPieces;
    }

    // This is just for debugging purposes. This class is not meant to run on its own
    public static void main(String[] args) {
        Position pos = new Position("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        pos.printPosition();

        Move.createEnPassantMove(pos, 25, 16);
        Timer.start();
        
        Timer.stop();
        Timer.printTime();
        pos.printPosition();
        
    }
    
}
