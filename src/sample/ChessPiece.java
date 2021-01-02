package sample;

import java.util.ArrayList;

public abstract class ChessPiece {
    protected Piece piece; //pawn, knight, bishop, rook, queen, king (as a enum)
    protected double numVal; //1, 3, 3, 5, 10, 100
    protected int[] location; // {i,j}
    //Early Game Piece Values
    private double[][] pawnWeighting4WhiteEarly = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                    {1.5, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.5},
                                                    {0.9, 1.0, 1.2, 1.5, 1.5, 1.2, 1.0, 0.9},
                                                    {0.6, 1.2, 1.2, 1.6, 1.6, 1.2, 1.2, 0.6},
                                                    {0.5, 0.6, 1.2, 1.4, 1.6, 1.2, 0.6, 0.5},
                                                    {0.2, 0.7, 1.0, 1.1, 1.3, 0.7, 0.7, 0.2},
                                                    {0.7, 0.7, 0.9, 1.0, 1.0, 1.0, 0.7, 0.7},
                                                    {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

    private double[][] knightWeighting4WhiteEarly = {{2.8, 2.5, 2.5, 2.6, 2.6, 2.5, 2.5, 2.8},
                                                    {2.6, 2.7, 3.3, 3.0, 3.0, 3.3, 2.7, 2.6},
                                                    {2.7, 3.0, 3.4, 3.4, 3.4, 3.4, 3.0, 2.7},
                                                    {2.7, 4.1, 3.7, 4.4, 4.4, 3.7, 4.1, 2.7},
                                                    {2.7, 3.0, 3.7, 4.4, 4.4, 3.7, 3.0, 2.7},
                                                    {2.8, 2.8, 3.5, 3.0, 3.0, 3.5, 2.8, 2.8},
                                                    {1.0, 2.1, 2.4, 2.4, 2.4, 2.4, 2.1, 1.0},
                                                    {0.3, 2.0, 1.4, 1.4, 1.4, 1.4, 2.0, 0.3}};

    private double[][] kingWeighting4WhiteEarly = {{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                    {90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                    {90.0, 93.0, 93.0, 93.0, 93.0, 93.0, 93.0, 90.0},
                                                    {93.0, 94.0, 94.0, 94.5, 94.5, 94.0, 94.0, 93.0},
                                                    {94.5, 95.0, 95.0, 95.0, 95.0, 95.0, 95.0, 94.5},
                                                    {96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5},
                                                    {98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0},
                                                    {100.0, 100.0, 100.7, 99.0, 100.0, 98.0, 101.8, 100.4}};

    //Mid Game Piece Values
    private double[][] pawnWeighting4WhiteMid = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                {1.6, 1.6, 1.8, 1.8, 1.8, 1.8, 1.6, 1.6},
                                                {0.9, 1.0, 1.2, 1.5, 1.5, 1.2, 1.0, 0.9},
                                                {0.8, 1.1, 1.3, 1.6, 1.6, 1.3, 1.1, 0.8},
                                                {0.9, 1.0, 1.0, 1.6, 1.6, 1.0, 1.0, 0.9},
                                                {0.9, 1.0, 1.1, 1.3, 1.3, 1.1, 1.0, 0.9},
                                                {0.9, 0.9, 0.9, 0.7, 0.7, 1.0, 1.0, 1.0},
                                                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

    private double[][] knightWeighting4WhiteMid = {{2.8, 2.1, 2.1, 2.1, 2.1, 2.1, 2.1, 2.8},
                                                    {2.4, 3.0, 3.2, 2.7, 2.7, 3.2, 3.0, 2.4},
                                                    {2.5, 3.1, 3.4, 3.4, 3.4, 3.4, 3.1, 2.5},
                                                    {2.6, 3.1, 3.5, 3.9, 3.9, 3.5, 3.1, 2.6},
                                                    {2.6, 3.1, 3.5, 3.9, 3.9, 3.5, 3.1, 2.6},
                                                    {2.5, 3.1, 3.4, 3.2, 3.2, 3.4, 3.1, 2.5},
                                                    {2.1, 2.4, 2.6, 2.7, 2.7, 2.6, 2.4, 2.1},
                                                    {1.6, 2.0, 1.7, 1.7, 1.7, 1.7, 2.0, 1.6}};

    private double[][] kingWeighting4WhiteMid = {{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                {90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                {90.0, 93.0, 93.0, 93.0, 93.0, 93.0, 93.0, 90.0},
                                                {93.0, 94.0, 94.0, 94.5, 94.5, 94.0, 94.0, 93.0},
                                                {94.5, 95.0, 95.0, 95.0, 95.0, 95.0, 95.0, 94.5},
                                                {96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5},
                                                {98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0},
                                                {100.0, 100.0, 100.7, 99.0, 100.0, 98.0, 101.8, 100.0}};

    //Endgame Piece Values
    private double[][] pawnWeighting4WhiteEnd = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                {4.0, 3.8, 3.6, 3.6, 3.6, 3.6, 3.8, 4.0},
                                                {2.8, 2.7, 2.5, 2.4, 2.4, 2.5, 2.7, 2.8},
                                                {2.4, 2.2, 2.0, 1.8, 1.8, 2.0, 2.2, 2.4},
                                                {2.0, 1.8, 1.6, 1.4, 1.4, 1.6, 1.8, 2.0},
                                                {1.2, 1.1, 1.0, 0.9, 0.9, 1.0, 1.1, 1.2},
                                                {1.2, 1.1, 1.0, 0.9, 0.9, 1.0, 1.1, 1.2},
                                                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

    private double[][] knightWeighting4WhiteEnd = {{2.1, 2.3, 2.3, 2.3, 2.3, 2.3, 2.3, 2.1},
                                                    {2.3, 2.7, 2.7, 2.7, 2.7, 2.7, 2.7, 2.3},
                                                    {2.3, 2.7, 2.9, 2.9, 2.9, 2.9, 2.7, 2.3},
                                                    {2.3, 2.7, 2.9, 3.0, 3.0, 2.9, 2.7, 2.3},
                                                    {2.3, 2.7, 2.9, 3.0, 3.0, 2.9, 2.7, 2.3},
                                                    {2.3, 2.7, 2.9, 2.9, 2.9, 2.9, 2.7, 2.3},
                                                    {2.3, 2.7, 2.7, 2.7, 2.7, 2.7, 2.7, 2.3},
                                                    {2.1, 2.3, 2.3, 2.3, 2.3, 2.3, 2.3, 2.1}};

    //adjust these numbers to give more weight to the king staying on the same side
    private double[][] kingWeighting4WhiteEnd = {{97.0, 97.5, 97.6, 97.7, 97.7, 97.6, 97.5, 97.0},
                                                {97.5, 98.0, 98.4, 98.7, 98.7, 98.4, 98.0, 97.5},
                                                {97.6, 98.4, 99.0, 99.2, 99.2, 99.0, 98.4, 97.6},
                                                {97.7, 98.7, 98.2, 99.9, 99.9, 99.2, 98.7, 97.7},
                                                {97.7, 98.7, 99.2, 99.9, 99.9, 99.2, 98.7, 97.7},
                                                {97.6, 98.4, 99.0, 99.2, 99.2, 99.0, 98.4, 97.6},
                                                {97.5, 98.0, 98.4, 98.7, 98.7, 98.4, 98.0, 97.5},
                                                {97.0, 97.5, 97.6, 97.7, 97.7, 97.6, 97.5, 97.0}};

    protected ChessPiece(Piece piece, double numVal, int locI, int locJ) {
        this.piece = piece;
        this.numVal = numVal;
        this.location = new int[]{locI, locJ};
    }

    //Returns type of piece as enum value (Piece.KING, Piece.ROOK, etc...)
    public Piece getPiece() {
        return piece;
    }
    public double getNumVal() {
        return numVal;
    }
    public int[] getLocation() {
        return location;
    }
    public void setLocation(int locI, int locJ) {
        this.location[0] = locI;
        this.location[1] = locJ;
    }

    protected abstract String getImageName();
    protected abstract boolean isWhite();

    public double getNumValEarly(ChessPiece[][] board) {
        int loc0 = (isWhite()) ? location[0] : (7-location[0]);
        if (piece == Piece.PAWN) {
            return pawnWeighting4WhiteEarly[loc0][location[1]] + getIncr(board); //If defended, value increases
        } else if (piece == Piece.KNIGHT) {
            return knightWeighting4WhiteEarly[loc0][location[1]];
        } else if (piece == Piece.KING) {
            return kingWeighting4WhiteEarly[loc0][location[1]];
        }
        return numVal;
    }
    public double getNumValMid(ChessPiece[][] board) {
        int loc0 = (isWhite()) ? location[0] : (7-location[0]);
        if (piece == Piece.PAWN) {
            return pawnWeighting4WhiteMid[loc0][location[1]] + getIncr(board);
        } else if (piece == Piece.KNIGHT) {
            return knightWeighting4WhiteMid[loc0][location[1]];
        } else if (piece == Piece.KING) {
            return kingWeighting4WhiteMid[loc0][location[1]];
        }
        return numVal;
    }
    public double getNumValEnd(ChessPiece[][] board) {
        int loc0 = (isWhite()) ? location[0] : (7-location[0]);
        if (piece == Piece.PAWN) {
            double incr = getIncr(board);
            if (incr == 0.2) {
                incr = 0.3; //more valuable in the endgame
            } else if (incr == 0.3) {
                incr = 0.45;
            }
            return pawnWeighting4WhiteEnd[loc0][location[1]] + incr;
        } else if (piece == Piece.KNIGHT) {
            return knightWeighting4WhiteEnd[loc0][location[1]];
        } else if (piece == Piece.KING) {
            return kingWeighting4WhiteEnd[loc0][location[1]];
        }
        return numVal;
    }
    //If a pawn is protected by another pawn, the value goes up
    private double getIncr(ChessPiece[][] board) {
        double incr = 0.0;
        int incrlocI = (isWhite()) ? 1 : -1;
        ChessPiece piece1 = board[location[0] + incrlocI][location[1] - 1];
        if (location[1] != 0 && piece1 != null && piece1.getPiece() == piece && piece1.isWhite() == isWhite()) {
            incr = 0.2;
        }
        ChessPiece piece2 = board[location[0] + incrlocI][location[1] + 1];
        if (location[1] != 7 && piece2 != null && piece2.getPiece() == piece && piece2.isWhite() == isWhite()) {
            if (incr == 0.2) {
                incr = 0.3;
            } else {
                incr = 0.2;
            }
        }
        return incr;
    }

    public ArrayList<Integer[]> getPossibleMoves(BoardClass boardClass) {
        switch(piece) {
            case PAWN:
                return getPosMoves4Pawn(boardClass.board);
            case KNIGHT:
                return getPosMoves4Knight(boardClass.board);
            case BISHOP:
                return getPosMoves4Bishop(boardClass.board);
            case ROOK:
                return getPosMoves4Rook(boardClass.board);
            case QUEEN:
                //adds all the possible moves of rook to that of bishop
                ArrayList<Integer[]> posMoves4Queen = getPosMoves4Bishop(boardClass.board);
                posMoves4Queen.addAll(getPosMoves4Rook(boardClass.board));
                return posMoves4Queen;
            case KING:
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
        //Checks if allowed to castle
        if (boardClass.canCastleKingSide(isWhite())) {
            posMoves.add(new Integer[]{((isWhite()) ? 7 : 0),6});
        }
        if (boardClass.canCastleQueenSide(isWhite())) {
            posMoves.add(new Integer[]{((isWhite()) ? 7 : 0),2});
        }

        return posMoves;
    }
    public boolean isKingMovePossible(BoardClass boardClass, int locI, int locJ, int newI, int newJ) {
        ChessPiece origPiece = boardClass.board[newI][newJ];
        tempMovePiece(locI, locJ, newI, newJ, boardClass.board, null);
        boolean isMovePossible = !boardClass.isInCheck(isWhite()) && !isNextToOpposingKing(boardClass.board, newI, newJ);
        tempMovePiece(newI, newJ, locI, locJ, boardClass.board, origPiece);
        return isMovePossible;
    }
    protected void tempMovePiece(int initI, int initJ, int finI, int finJ, ChessPiece[][] board, ChessPiece toAddInPlace) { //Assumes move is possible
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(finI, finJ);
        board[initI][initJ] = toAddInPlace;
    }

    //Checks if the new position would be next to the opposing king
    private boolean isNextToOpposingKing(ChessPiece[][] board, int locI, int locJ) {
        int[] iChanges = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] jChanges = {-1, -1, -1, 0, 0, 1, 1, 1};
        for (int i = 0; i < iChanges.length; i++) {
            int newI = locI + iChanges[i];
            int newJ = locJ + jChanges[i];
            //Checks if in bounds
            if (newI >= 0 && newI < 8 && newJ >= 0 && newJ < 8) {
                //If the spot at [newI][newJ] is occupied by opposing king
                if (board[newI][newJ] != null && board[newI][newJ].getPiece() == Piece.KING && board[newI][newJ].isWhite() != isWhite()) {
                    System.out.println("King is next to the thing at " + locI + " " + locJ);
                    return true;
                }
            }
        }
        return false;
    }

    //For debugging purposes
    public String toString() {
        return piece + " - [" + location[0] + "][" + location[1] + "]";
    }
}
