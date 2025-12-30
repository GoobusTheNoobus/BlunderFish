/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.helper;

import board.position.Piece;
import board.position.state.Board;;

public class BoardParser {
    public static void parseBoard(Board board, String boardStr) {
        int pointer = 56;
        for (char car: boardStr.toCharArray()) {
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
                        board.bitboards[Piece.WP] |= mask;
                        board.mailbox[pointer] = Piece.WP;
                        break;
                    case 'p':
                        board.bitboards[Piece.BP] |= mask;
                        board.mailbox[pointer] = Piece.BP;
                        break;
                    case 'N':
                        board.bitboards[Piece.WN] |= mask;
                        board.mailbox[pointer] = Piece.WN;
                        break;
                    case 'n':
                        board.bitboards[Piece.BN] |= mask;
                        board.mailbox[pointer] = Piece.BN;
                        break;
                    case 'B':
                        board.bitboards[Piece.WB] |= mask;
                        board.mailbox[pointer] = Piece.WB;
                        break;
                    case 'b':
                        board.bitboards[Piece.BB] |= mask;
                        board.mailbox[pointer] = Piece.BB;
                        break;
                    case 'R':
                        board.bitboards[Piece.WR] |= mask;
                        board.mailbox[pointer] = Piece.WR;
                        break;
                    case 'r':
                        board.bitboards[Piece.BR] |= mask;
                        board.mailbox[pointer] = Piece.BR;
                        break;
                    case 'Q':
                        board.bitboards[Piece.WQ] |= mask;
                        board.mailbox[pointer] = Piece.WQ;
                        break;
                    case 'q':
                        board.bitboards[Piece.BQ] |= mask;
                        board.mailbox[pointer] = Piece.BQ;
                        break;
                    case 'K':
                        board.bitboards[Piece.WK] |= mask;
                        board.mailbox[pointer] = Piece.WK;
                        break;
                    case 'k':
                        board.bitboards[Piece.BK] |= mask;
                        board.mailbox[pointer] = Piece.BK;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FEN character in board field: " + car);
                    
                }

                pointer ++;
            }
            board.updateOccupancy();
        }
    }
    
}
