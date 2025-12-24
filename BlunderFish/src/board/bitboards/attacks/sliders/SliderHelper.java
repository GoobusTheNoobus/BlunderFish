package board.bitboards.attacks.sliders;

public class SliderHelper {
    public static long indexToBlockers(int index, long mask) {
        long blockers = 0L;
        int bitPos = 0;

        while (mask != 0) {
            long lsb = mask & -mask;   // lowest set bit in mask
            mask ^= lsb;

            if ((index & (1 << bitPos)) != 0) {
                blockers |= lsb;
            }

            bitPos++;
        }

        return blockers;
    }
}
