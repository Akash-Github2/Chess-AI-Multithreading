package sample;

import java.util.ArrayList;

public class WhitePiece extends ChessPiece {
    public WhitePiece(String name, double numVal, int i, int j) {
        super(name, numVal, new int[]{i,j});
    }
    public String getImageName() {
        return name + "_white";
    }
    public boolean isWhite() {
        return true;
    }

    protected ArrayList<Integer[]> getPosMoves4Pawn(ChessPiece[][] board) {
        return new ArrayList<>();
    }
}
