/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.bitboards.attacks.sliders;

public class Hash {
    public static int hash(long blockers, int square, boolean isRook) {
        blockers &= isRook ? AttackMasks.ROOK_MASKS[square] : AttackMasks.BISHOP_MASKS[square];
        blockers *= isRook ? Magic.ROOK_MAGIC_NUMBERS[square] : Magic.BISHOP_MAGIC_NUMBERS[square];
        blockers >>>= isRook ? 64 - RookAttacks.RELEVANT_ROOK_BITS[square] : 64 - BishopAttacks.RELEVANT_BISHOP_BITS[square];
        return ((int)blockers);
    }
}
