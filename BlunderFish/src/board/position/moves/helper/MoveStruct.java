package board.position.moves.helper;

import utils.Utility;

public final class MoveStruct {
    public int from;
    public int to;
    public int pieceCaptured;
    public int piecePromoted;
    public int previousCastlingRights;
    public int flag;
    public int previousEnPassantSquare;

    public MoveStruct(long move) {
        from = Move.getFromSquare(move);
        to = Move.getToSquare(move);
        pieceCaptured = Move.getCapturedPiece(move);
        piecePromoted = Move.getPromotedPiece(move);
        previousCastlingRights = Move.getCastlingRights(move);
        previousEnPassantSquare = Move.getPreviousEnPassantSquare(move);
        flag = Move.getFlag(move);
    }


    @Override
    public String toString() {
        return String.format("Move {\nfrom: %s\nto: %s\npiece captured: %d\npiece promoted: %d\nprevious en passant square: %s\ncastling rights: %s\nflag: %d\n}", Utility.getStringFromSquareInt(from), Utility.getStringFromSquareInt(to), pieceCaptured, piecePromoted, Utility.getStringFromSquareInt(previousEnPassantSquare), Integer.toBinaryString(previousCastlingRights), flag);
    }
}
