package sample;

import java.util.ArrayList;

public class BoardClass {
    public ChessPiece[][] board = new ChessPiece[8][8];
    public ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    public ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    public int[] selectedLoc = new int[]{-1,-1};

    public BoardClass() {
        initializeBoard();
        //Populates the whitePieces & blackPieces ArrayLists
        for (int i = 0; i < board[0].length; i++) {
            if (board[1][i] != null) {
                blackPieces.add(board[1][i]);
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            if (i != 4 && board[0][i] != null) {
                blackPieces.add(board[0][i]);
            }
        }
        blackPieces.add(board[0][4]); //Adds king at the end
        for (int i = 0; i < board[0].length; i++) {
            if (board[6][i] != null) {
                whitePieces.add(board[6][i]);
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            if (i != 4 && board[7][i] != null) {
                whitePieces.add(board[7][i]);
            }
        }
        whitePieces.add(board[7][4]);
    }

    public int[] getSelectedLoc() {
        return selectedLoc;
    }
    public void toggleSelectedLoc(int i, int j) {
        if (selectedLoc[0] == i && selectedLoc[1] == j) { //if clicking a selected spot, it un-selects it
            selectedLoc[0] = -1;
            selectedLoc[1] = -1;
        } else { //changes the selected location to the new coordinates
            this.selectedLoc[0] = i;
            this.selectedLoc[1] = j;
        }
    }

    public void movePiece(int[] initLoc, int[] finLoc) { //Assumes the move is possible
        int initI = initLoc[0];
        int initJ = initLoc[1];
        int finI = finLoc[0];
        int finJ = finLoc[1];
        if (board[finI][finJ] != null) {
            ArrayList<ChessPiece> toCheck = blackPieces;
            if (!board[initI][initJ].isWhite()) {
                toCheck = whitePieces;
            }
            //Removes the opponent's piece that will be taken from the arraylist
            for (int i = 0; i < toCheck.size(); i++) {
                int[] tempLoc = toCheck.get(i).getLocation();
                if (tempLoc[0] == finI && tempLoc[1] == finJ) {
                    toCheck.remove(i);
                    break;
                }
            }
        }
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(new int[]{finI,finJ});
        board[initI][initJ] = null;
    }

    public boolean isInCheck(boolean isWhite) {
        int[] locKing = new int[2];
        ArrayList<ChessPiece> defendingPieces = whitePieces; //The one that might be in check
        ArrayList<ChessPiece> attackingPieces = blackPieces;
        if (!isWhite) {
            defendingPieces = blackPieces;
            attackingPieces = whitePieces;
        }
        //Locates King
        for (int i = 0; i < defendingPieces.size(); i++) {
            if (defendingPieces.get(i).getName().equals("king")) {
                locKing = defendingPieces.get(i).getLocation();
            }
        }
        //Sees if possible moves land on Opposing King
        for (int i = 0; i < attackingPieces.size(); i++) {
            if (!attackingPieces.get(i).getName().equals("king")) { //King can't check other king so won't include King to save time
                ArrayList<Integer[]> tempPosMoves = attackingPieces.get(i).getAllPossibleMoves(this);
                for (int j = 0; j < tempPosMoves.size(); j++) {
                    if (tempPosMoves.get(j)[0] == locKing[0] && tempPosMoves.get(j)[1] == locKing[1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }
        }
        //Black Pieces
        board[0][0] = new BlackPiece("rook", 5, 0, 0);
        board[0][1] = new BlackPiece("knight", 3, 0, 1);
        board[0][2] = new BlackPiece("bishop", 3, 0, 2);
        board[0][3] = new BlackPiece("queen", 10, 0, 3);
        board[0][4] = new BlackPiece("king", 100, 0, 4);
        board[0][5] = new BlackPiece("bishop", 3, 0, 5);
        board[0][6] = new BlackPiece("knight", 3, 0, 6);
        board[0][7] = new BlackPiece("rook", 5, 0, 7);
        //White Pieces
        board[7][0] = new WhitePiece("rook", 5, 7, 0);
        board[7][1] = new WhitePiece("knight", 3, 7, 1);
        board[7][2] = new WhitePiece("bishop", 3, 7, 2);
        board[7][3] = new WhitePiece("queen", 10, 7, 3);
        board[7][4] = new WhitePiece("king", 100, 7, 4);
        board[7][5] = new WhitePiece("bishop", 3, 7, 5);
        board[7][6] = new WhitePiece("knight", 3, 7, 6);
        board[7][7] = new WhitePiece("rook", 5, 7, 7);

        //Pawns
        for (int j = 0; j < board[0].length; j++) {
            board[1][j] = new BlackPiece("pawn", 1, 1, j);
            board[6][j] = new WhitePiece("pawn", 1, 6, j);
        }
    }
}
