package sample;

import java.util.ArrayList;

public abstract class ChessPiece {
    protected String name; //pawn, knight, bishop, rook, queen, king
    protected double numVal; //1, 3, 3, 5, 10, 100
    protected int[] location = new int[2]; // {i,j}

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

    public ArrayList<Integer[]> getPossibleMoves(BoardClass boardClass) {
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
            if (newI >= 0 && newI < 8 && newJ >= 0 && newJ < 8 && (board[newI][newJ] == null || board[newI][newJ].isWhite() != isWhite())) {
                posMoves.add(new Integer[]{newI, newJ});
            }
        }
        return posMoves;
    }

    private ArrayList<Integer[]> getPosMoves4Bishop(ChessPiece[][] board) {
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        //Go TL
        int tempI = location[0] - 1;
        int tempJ = location[1] - 1;
        while(tempI >= 0 && tempJ >= 0 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) { //Within bounds and not same piece
            posMoves.add(new Integer[]{tempI, tempJ});
            //Different Piece
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI--;
            tempJ--;
        }
        //Go BL
        tempI = location[0] + 1;
        tempJ = location[1] - 1;
        while(tempI < 8 && tempJ >= 0 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) {
            posMoves.add(new Integer[]{tempI, tempJ});
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI++;
            tempJ--;
        }
        //Go TR
        tempI = location[0] - 1;
        tempJ = location[1] + 1;
        while(tempI >= 0 && tempJ < 8 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) {
            posMoves.add(new Integer[]{tempI, tempJ});
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI--;
            tempJ++;
        }
        //Go BR
        tempI = location[0] + 1;
        tempJ = location[1] + 1;
        while(tempI < 8 && tempJ < 8 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) {
            posMoves.add(new Integer[]{tempI, tempJ});
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI++;
            tempJ++;
        }
        return posMoves;
    }

    private ArrayList<Integer[]> getPosMoves4Rook(ChessPiece[][] board) {
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        //Go L
        int tempI = location[0];
        int tempJ = location[1] - 1;
        while(tempJ >= 0 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) { //Within bounds and not same piece
            posMoves.add(new Integer[]{tempI, tempJ});
            //Different Piece
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempJ--;
        }
        //Go Up
        tempI = location[0] - 1;
        tempJ = location[1];
        while(tempI >= 0 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) {
            posMoves.add(new Integer[]{tempI, tempJ});
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI--;
        }
        //Go R
        tempI = location[0];
        tempJ = location[1] + 1;
        while(tempJ < 8 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) {
            posMoves.add(new Integer[]{tempI, tempJ});
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempJ++;
        }
        //Go D
        tempI = location[0] + 1;
        tempJ = location[1];
        while(tempI < 8 && (board[tempI][tempJ] == null || board[tempI][tempJ].isWhite() != isWhite())) {
            posMoves.add(new Integer[]{tempI, tempJ});
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI++;
        }
        return posMoves;
    }

    private ArrayList<Integer[]> getPosMoves4King(BoardClass boardClass) {
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        int[] iChanges = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] jChanges = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < iChanges.length; i++) {
            int newI = location[0] + iChanges[i];
            int newJ = location[1] + jChanges[i];
            //Checks if it's in bounds and not the same color piece
            if (newI >= 0 && newI < 8 && newJ >= 0 && newJ < 8 && (boardClass.board[newI][newJ] == null || boardClass.board[newI][newJ].isWhite() != isWhite())) {
                //Checks if the move is possible
                if (isKingMovePossible(boardClass, location[0], location[1], newI, newJ)) {
                    posMoves.add(new Integer[]{newI, newJ});
                }
            }
        }
        //Still need to implement the castling stuff here!!

        return posMoves;
    }
    public boolean isKingMovePossible(BoardClass boardClass, int locI, int locJ, int newI, int newJ) {
        ChessPiece origPiece = boardClass.board[newI][newJ];
        tempMovePiece(locI, locJ, newI, newJ, boardClass.board, null);
        boolean isMovePossible = !boardClass.isInCheck(isWhite()) && !isNextToOpposingKing(boardClass.board, new int[]{newI, newJ});
        tempMovePiece(newI, newJ, locI, locJ, boardClass.board, origPiece);
        return isMovePossible;
    }
    protected void tempMovePiece(int initI, int initJ, int finI, int finJ, ChessPiece[][] board, ChessPiece toAddInPlace) { //Assumes move is possible
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(new int[]{finI, finJ});
        board[initI][initJ] = toAddInPlace;
    }

    //Checks if the new position would be next to the opposing king
    private boolean isNextToOpposingKing(ChessPiece[][] board, int[] locKing) {
        int[] iChanges = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] jChanges = {-1, -1, -1, 0, 0, 1, 1, 1};
        for (int i = 0; i < iChanges.length; i++) {
            int newI = locKing[0] + iChanges[i];
            int newJ = locKing[1] + jChanges[i];
            //Checks if in bounds
            if (newI >= 0 && newI < 8 && newJ >= 0 && newJ < 8) {
                //If the spot at [newI][newJ] is occupied by opposing king
                if (board[newI][newJ] != null && board[newI][newJ].getName().equals("king") && board[newI][newJ].isWhite() != isWhite()) {
                    System.out.println("King is next to the thing at " + locKing[0] + " " + locKing[1]);
                    return true;
                }
            }
        }
        return false;
    }

    //For debugging purposes
    public String toString() {
        return name + " - [" + location[0] + "][" + location[1] + "]";
    }

}
