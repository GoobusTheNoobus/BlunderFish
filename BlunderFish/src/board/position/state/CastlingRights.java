/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.state;

public class CastlingRights {
    public static final int WHITE_KINGSIDE_CASTLING_MASK = 0b0001;
    public static final int WHITE_QUEENSIDE_CASTLING_MASK = 0b0010;
    public static final int BLACK_KINGSIDE_CASTLING_MASK = 0b0100;
    public static final int BLACK_QUEENSIDE_CASTLING_MASK = 0b1000;

    public static boolean hasRight (int rights, int right) {
        return (rights & right) != 0;
    }

    public static int removeRight (int rights, int rightMask) {
        return rights & ~rights;
    }

    public static int addRight (int rights, int rightMask) {
        return rights | rightMask;
    }
}
