package board.position.state;

public class MoveStack {
    private long[] moves;
    private int stackPointer;

    public MoveStack(int capacity) {
        moves = new long[capacity];
    }

    public void push (long move) {
        moves[stackPointer++] = move;
    }

    public long pop () {
        stackPointer--;
        long move = moves[stackPointer];

        moves[stackPointer] = 0L;

        return move;
    }

    public long peek () {
        return moves[stackPointer - 1];
    }
}
