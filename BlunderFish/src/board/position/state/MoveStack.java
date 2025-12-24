package board.position.state;

public class MoveStack {
    long[] moves;
    int stackPointer;

    public MoveStack(int capacity) {
        moves = new long[capacity];
    }

    public void push (long move) {
        moves[stackPointer++] = move;
    }

    public long pop () {
        stackPointer--;
        return moves[stackPointer];
    }

    public long peek () {
        return moves[stackPointer - 1];
    }
}
