package board.position.moves;

import java.util.Arrays;

import board.position.moves.helper.Move;

public class MoveBuffer {
    private long[] internalArray;
    private int size;

    public MoveBuffer(int maxCapacity) {
        internalArray = new long[maxCapacity];
    }

    public void append(long move) {
        internalArray[size++] = move;
    }

    public long getElementByIndex(int index) {
        return internalArray[index];
    }

    public int size() {
        return size;
    } 

    public void clear() {
        Arrays.fill(internalArray, 0L);
    }

    public void printMoveList() {
        for (int index = 0; index < size; index++) {
            System.out.println(Move.toString(internalArray[index]));
        }
        System.out.println("Size: " + size);
    }
}
