package sample;

import java.util.ArrayList;

public abstract class ChessPiece {
    protected String name; //pawn, knight, bishop, rook, queen, king
    protected double numVal; //1, 3, 3, 5, 10, 100
    protected int[] location = new int[2]; // "ij" - use substring() to get i and j

    protected ChessPiece(String name, double numVal, int[] location) {
        this.name = name;
        this.numVal = numVal;
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    public String getName() {
        return name;
    }
    public double getNumVal() {
        return numVal;
    }
    public int[] getLocation() {
        return location;
    }
    public void setLocation(int[] location) {
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    protected abstract String getImageName();
    protected abstract boolean isWhite();

    public ArrayList<Integer[]> getAllPossibleMoves(BoardClass boardClass) {
        switch(name) {
            case "pawn":
                return getPosMoves4Pawn(boardClass.board);
            case "knight":
                return getPosMoves4Knight(boardClass.board);
            case "bishop":
                return getPosMoves4Bishop(boardClass.board);
            case "rook":
                return getPosMoves4Rook(boardClass.board);
            case "queen":
                //adds all the possible moves of rook to that of bishop
                ArrayList<Integer[]> posMoves4Queen = getPosMoves4Bishop(boardClass.board);
                posMoves4Queen.addAll(getPosMoves4Rook(boardClass.board));
                return posMoves4Queen;
            case "king":
                return getPosMoves4King(boardClass);
        }
        return new ArrayList<>();
    }

    protected abstract ArrayList<Integer[]> getPosMoves4Pawn(ChessPiece[][] board);

    private ArrayList<Integer[]> getPosMoves4Knight(ChessPiece[][] board) {
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        int[] iChanges = {1, 2, 2, 1, -1, -2, -2, -1};
        int[] jChanges = {2, 1, -1, -2, -2, -1, 1, 2};
        for (int i = 0; i < iChanges.length; i++) {
            int newI = location[0] + iChanges[i];
            int newJ = location[1] + jChanges[i];
            //determines if the new position is in bounds and if it's not their own color piece
            if (newI >= 0 && newI < board.length && newJ >= 0 && newJ < board[0].length && (board[newI][newJ] == null || board[newI][newJ].isWhite() != isWhite())) {
                posMoves.add(new Integer[]{newI, newJ});
            }
        }
        return posMoves;
    }

    private ArrayList<Integer[]> getPosMoves4Bishop(ChessPiece[][] board) {
        return new ArrayList<>();
    }

    private ArrayList<Integer[]> getPosMoves4Rook(ChessPiece[][] board) {
        return new ArrayList<>();
    }

    private ArrayList<Integer[]> getPosMoves4King(BoardClass boardClass) {
        return new ArrayList<>();
    }

}
