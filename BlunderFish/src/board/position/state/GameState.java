/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.state;

public class GameState {
    public boolean whiteToMove;

    // Bit 0: KingsideW | Bit 1: QueensideW | Bit 2: KingsideB | Bit 3: QueensideB
    public int castlingRights;

    // 0-63, 64 = No Square
    public int enPassantSquare;

    // fifty move rule
    public int fiftyMoveClock;


    public GameState() {
        whiteToMove = true;
        enPassantSquare = 64;

    }
}
