/* ---------------------MY FIRST CHESS ENGINE--------------------- */

package board.position.moves;

import board.position.moves.helper.Move;

public class MoveBuffer {
    public long[] internalArray;
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
        size = 0;
    }

    public void printMoveList() {
        for (int index = 0; index < size; index++) {
            System.out.printf("Move: %s (%d)\n", Move.toString(internalArray[index]), internalArray[index]);
        }
        System.out.println("Size: " + size);
    }
}
