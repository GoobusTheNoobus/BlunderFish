package board;

public class Bitboards {
    public static final long A_FILE = 0x0101010101010101L;
    public static final long B_FILE = A_FILE << 1;
    public static final long C_FILE = A_FILE << 2;
    public static final long D_FILE = A_FILE << 3;
    public static final long E_FILE = A_FILE << 4;
    public static final long F_FILE = A_FILE << 5;
    public static final long G_FILE = A_FILE << 6;
    public static final long H_FILE = A_FILE << 7;

    public static final long RANK_1 = 0x00000000000000FFL;
    public static final long RANK_2 = RANK_1 << 8;
    public static final long RANK_3 = RANK_2 << 8;
    public static final long RANK_4 = RANK_3 << 8;
    public static final long RANK_5 = RANK_4 << 8;
    public static final long RANK_6 = RANK_5 << 8;
    public static final long RANK_7 = RANK_6 << 8;
    public static final long RANK_8 = RANK_7 << 8;

    public static final long[] KNIGHT_ATTACKS = new long[64];
    public static final long[] KING_ATTACKS = new long[64];
    public static final long[] WHITE_PAWN_ATTACKS = new long[64];
    public static final long[] BLACK_PAWN_ATTACKS = new long[64];

    public static final long[] ROOK_MASKS = new long[64];
    public static final long[] BISHOP_MASKS = new long[64];

    public static final long[][] ROOK_ATTACKS = new long[64][];
    public static final long[][] BISHOP_ATTACKS = new long[64][];

    public static final long[] ROOK_MAGIC_NUMBERS = {
        0x2080031423804000L,
        0x4040062000401002L,
        0x2A8009E000300080L,
        0x8100043001002148L,
        0x8200102200082004L,
        0x8A00070408420090L,
        0xA400228110180402L,
        0x8A00082040830C02L,
        0x4080800080400A6CL,
        0x321040004C201003L,
        0x2120801009806000L,
        0x92C2002200584130L,
        0xC350802802801C00L,
        0x1100800601440080L,
        0x4A01001300220004L,
        0xC0C2000282086401L,
        0x0100818006400428L,
        0x03B4808040002004L,
        0x21018080100AA001L,
        0x84010A0020413200L,
        0x80E0310025002800L,
        0x026101000400B806L,
        0x6C00840012100318L,
        0x0C4002000910864CL,
        0x00400245800180E5L,
        0x11AAD0044001A000L,
        0x00003101002002C2L,
        0x491B00210010011AL,
        0xB412010600085060L,
        0xC40A000E00900408L,
        0x0408101400121843L,
        0xF180110200185884L,
        0x2840004280801064L,
        0x2A61610483004001L,
        0x000A0010C2002181L,
        0x00B04A3001002100L,
        0x0042D81E0E001200L,
        0x1002005002000408L,
        0x4030211004004258L,
        0x50080401EA000081L,
        0x10C0C00080008021L,
        0x0A38810202220041L,
        0x800E015020820040L,
        0x8100A21842020010L,
        0x0822011004620008L,
        0x00060024B0120018L,
        0x4218120810240027L,
        0xA881088112C2000CL,
        0x800A008443350200L,
        0x800423884D020200L,
        0x224022811A004200L,
        0x40004201A2281200L,
        0x042800A400800980L,
        0x4B62001890844200L,
        0x11501008218A3C00L,
        0x3010231280440200L,
        0x44484A0011012182L,
        0x04410C4000128021L,
        0x18005142002080AAL,
        0x442A900121001805L,
        0x0A8600288C102112L,
        0x02B6000130881402L,
        0x2A05018310060824L,
        0x408044004281EF06L,
    };

    public static final long[] BISHOP_MAGIC_NUMBERS = {
        0x0022101050818080L,
        0x00A0084508608128L,
        0x40D0810A00204000L,
        0x0024440080000040L,
        0x4042021000000000L,
        0x100A020222040000L,
        0x0809450828400200L,
        0x0410104410084800L,
        0x0000082128020040L,
        0x640820280A004A41L,
        0xA09410448C810000L,
        0x8008542400900000L,
        0x00040450C0181000L,
        0x8000020844042189L,
        0x0808048090101201L,
        0x0001304042282040L,
        0x0250310410320820L,
        0x0202105144180080L,
        0x0000404880408020L,
        0x000C040801202400L,
        0x3111001820082000L,
        0x100100080C02020AL,
        0x1500400101282010L,
        0x1202102A49040500L,
        0x090D40004C100C00L,
        0x4010840208080080L,
        0x0104020001180104L,
        0x0C00080884004808L,
        0x0281080401004004L,
        0x0410018083024100L,
        0x008404000086191AL,
        0x1000408000440C40L,
        0x0488210405100400L,
        0x0488210405100400L,
        0xE852211004010C00L,
        0x5000040401080120L,
        0x010004A080104084L,
        0x6090064601284100L,
        0x4004040C22008080L,
        0x02280A0030088882L,
        0x0001101004251103L,
        0x110C2104020A1004L,
        0x000020940A101000L,
        0x7000222018003100L,
        0x1048400093000204L,
        0x40001A0380200100L,
        0x0044010811020A00L,
        0x1902208112089D00L,
        0x0809450828400200L,
        0x610047008820084CL,
        0x0010131401040012L,
        0x0005000020880C00L,
        0x0100105202020900L,
        0x2012400508022011L,
        0x002204015801000AL,
        0x00A0084508608128L,
        0x0410104410084800L,
        0x0001304042282040L,
        0x0488001022011000L,
        0x120C408C0A150401L,
        0x0288000004504400L,
        0x0284200404080208L,
        0x0000082128020040L,
        0x0022101050818080L
    };

    public static final int[] RELEVANT_ROOK_BITS = {
        12, 11, 11, 11, 11, 11, 11, 12,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        12, 11, 11, 11, 11, 11, 11, 12
    };

    public static final int[] RELEVANT_BISHOP_BITS = {
        6, 5, 5, 5, 5, 5, 5, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5,
        6, 5, 5, 5, 5, 5, 5, 6
    };
    
    public static void intializePawnAttackTables() {
        // Precompute Pawn Attacks (Not moves, just captures)
        int[][] whitePawnAttacks = {{1, 1}, {1, -1}};
        int[][] blackPawnAttacks = {{-1, 1}, {-1, -1}};

        // iterate squares
        for (int i = 0; i < 64; i ++) {

            long whitePawnMask = 0L;
            long blackPawnMask = 0L;

            for (int[] whitePawnAttack : whitePawnAttacks) {
                int x = (i >> 3) + whitePawnAttack[0];
                int y = (i & 7) + whitePawnAttack[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                whitePawnMask |= 1L << square;

            }
            for (int[] blackPawnAttack: blackPawnAttacks) {
                int x = (i >> 3) + blackPawnAttack[0];
                int y = (i & 7) + blackPawnAttack[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                blackPawnMask |= 1L << square;
            } 

            // Append to the attacks
            WHITE_PAWN_ATTACKS[i] = whitePawnMask;
            BLACK_PAWN_ATTACKS[i] = blackPawnMask;
        }
        
        
    }

    public static void initalizeKnightTable() {
        int[][] knightDirections = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}}; // Knight Attacks

        for (int i = 0; i < 64; i ++) {
            // Init Mask
            long mask = 0L;

            // Iterate through each direction
            for (int[] knightDir : knightDirections) {
                int x = (i >> 3) + knightDir[0];
                int y = (i & 7) + knightDir[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                mask |= 1L << square;

            }
            // Append to array
            KNIGHT_ATTACKS[i] = mask;
        }
    }

    public static void initalizeKingTable() {
        int[][] kingDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int i = 0; i < 64; i ++) {
            long mask = 0L;

            // Iterate direction
            for (int[] kingDir : kingDirections) {
                int x = (i >> 3) + kingDir[0];
                int y = (i & 7) + kingDir[1];

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                }

                int square = x << 3 | y;

                mask |= 1L << square;

            }
            // Append to array
            KING_ATTACKS[i] = mask;
        }
    }

    public static long generateRookAttacksPerSquare (int square, long blockers) {
        // Generate Rook Attacks for square and given blockers
        long mask = 0L;

        // Parse Rank and File 
        int rank = square / 8;
        int file = square % 8;

        // Iterate in each direction
        for (int newRank = rank + 1; newRank <= 7; newRank++){ // Upwards
            long newSquareMask = 1L << (newRank * 8 + file);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        } 

        for (int newRank = rank - 1; newRank >= 0; newRank--){ // Downwards
            long newSquareMask = 1L << (newRank * 8 + file);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newFile = file + 1; newFile <= 7; newFile++) { // Right
            long newSquareMask = 1L << (rank * 8 + newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        } 
        for (int newFile = file - 1; newFile >= 0; newFile--){ // Left
            long newSquareMask = 1L << (rank * 8 + newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask;
        } 

        return mask;
    }

    public static long generateBishopAttacksPerSquare (int square, long blockers) {
        // Generate Bishop Attacks for square and given blockers
        long mask = 0L;

        // Parse Rank and File 
        int rank = square / 8;
        int file = square % 8;

        // Iterate
        for (int newRank = rank + 1, newFile = file + 1; newRank <= 7 && newFile <= 7; newRank++, newFile++){
            long newSquareMask = 1L << (newRank * 8 + newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked

                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newRank = rank + 1, newFile = file - 1; newRank <= 7 && newFile >= 0; newRank++, newFile--){
            long newSquareMask = 1L << (newRank * 8 + newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newRank = rank - 1, newFile = file + 1; newRank >= 0 && newFile <= 7; newRank--, newFile++){
            long newSquareMask = 1L << (newRank * 8 + newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }

        for (int newRank = rank - 1, newFile = file - 1; newRank >= 0 && newFile >= 0; newFile--, newRank--){
            long newSquareMask = 1L << (newRank * 8 + newFile);
            if ((newSquareMask & blockers) != 0L) { // Square is blocked
                mask |= newSquareMask; 
                break;
            }
            mask |= newSquareMask; 
        }
        return mask;
    }
    
    

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


    public static void initalizeSliderAttacks () {
        // Iterate through each square

        for (int square = 0; square < 64; square++) {
            /* ATTACK MASKS */
            // Generate Rook Mask for square
            long mask = 0L;

            // Parse Rank and File 
            int rank = square / 8;
            int file = square % 8;

            // Iterate in each direction
            for (int newRank = rank + 1; newRank <= 6; newRank++) mask |= 1L << (newRank * 8 + file); // Upwards
            for (int newRank = rank - 1; newRank >= 1; newRank--) mask |= 1L << (newRank * 8 + file); // Downwards
            for (int newFile = file + 1; newFile <= 6; newFile++) mask |= 1L << (rank * 8 + newFile); // Right
            for (int newFile = file - 1; newFile >= 1; newFile--) mask |= 1L << (rank * 8 + newFile); // Left

            // Put it into ROOK_MASK
            ROOK_MASKS[square] = mask;


            // Generate bishop mask (should be same logic as rook)

            mask = 0L;


            // Iterate
            for (int newRank = rank + 1, newFile = file + 1; newRank <= 6 && newFile <= 6; newRank++, newFile++)    mask |= 1L << (newRank * 8 + newFile); // NE

            for (int newRank = rank + 1, newFile = file - 1; newRank <= 6 && newFile >= 1; newRank++, newFile--) mask |= 1L << (newRank * 8 + newFile); // NW

            for (int newRank = rank - 1, newFile = file + 1; newRank >= 1 && newFile <= 6; newRank--, newFile++) mask |= 1L << (newRank * 8 + newFile); // SE

            for (int newRank = rank - 1, newFile = file - 1; newRank >= 1 && newFile >= 1; newRank--, newFile--) mask |= 1L << (newRank * 8 + newFile); // SW

            // Put it into BISHOP_MASKS
            BISHOP_MASKS[square] = mask;




            // Initialize the arrays for the square
            // The size is always 2 ^ relevant_bits

            BISHOP_ATTACKS[square] = new long[1 << RELEVANT_BISHOP_BITS[square]];
            ROOK_ATTACKS[square] = new long[1 << RELEVANT_ROOK_BITS[square]];

            // Do bishop stuff
            for (int index = 0; index < (1 << RELEVANT_BISHOP_BITS[square]); index++) {
                long blockers = indexToBlockers(index, BISHOP_MASKS[square]);


                BISHOP_ATTACKS[square][hash(blockers, square, false)] = generateBishopAttacksPerSquare(square, blockers);
            }

            // Do rook stuff

            for (int index = 0; index < (1 << RELEVANT_ROOK_BITS[square]); index++) {
                long blockers = indexToBlockers(index, ROOK_MASKS[square]);

                ROOK_ATTACKS[square][hash(blockers, square, true)] = generateRookAttacksPerSquare(square, blockers);
            }
        }
    }

    public static long getBishopAttack(long occupied, int square) { 
        // Time for some MAGIC
        return BISHOP_ATTACKS[square][hash(occupied & BISHOP_MASKS[square], square, false)];
    }

    public static long getRookAttack(long occupied, int square) {
        return ROOK_ATTACKS[square][hash(occupied & ROOK_MASKS[square], square, true)];
    }

    public static int hash(long blockers, int square, boolean isRook) {
        blockers &= isRook ? ROOK_MASKS[square] : BISHOP_MASKS[square];
        blockers *= isRook ? ROOK_MAGIC_NUMBERS[square] : BISHOP_MAGIC_NUMBERS[square];
        blockers >>>= isRook ? 64 - RELEVANT_ROOK_BITS[square] : 64 - RELEVANT_BISHOP_BITS[square];
        return ((int)blockers);
    }

    public static void init() {
        initalizeKingTable();
        initalizeKnightTable();
        intializePawnAttackTables();
        initalizeSliderAttacks();
    }

    /* ##### HELPER FUNCTIONS ##### */

    public static void printBitboard(long bb) {
        for (int rank = 7; rank >= 0; rank--) {

            System.out.print((rank + 1) + " ");

            for (int file = 0; file < 8; file++) {
                int sq = rank * 8 + file;
                System.out.print(((bb >>> sq) & 1L) != 0 ? "■ " : ". ");
            }

            System.out.println();
        }

        System.out.println("  a b c d e f g h\n");
    }
    
    public static void detectBishopMagicCollisions(int square) {
        long mask = BISHOP_MASKS[square];
        int bits = RELEVANT_BISHOP_BITS[square];
        int size = 1 << bits;

        long[] usedAttacks = new long[size];
        long[] usedBlockers = new long[size];
        boolean[] used = new boolean[size];

        System.out.println("Testing bishop magic collisions on square " + square);

        for (int index = 0; index < size; index++) {
            long blockers = indexToBlockers(index, mask);

            int hash = hash(blockers, square, false);
            long attacks = generateBishopAttacksPerSquare(square, blockers);

            if (!used[hash]) {
                used[hash] = true;
                usedAttacks[hash] = attacks;
                usedBlockers[hash] = blockers;
            } else {
                if (usedAttacks[hash] != attacks) {
                    System.out.println("MAGIC COLLISION FOUND");
                    System.out.println("Square: " + square);
                    System.out.println("Hash index: " + hash);

                    System.out.println("Blockers A:");
                    printBitboard(usedBlockers[hash]);
                    System.out.println("Attacks A:");
                    printBitboard(usedAttacks[hash]);

                    System.out.println("Blockers B:");
                    printBitboard(blockers);
                    System.out.println("Attacks B:");
                    printBitboard(attacks);
                    return;
                }
            }
        }

        System.out.println("No bishop magic collisions on square " + square);
    }
    public static void detectRookMagicCollisions(int square) {
        long mask = ROOK_MASKS[square];
        int bits = RELEVANT_ROOK_BITS[square];
        int size = 1 << bits;

        long[] usedAttacks = new long[size];
        long[] usedBlockers = new long[size];
        boolean[] used = new boolean[size];

        System.out.println("Testing rook magic collisions on square " + square);

        for (int index = 0; index < size; index++) {
            long blockers = indexToBlockers(index, mask);

            int hash = hash(blockers, square, true);
            long attacks = generateRookAttacksPerSquare(square, blockers);

            if (!used[hash]) {
                used[hash] = true;
                usedAttacks[hash] = attacks;
                usedBlockers[hash] = blockers;
            } else {
                if (usedAttacks[hash] != attacks) {
                    System.out.println("MAGIC COLLISION FOUND");
                    System.out.println("Square: " + square);
                    System.out.println("Hash index: " + hash);

                    System.out.println("Blockers A:");
                    printBitboard(usedBlockers[hash]);
                    System.out.println("Attacks A:");
                    printBitboard(usedAttacks[hash]);

                    System.out.println("Blockers B:");
                    printBitboard(blockers);
                    System.out.println("Attacks B:");
                    printBitboard(attacks);
                    return;
                }
            }
        }

        System.out.println("No rook magic collisions on square " + square);
    }

    


}   
