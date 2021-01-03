package sample;

import java.util.ArrayList;

public class BlackPiece extends ChessPiece {
    public BlackPiece(Piece piece, double numVal, int i, int j) {
        super(piece, numVal, i,j);
    }
    //Check that it is an instance of BlackPiece before using this copy constructor
    public BlackPiece(ChessPiece blackPiece) {
        super(blackPiece.getPiece(), blackPiece.getNumVal(), blackPiece.getLocation()[0], blackPiece.getLocation()[1]);
    }
    public String getImageName() {
        return piece + "_black";
    }
    public boolean isWhite() {
        return false;
    }

    protected ArrayList<Integer[]> getPosMoves4Pawn(ChessPiece[][] board) {
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        //If it can move 1 space up
        if (location[0] != 7 && board[location[0]+1][location[1]] == null) {
            posMoves.add(new Integer[]{location[0]+1, location[1]});
        }
        //If it can move 2 spaces up
        if (location[0] == 1 && board[location[0]+2][location[1]] == null && board[location[0]+1][location[1]] == null) {
            posMoves.add(new Integer[]{location[0]+2, location[1]});
        }
        //Checks if piece can move diagonally right
        if (location[0] != 7 && location[1] != 7 && board[location[0]+1][location[1]+1] != null && board[location[0]+1][location[1]+1].isWhite()) {
            posMoves.add(new Integer[]{location[0]+1, location[1]+1});
        }
        //Checks if piece can move diagonally left
        if (location[0] != 7 && location[1] != 0 && board[location[0]+1][location[1]-1] != null && board[location[0]+1][location[1]-1].isWhite()) {
            posMoves.add(new Integer[]{location[0]+1, location[1]-1});
        }
        return posMoves;
    }
}
