package board.bitboards.masks;

public class RankMasks {
    public static final long RANK_1 = 0x00000000000000FFL;
    public static final long RANK_2 = RANK_1 << 8;
    public static final long RANK_3 = RANK_2 << 8;
    public static final long RANK_4 = RANK_3 << 8;
    public static final long RANK_5 = RANK_4 << 8;
    public static final long RANK_6 = RANK_5 << 8;
    public static final long RANK_7 = RANK_6 << 8;
    public static final long RANK_8 = RANK_7 << 8;
}
