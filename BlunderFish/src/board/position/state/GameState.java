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

    public boolean hasRight (int right) {
        return (castlingRights & right) != 0;
    }

    public void removeRight (int rightMask) {
        castlingRights &= ~rightMask;
    }

    public void addRight (int rightMask) {
        castlingRights |= rightMask;
    }

    public void switchSide () {
        whiteToMove = !whiteToMove;
    }
}
