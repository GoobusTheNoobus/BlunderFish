package board.position.state;

import board.position.moves.helper.Move;
import java.util.Arrays;

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

    public void printStack () {
        for (int i = stackPointer; i >= 0; i--) {
            System.out.println(Move.toString(moves[i]));
        }
    }
    
    @Override
    public MoveStack clone() throws CloneNotSupportedException {
        MoveStack newMoveStack = new MoveStack(moves.length);

        newMoveStack.moves = Arrays.copyOf(moves, moves.length);
        newMoveStack.stackPointer = stackPointer;

        return newMoveStack;
    }
}
