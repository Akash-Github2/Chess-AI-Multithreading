package sample;

import java.util.ArrayList;

public class BlackPiece extends ChessPiece {
    public BlackPiece(String name, double numVal, int i, int j) {
        super(name, numVal, new int[]{i,j});
    }
    public String getImageName() {
        return name + "_black";
    }
    public boolean isWhite() {
        return false;
    }

    protected ArrayList<Integer[]> getPosMoves4Pawn(ChessPiece[][] board) {
        return new ArrayList<>();
    }
}
