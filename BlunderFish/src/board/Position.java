package board;



import utils.Utility;
import utils.Constants;

public class Position {
    public long[] bitboards = new long[12];

    public int[] mailbox = new int[64];

    public long whitePieces;
    public long blackPieces;
    public long occupied;

    public boolean whiteToMove;

    public int castlingRights;
    public int enPassantSquare;

    public int fiftyMoveClock;

    public int moveNumber;

    public static final int W_PAWN = 1;
    public static final int W_KNIGHT = 2;
    public static final int W_BISHOP = 3;
    public static final int W_ROOK = 4;
    public static final int W_QUEEN = 5;
    public static final int W_KING = 6;

    public static final int B_PAWN = 1;

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


    public void loadFEN (String fen) {
        
        for (int i = 0; i < 12; i ++) {
            bitboards[i] = 0L;
        }

        String[] parts = fen.split(" ");

        // Handling Board

        String boardPart = parts[0];
        int pointer = 56;
        for (char car: boardPart.toCharArray()) {
            if (car == '/')
                pointer = pointer - 16;

            
            else if (Character.isDigit(car)) {
                pointer = pointer + (car - '0');
            }
            else {
                long mask = 1L << pointer;

                switch (car) {
                    case 'P':
                        bitboards[Constants.WP_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.WP;
                        break;
                    case 'p':
                        bitboards[Constants.BP_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.BP;
                        break;
                    case 'N':
                        bitboards[Constants.WN_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.WN;
                        break;
                    case 'n':
                        bitboards[Constants.BN_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.BN;
                        break;
                    case 'B':
                        bitboards[Constants.WB_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.WB;
                        break;
                    case 'b':
                        bitboards[Constants.BB_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.BB;
                        break;
                    case 'R':
                        bitboards[Constants.WR_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.WR;
                        break;
                    case 'r':
                        bitboards[Constants.BR_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.BR;
                        break;
                    case 'Q':
                        bitboards[Constants.WQ_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.WQ;
                        break;
                    case 'q':
                        bitboards[Constants.BQ_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.BQ;
                        break;
                    case 'K':
                        bitboards[Constants.WK_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.WK;
                        break;
                    case 'k':
                        bitboards[Constants.BK_BITBOARD] |= mask;
                        mailbox[pointer] = Piece.BK;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FEN character in board field: " + car);
                    
                }

                pointer ++;
            }
        }

        for (int i = 0; i < 6; i++) {
            whitePieces |= bitboards[i];
        }
        for (int i = 6; i < 12; i++) {
            blackPieces |= bitboards[i];
        }
        occupied = whitePieces | blackPieces;

        // Side to Move
        switch (parts[1]) {
            case "w":
                this.whiteToMove = true;
                break;
            case "b":
                this.whiteToMove = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid FEN character in side to move field");
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
            enPassantSquare = -1;
        } else {
           enPassantSquare = Utility.getSquareIntFromString(enPassantPart);
        }

        // 50 Move Clock
        fiftyMoveClock = Integer.parseInt(parts[4]);

        moveNumber = Integer.parseInt(parts[5]);
    }

    public int pieceAt(int square) {
        return mailbox[square];
    }

    @Deprecated
    public void printBoard () {
        int c = 56;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(mailbox[c]);

                c++;
            }
            System.out.println();
            c = c - 16;
        }
        System.out.println("White to move? : " + whiteToMove);
    }

    

    public void move (int fromSquare, int toSquare) {

    }

    public boolean isSquareAttacked (int square, boolean white) {
        long mask = 1L << square;
        long pawns = white ? bitboards[Constants.WP_BITBOARD] : bitboards[Constants.BP_BITBOARD];
        long knights = white ? bitboards[Constants.WN_BITBOARD] : bitboards[Constants.BN_BITBOARD];
        long bishops = white ? bitboards[Constants.WB_BITBOARD] : bitboards[Constants.BB_BITBOARD];
        long rooks = white ? bitboards[Constants.WR_BITBOARD] : bitboards[Constants.BR_BITBOARD];
        long queens = white ? bitboards[Constants.WQ_BITBOARD] : bitboards[Constants.BQ_BITBOARD];
        long king = white ? bitboards[Constants.WK_BITBOARD] : bitboards[Constants.BK_BITBOARD];

        // Pawn Attacks

        if (white){
            if (((mask >>> 7) & pawns & ~Bitboards.A_FILE) != 0) return true;
            if (((mask >>> 9) & pawns & ~Bitboards.H_FILE) != 0) return true;
        } else {
            if (((mask << 7) & pawns & ~Bitboards.H_FILE) != 0) return true;
            if (((mask << 9) & pawns & ~Bitboards.A_FILE) != 0) return true;
        }

        // Knights Attacks
        if ((Bitboards.KNIGHT_ATTACKS[square] & knights) != 0) return true;

        // King Attacks
        if ((Bitboards.KING_ATTACKS[square] & king) != 0) return true;

        // Sliding Pieces: Magic (not actually magic just a big database) Tables

    

        long rookAttacks = Bitboards.getRookAttack(occupied & Bitboards.ROOK_MASKS[square], square);
        long bishopAttacks = Bitboards.getBishopAttack(occupied & Bitboards.BISHOP_MASKS[square], square);

        if (((rookAttacks & rooks) != 0L) || ((rookAttacks & queens)  != 0L)) {
            return true;
        }

        if (((bishopAttacks & bishops) != 0L) || ((bishopAttacks & queens)  != 0L)) {
            return true;
        }
        
        
        return false;
    }

    public boolean isInCheck() {
        int kingIndex = (whiteToMove) ? 63 - Long.numberOfLeadingZeros(bitboards[Constants.WK_BITBOARD]) : 63 - Long.numberOfLeadingZeros(bitboards[Constants.BK_BITBOARD]);
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


    public void printPosition() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                System.out.print(mailbox[i << 3 | j]);
            }
            System.out.println();
        }
    }
}
