package sample;

import java.util.ArrayList;

public class WhitePiece extends ChessPiece {
    public WhitePiece(Piece piece, double numVal, int i, int j) {
        super(piece, numVal, i,j);
    }
    public String getImageName() {
        return piece + "_white";
    }
    public boolean isWhite() {
        return true;
    }

    protected ArrayList<Integer[]> getPosMoves4Pawn(ChessPiece[][] board) {
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        //If it can move 1 space up
        if (location[0] != 0 && board[location[0]-1][location[1]] == null) {
            posMoves.add(new Integer[]{location[0]-1, location[1]});
        }
        //If it can move 2 spaces up
        if (location[0] == 6 && board[location[0]-2][location[1]] == null && board[location[0]-1][location[1]] == null) {
            posMoves.add(new Integer[]{location[0]-2, location[1]});
        }
        //Checks if piece can move diagonally right
        if (location[0] != 0 && location[1] != 7 && board[location[0]-1][location[1]+1] != null && !board[location[0]-1][location[1]+1].isWhite()) {
            posMoves.add(new Integer[]{location[0]-1, location[1]+1});
        }
        //Checks if piece can move diagonally left
        if (location[0] != 0 && location[1] != 0 && board[location[0]-1][location[1]-1] != null && !board[location[0]-1][location[1]-1].isWhite()) {
            posMoves.add(new Integer[]{location[0]-1, location[1]-1});
        }
        return posMoves;
    }
}
