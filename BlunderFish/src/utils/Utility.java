/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package utils;

public class Utility {
    public static int getSquareIntFromString (String squareString) {
        char fileC = squareString.charAt(0);
        char rankC = squareString.charAt(1);

        int file = fileC - 'a';
        int rank = rankC - '1';

        return rank * 8 + file;
    }

    public static String getStringFromSquareInt(int square) {
        int file = square & 7;
        int rank = square >> 3;

        char f = (char)(file + 'a');
        char r = (char)(rank + '1');

        return "" + f + r;
    }
    
    public static int parseRank (int square) {
        return square >> 3;
    }

    public static int parseFile (int square) {
        return square & 7;
    }

    public static int parseSquare (int rank, int file) {
        return (rank << 3) | file;
    }
    
    
}
