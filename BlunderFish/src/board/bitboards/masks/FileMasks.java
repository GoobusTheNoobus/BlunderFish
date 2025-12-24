package board.bitboards.masks;

public class FileMasks {
    public static final long A_FILE = 0x0101010101010101L;
    public static final long B_FILE = A_FILE << 1;
    public static final long C_FILE = A_FILE << 2;
    public static final long D_FILE = A_FILE << 3;
    public static final long E_FILE = A_FILE << 4;
    public static final long F_FILE = A_FILE << 5;
    public static final long G_FILE = A_FILE << 6;
    public static final long H_FILE = A_FILE << 7;
}
