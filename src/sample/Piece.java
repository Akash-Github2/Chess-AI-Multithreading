package sample;

public enum Piece {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
