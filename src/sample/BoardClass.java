package sample;

import java.util.ArrayList;

public class BoardClass {
    public ChessPiece[][] board = new ChessPiece[8][8];
    public ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    public ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    private int[] selectedLoc = new int[]{-1,-1};

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

    //Moves the chess piece to another location
    public void movePiece(int initI, int initJ, int finI, int finJ) { //Assumes the move is possible
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
        board[finI][finJ].setLocation(finI,finJ);
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
            if (defendingPieces.get(i).getPiece() == Piece.KING) {
                locKing = defendingPieces.get(i).getLocation();
            }
        }
        //Sees if possible moves land on Opposing King
        for (int i = 0; i < attackingPieces.size(); i++) {
            if (attackingPieces.get(i).getPiece() != Piece.KING) { //King can't check other king so won't include King to save time
                ArrayList<Integer[]> tempPosMoves = attackingPieces.get(i).getPossibleMoves(this);
                for (int j = 0; j < tempPosMoves.size(); j++) {
                    if (tempPosMoves.get(j)[0] == locKing[0] && tempPosMoves.get(j)[1] == locKing[1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckMate(boolean isWhite) {
        if (!isInCheck(isWhite)) {
            return false;
        }
        boolean shouldReturn = false;
        int[] locKing = new int[2];
        ArrayList<ChessPiece> defendingPieces = (isWhite) ? whitePieces : blackPieces; //The one that might be in check
        //Locates King
        for (int i = 0; i < defendingPieces.size(); i++) {
            if (defendingPieces.get(i).getPiece() == Piece.KING) {
                locKing = defendingPieces.get(i).getLocation();
            }
        }
        //Other pieces move to protect king
        for (int i = 0; i < defendingPieces.size(); i++) {
            if (defendingPieces.get(i).getPiece() != Piece.KING) {
                ArrayList<Integer[]> tempPossibleMoves = defendingPieces.get(i).getPossibleMoves(this);
                int locI = defendingPieces.get(i).getLocation()[0];
                int locJ = defendingPieces.get(i).getLocation()[1];
                for (int j = 0; j < tempPossibleMoves.size(); j++) {
                    ChessPiece origPiece = board[tempPossibleMoves.get(j)[0]][tempPossibleMoves.get(j)[1]];
                    movePiece(locI, locJ, tempPossibleMoves.get(j)[0], tempPossibleMoves.get(j)[1]);
                    shouldReturn = !isInCheck(isWhite); //if the move is possible it will return after reverting the board to what it originally was
                    movePiece(tempPossibleMoves.get(j)[0], tempPossibleMoves.get(j)[1], locI, locJ);
                    if (origPiece != null) {
                        if (isWhite) {
                            blackPieces.add(origPiece);
                        } else {
                            whitePieces.add(origPiece);
                        }
                    }
                    board[tempPossibleMoves.get(j)[0]][tempPossibleMoves.get(j)[1]] = origPiece;
                    if (shouldReturn) {
                        return false;
                    }
                }
            }
        }

        //Can King moves to safety
        ArrayList<Integer[]> kingPossibleMoves = board[locKing[0]][locKing[1]].getPossibleMoves(this);
        return kingPossibleMoves.size() == 0;
    }

    //Returns a list of all possible moves from all pieces
    public ArrayList<String> getAllPossibleMoves(boolean isWhite) {
        ArrayList<String> allPosMoves = new ArrayList<>();
        ArrayList<ChessPiece> currSide = (isWhite) ? whitePieces : blackPieces;
        for (int i = 0; i < currSide.size(); i++) {
            ArrayList<Integer[]> tempPosMoves = currSide.get(i).getPossibleMoves(this);
            int locI = currSide.get(i).getLocation()[0];
            int locJ = currSide.get(i).getLocation()[1];
            //Checks the all possible moves of a specific piece and sees which ones actually work
            for (int j = 0; j < tempPosMoves.size(); j++) {
                boolean works = false;
                ChessPiece origPiece = board[tempPosMoves.get(j)[0]][tempPosMoves.get(j)[1]];
                //tests to make sure the move doesn't put/leave the king in check
                movePiece(locI,locJ, tempPosMoves.get(j)[0], tempPosMoves.get(j)[1]);
                works = !isInCheck(isWhite);
                movePiece(tempPosMoves.get(j)[0], tempPosMoves.get(j)[1], locI,locJ);
                //only add the taken piece back if there was a piece on the board at that spot
                if (origPiece != null) {
                    if (isWhite) {
                        blackPieces.add(origPiece);
                    } else {
                        whitePieces.add(origPiece);
                    }
                }
                board[tempPosMoves.get(j)[0]][tempPosMoves.get(j)[1]] = origPiece;
                if (works) {
                    allPosMoves.add(locI + "," + locJ + "->" + tempPosMoves.get(j)[0] + "," + tempPosMoves.get(j)[1]);
                }
            }
        }
        return allPosMoves;
    }

    //Gets the possible moves for a piece - only the ones that don't result in the king being in check
    public ArrayList<Integer[]> getPosMoves4PieceAtLoc(int locI, int locJ) {
        ArrayList<String> allPosMoves = getAllPossibleMoves(board[locI][locJ].isWhite());
        ArrayList<Integer[]> posMovesForPiece = new ArrayList<>();
        for (String move : allPosMoves) {
            int initI = Integer.parseInt(move.substring(0, 1));
            int initJ = Integer.parseInt(move.substring(2, 3));
            int finI = Integer.parseInt(move.substring(5, 6));
            int finJ = Integer.parseInt(move.substring(7, 8));
            if (initI == locI && initJ == locJ) {
                posMovesForPiece.add(new Integer[]{finI, finJ});
            }
        }
        return posMovesForPiece;
    }

    public void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }
        }
        //Black Pieces
        board[0][0] = new BlackPiece(Piece.ROOK, 5, 0, 0);
        board[0][1] = new BlackPiece(Piece.KNIGHT, 3, 0, 1);
        board[0][2] = new BlackPiece(Piece.BISHOP, 3, 0, 2);
        board[0][3] = new BlackPiece(Piece.QUEEN, 10, 0, 3);
        board[0][4] = new BlackPiece(Piece.KING, 100, 0, 4);
        board[0][5] = new BlackPiece(Piece.BISHOP, 3, 0, 5);
        board[0][6] = new BlackPiece(Piece.KNIGHT, 3, 0, 6);
        board[0][7] = new BlackPiece(Piece.ROOK, 5, 0, 7);
        //White Pieces
        board[7][0] = new WhitePiece(Piece.ROOK, 5, 7, 0);
        board[7][1] = new WhitePiece(Piece.KNIGHT, 3, 7, 1);
        board[7][2] = new WhitePiece(Piece.BISHOP, 3, 7, 2);
        board[7][3] = new WhitePiece(Piece.QUEEN, 10, 7, 3);
        board[7][4] = new WhitePiece(Piece.KING, 100, 7, 4);
        board[7][5] = new WhitePiece(Piece.BISHOP, 3, 7, 5);
        board[7][6] = new WhitePiece(Piece.KNIGHT, 3, 7, 6);
        board[7][7] = new WhitePiece(Piece.ROOK, 5, 7, 7);

        //Pawns
        for (int j = 0; j < board[0].length; j++) {
            board[1][j] = new BlackPiece(Piece.PAWN, 1, 1, j);
            board[6][j] = new WhitePiece(Piece.PAWN, 1, 6, j);
        }
    }

    //For debugging purposes: for this to be more useful, have the toString() of the ChessPiece return the unicodeVal
    public String toString() {
        String retStr = "";
        for (int i = 0; i < board.length; i++) {
            retStr += "---------------------------------\n";
            for (int j = 0; j < board[i].length; j++) {
                retStr += "| " + ((board[i][j] != null) ? board[i][j] : " ") + " ";
            }
            retStr += "|\n";
        }
        retStr += "---------------------------------\n";
        return retStr;
    }
}
