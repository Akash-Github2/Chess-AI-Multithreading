package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BoardClass {
    public ChessPiece[][] board = new ChessPiece[8][8];
    public ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    public ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    private final int[] selectedLoc = new int[]{-1,-1};
    public boolean wKingHasMoved = false;
    public boolean bKingHasMoved = false;
    public boolean wRookLHasMoved = false;
    public boolean bRookLHasMoved = false;
    public boolean wRookRHasMoved = false;
    public boolean bRookRHasMoved = false;
    public boolean isAlmostCheckmate = false;
    public int movesSinceNoCaptureOrPawn = 0;

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

    public BoardClass() {
        initializeBoard();
        //Populates the whitePieces & blackPieces ArrayLists
        initializeWhiteBlackLists();
    }

    //Makes the move - accounts for pawn promotion and castling as well as normal moves
    public boolean makeMoveOverall(int initI, int initJ, int finI, int finJ, boolean isTemp) {
        boolean didPawnPromo = false;
        boolean blackTurn = !board[initI][initJ].isWhite();
        if (blackTurn && board[initI][initJ].getPiece() == Piece.PAWN && finI == 7) { //Black Pawn Promotion
            ChessPiece promotionPiece = new BlackPiece(Piece.QUEEN, 10, finI, finJ);
            pawnPromotion(initI, initJ, finI, finJ, promotionPiece);
            didPawnPromo = true;
        } else if (!blackTurn && board[initI][initJ].getPiece() == Piece.PAWN && finI == 0) { //White Pawn Promotion
            ChessPiece promotionPiece = new WhitePiece(Piece.QUEEN,10, finI, finJ);
            pawnPromotion(initI, initJ, finI, finJ, promotionPiece);
            didPawnPromo = true;
        } else {
            //Adjusts certain values for future reference on castling
            if (!isTemp) {
                if (blackTurn) {
                    if (board[initI][initJ].getPiece() == Piece.KING) {
                        bKingHasMoved = true;
                    }
                    if (board[initI][initJ].getPiece() == Piece.ROOK && initI == 0 && initJ == 0) {
                        bRookLHasMoved = true;
                    }
                    if (board[initI][initJ].getPiece() == Piece.ROOK && initI == 0 && initJ == 7) {
                        bRookRHasMoved = true;
                    }
                } else {
                    if (board[initI][initJ].getPiece() == Piece.KING) {
                        wKingHasMoved = true;
                    }
                    if (board[initI][initJ].getPiece() == Piece.ROOK && initI == 7 && initJ == 0) {
                        wRookLHasMoved = true;
                    }
                    if (board[initI][initJ].getPiece() == Piece.ROOK && initI == 7 && initJ == 7) {
                        wRookRHasMoved = true;
                    }
                }
            }

            //Accounts for any type of move, except the pawn promotion, which was done above
            if (initI == 7 && initJ == 4 && finI == 7 && (finJ == 2 || finJ == 6) && board[initI][initJ].getPiece() == Piece.KING) { //Castling White
                movePieceCastling(initI, initJ, finI, finJ);
            } else if (initI == 0 && initJ == 4 && finI == 0 && (finJ == 2 || finJ == 6) && board[initI][initJ].getPiece() == Piece.KING) { //Castling Black
                movePieceCastling(initI, initJ, finI, finJ);
            } else {
                movePiece(initI, initJ, finI, finJ);
            }
        }
        return didPawnPromo;
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

    //Called if the desired move involves castling
    public void movePieceCastling(int initI, int initJ, int finI, int finJ) { //Assumes move is possible
        //Works for both white and black side - king side
        if (finJ == 6) {
            board[finI][5] = board[finI][7];
            board[finI][5].setLocation(finI, 5);
            board[finI][7] = null;
        }
        //queen side
        if (finJ == 2) {
            board[finI][3] = board[finI][0];
            board[finI][3].setLocation(finI, 3);
            board[finI][0] = null;
        }

        //For King Movement
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(finI, finJ);
        board[initI][initJ] = null;
    }

    //Moves piece in pawn promotion
    public void pawnPromotion(int initI, int initJ, int finI, int finJ, ChessPiece promotionPiece) {
        movePiece(initI, initJ, finI, finJ);
        ArrayList<ChessPiece> teamPieces = (board[finI][finJ].isWhite()) ? whitePieces : blackPieces;
        for (int a = 0; a < teamPieces.size(); a++) {
            int[] tempLoc = teamPieces.get(a).getLocation();
            if (tempLoc[0] == finI && tempLoc[1] == finJ) {
                teamPieces.remove(a);
                break;
            }
        }
        board[finI][finJ] = promotionPiece;
        teamPieces.add(board[finI][finJ]);
    }

    public void fullyResetMove(int initI, int initJ, int finI, int finJ, boolean isBlackTurn, boolean didPawnPromo, ChessPiece origPiece) {
        if (initI == 7 && initJ == 4 && finI == 7 && (finJ == 2 || finJ == 6) && board[finI][finJ].getPiece() == Piece.KING) { //Reset Castling White
            resetCastling(initI, initJ, finI, finJ);
        } else if (initI == 0 && initJ == 4 && finI == 0 && (finJ == 2 || finJ == 6) && board[finI][finJ].getPiece() == Piece.KING) { //Reset Castling Black
            resetCastling(initI, initJ, finI, finJ);
        } else {
            resetMove(initI, initJ, finI, finJ, isBlackTurn, didPawnPromo, origPiece);
        }
    }
    public void resetMove(int initI, int initJ, int finI, int finJ, boolean isBlackTurn, boolean didPawnPromo, ChessPiece origPiece) {
        movePiece(finI, finJ, initI, initJ);
        if (origPiece != null) {
            if (isBlackTurn) {
                whitePieces.add(origPiece);
            } else {
                blackPieces.add(origPiece);
            }
        }
        board[finI][finJ] = origPiece;
        if (didPawnPromo) {
            if (isBlackTurn) {
                ChessPiece bPawn = new BlackPiece(Piece.PAWN, 1, initI, initJ);
                for (int a = 0; a < blackPieces.size(); a++) {
                    int[] tempLoc = blackPieces.get(a).getLocation();
                    if (tempLoc[0] == initI && tempLoc[1] == initJ) {
                        blackPieces.remove(a);
                        break;
                    }
                }
                board[initI][initJ] = bPawn;
                blackPieces.add(board[initI][initJ]);
            } else {
                ChessPiece wPawn = new WhitePiece(Piece.PAWN, 1, initI, initJ);
                for (int a = 0; a < whitePieces.size(); a++) {
                    int[] tempLoc = whitePieces.get(a).getLocation();
                    if (tempLoc[0] == initI && tempLoc[1] == initJ) {
                        whitePieces.remove(a);
                        break;
                    }
                }
                board[initI][initJ] = wPawn;
                whitePieces.add(board[initI][initJ]);
            }
        }
    }
    public void resetCastling(int initI, int initJ, int finI, int finJ) { //Assumes move is possible
        if (finJ == 6) {
            board[finI][7] = board[finI][5];
            board[finI][7].setLocation(finI, 7);
            board[finI][5] = null;
        }
        if (finJ == 2) {
            board[finI][0] = board[finI][3];
            board[finI][0].setLocation(finI, 0);
            board[finI][3] = null;
        }
        //For King Movement
        board[initI][initJ] = board[finI][finJ];
        board[initI][initJ].setLocation(initI, initJ);
        board[finI][finJ] = null;
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
        for (ChessPiece defendingPiece : defendingPieces) {
            if (defendingPiece.getPiece() == Piece.KING) {
                locKing = defendingPiece.getLocation();
            }
        }
        //Sees if possible moves land on Opposing King
        for (ChessPiece attackingPiece : attackingPieces) {
            if (attackingPiece.getPiece() != Piece.KING) { //King can't check other king so won't include King to save time
                ArrayList<Integer[]> tempPosMoves = attackingPiece.getPossibleMoves(this);
                for (Integer[] tempPosMove : tempPosMoves) {
                    if (tempPosMove[0] == locKing[0] && tempPosMove[1] == locKing[1]) {
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
        for (ChessPiece defendingPiece : defendingPieces) {
            if (defendingPiece.getPiece() == Piece.KING) {
                locKing = defendingPiece.getLocation();
            }
        }
        //Other pieces move to protect king
        for (ChessPiece defendingPiece : defendingPieces) {
            if (defendingPiece.getPiece() != Piece.KING) {
                ArrayList<Integer[]> tempPosMoves = defendingPiece.getPossibleMoves(this);
                int locI = defendingPiece.getLocation()[0];
                int locJ = defendingPiece.getLocation()[1];
                for (Integer[] tempPosMove : tempPosMoves) {
                    ChessPiece origPiece = board[tempPosMove[0]][tempPosMove[1]];
                    movePiece(locI, locJ, tempPosMove[0], tempPosMove[1]);
                    shouldReturn = !isInCheck(isWhite); //if the move is possible it will return after reverting the board to what it originally was
                    movePiece(tempPosMove[0], tempPosMove[1], locI, locJ);
                    if (origPiece != null) {
                        if (isWhite) {
                            blackPieces.add(origPiece);
                        } else {
                            whitePieces.add(origPiece);
                        }
                    }
                    board[tempPosMove[0]][tempPosMove[1]] = origPiece;
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

    //Tests different cases where the game would be a tie to determine if it is
    public boolean isTie(boolean isWhite, HashMap<String, Integer> boardFreq) {
        ArrayList<String> allPosMoves = getAllPossibleMoves(isWhite);
        if (allPosMoves.size() == 0 || (whitePieces.size() == 1 && blackPieces.size() == 1)) {
            return true;
        }
        if (whitePieces.size() == 2 && blackPieces.size() == 1) {
            for (ChessPiece whitePiece : whitePieces) {
                if (whitePiece.getPiece() == Piece.BISHOP || whitePiece.getPiece() == Piece.KNIGHT) {
                    return true;
                }
            }
        } else if (whitePieces.size() == 1 && blackPieces.size() == 2) {
            for (ChessPiece blackPiece : blackPieces) {
                if (blackPiece.getPiece() == Piece.BISHOP || blackPiece.getPiece() == Piece.KNIGHT) {
                    return true;
                }
            }
        } else if (whitePieces.size() == 2 && blackPieces.size() == 2) {
            int wBishopIndex = -1;
            int bBishopIndex = -1;
            for (int i = 0; i < whitePieces.size(); i++) {
                if (whitePieces.get(i).getPiece() == Piece.BISHOP){
                    wBishopIndex = i;
                    break;
                }
            }
            for (int i = 0; i < blackPieces.size(); i++) {
                if (blackPieces.get(i).getPiece() == Piece.BISHOP){
                    bBishopIndex = i;
                    break;
                }
            }
            if (wBishopIndex != -1 && bBishopIndex != -1) {
                int[] wBishopLoc = whitePieces.get(wBishopIndex).getLocation();
                int[] bBishopLoc = blackPieces.get(bBishopIndex).getLocation();
                if ((wBishopLoc[0] + wBishopLoc[1]) % 2 == (bBishopLoc[0] + bBishopLoc[1]) % 2){
                    return true;
                }
            }
        }
        if (movesSinceNoCaptureOrPawn == 100) { //50 moves with no capture or pawn movement
            return true;
        }

        //Threefold Rule
        return boardFreq.containsValue(3);
    }

    //Returns a list of all possible moves from all pieces
    public ArrayList<String> getAllPossibleMoves(boolean isWhite) {
        ArrayList<String> allPosMoves = new ArrayList<>();
        ArrayList<ChessPiece> currSide = (isWhite) ? whitePieces : blackPieces;
        for (ChessPiece chessPiece : currSide) {
            ArrayList<Integer[]> tempPosMoves = chessPiece.getPossibleMoves(this);
            int locI = chessPiece.getLocation()[0];
            int locJ = chessPiece.getLocation()[1];
            //Checks the all possible moves of a specific piece and sees which ones actually work
            for (Integer[] tempPosMove : tempPosMoves) {
                boolean works = false;
                ChessPiece origPiece = board[tempPosMove[0]][tempPosMove[1]];
                //tests to make sure the move doesn't put/leave the king in check
                movePiece(locI, locJ, tempPosMove[0], tempPosMove[1]);
                works = !isInCheck(isWhite);
                movePiece(tempPosMove[0], tempPosMove[1], locI, locJ);
                //only add the taken piece back if there was a piece on the board at that spot
                if (origPiece != null) {
                    if (isWhite) {
                        blackPieces.add(origPiece);
                    } else {
                        whitePieces.add(origPiece);
                    }
                }
                board[tempPosMove[0]][tempPosMove[1]] = origPiece;
                if (works) {
                    allPosMoves.add(locI + "," + locJ + "->" + tempPosMove[0] + "," + tempPosMove[1]);
                }
            }
        }
        return allPosMoves;
    }

    public void printAllPossibleMoves(boolean isWhite) {
        ArrayList<String> allPosMoves = getAllPossibleMoves(isWhite);
        System.out.println("Possible Moves: (" + allPosMoves.size() + ")");
        if (allPosMoves.size() == 0) {
            System.out.println("N/A");
            return;
        }
        for (String allPosMove : allPosMoves) {
            System.out.println(allPosMove);
        }
    }

    //Gets the possible moves for a piece - only the ones that don't result in the king being in check
    public ArrayList<Integer[]> getPosMoves4PieceAtLoc(int locI, int locJ) {
        ArrayList<String> allPosMoves = getAllPossibleMoves(board[locI][locJ].isWhite());
        ArrayList<Integer[]> posMovesForPiece = new ArrayList<>();
        for (String move : allPosMoves) {
            int initI = Integer.parseInt(move.substring(0, 1));
            int initJ = Integer.parseInt(move.substring(2, 3));
            if (initI == locI && initJ == locJ) {
                int finI = Integer.parseInt(move.substring(5, 6));
                int finJ = Integer.parseInt(move.substring(7, 8));
                posMovesForPiece.add(new Integer[]{finI, finJ});
            }
        }
        return posMovesForPiece;
    }

    //Determines if castling can occur on the king side
    public boolean canCastleKingSide(boolean isWhite) {
        int rowNum = (isWhite) ? 7 : 0;

        //if the king or rook has moved, castling isn't allowed
        if (isWhite) {
            if (wKingHasMoved || wRookRHasMoved) {
                return false;
            }
        } else {
            if (bKingHasMoved || bRookRHasMoved) {
                return false;
            }
        }

        if (board[rowNum][4] == null || board[rowNum][7] == null || board[rowNum][7].isWhite() != isWhite) {
            return false;
        }

        if (board[rowNum][5] != null || board[rowNum][6] != null) {
            return false;
        }

        if (isInCheck(isWhite)) {
            return false;
        }

        if (!board[rowNum][4].isKingMovePossible(this, rowNum, 4, rowNum, 5)) {
            return false;
        }
        if (!board[rowNum][4].isKingMovePossible(this, rowNum, 4, rowNum, 6)) {
            return false;
        }
        return true;
    }

    public boolean canCastleQueenSide(boolean isWhite) {
        int rowNum = (isWhite) ? 7 : 0;

        if (isWhite) {
            if (wKingHasMoved || wRookLHasMoved) {
                return false;
            }
        } else {
            if (bKingHasMoved || bRookLHasMoved) {
                return false;
            }
        }

        if (board[rowNum][4] == null || board[rowNum][0] == null || board[rowNum][0].isWhite() != isWhite) {
            return false;
        }

        if (board[rowNum][1] != null || board[rowNum][2] != null || board[rowNum][3] != null) {
            return false;
        }

        if (isInCheck(isWhite)) {
            return false;
        }

        if (!board[rowNum][4].isKingMovePossible(this, rowNum, 4, rowNum, 3)) {
            return false;
        }
        if (!board[rowNum][4].isKingMovePossible(this, rowNum, 4, rowNum, 2)) {
            return false;
        }
        return true;
    }

    public double getWhiteVal(int moveCounter) {
        double val = 0;
        for (ChessPiece cp: whitePieces) {
            if (isEarlyGame(moveCounter)) { //Early Game
                val += cp.getNumValEarly(board);
            } else if (isMidGame(moveCounter)) { //Mid Game
                val += cp.getNumValMid(board);
            } else { //End Game
                val += cp.getNumValEnd(board);
            }
        }
        return val;
    }
    public double getBlackVal(int moveCounter) {
        double val = 0;
        for (ChessPiece cp: blackPieces) {
            if (isEarlyGame(moveCounter)) { //Early Game
                val += cp.getNumValEarly(board);
            } else if (isMidGame(moveCounter)) { //Mid Game
                val += cp.getNumValMid(board);
            } else { //End Game
                val += cp.getNumValEnd(board);
            }
        }
        return val;
    }
    //For early game; adjust for mid and end game
    public double getNumWhiteOver1() {
        double total = 0;
        for (ChessPiece cp: whitePieces) {
            if (cp.getNumValEarly(board) > 1) {
                total+=cp.getNumValEarly(board);
            }
        }
        return total;
    }
    public double getNumBlackOver1() {
        double total = 0;
        for (ChessPiece cp: blackPieces) {
            if (cp.getNumValEarly(board) > 1) {
                total+=cp.getNumValEarly(board);
            }
        }
        return total;
    }

    private void initializeBoard() {
        for (ChessPiece[] chessPieces : board) { //initializes all tiles to null
            Arrays.fill(chessPieces, null);
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

    private void initializeWhiteBlackLists() {
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

    //For debugging purposes: for this to be more useful, have the toString() of the ChessPiece return the unicodeVal
    public String toString() {
        StringBuilder retStr = new StringBuilder(); //more efficient than using regular Strings in a loop because Strings are immutable
        for (ChessPiece[] chessPieces : board) {
            retStr.append("---------------------------------\n");
            for (ChessPiece chessPiece : chessPieces) {
                retStr.append("| ").append((chessPiece != null) ? chessPiece : " ").append(" ");
            }
            retStr.append("|\n");
        }
        retStr.append("---------------------------------\n");
        return retStr.toString();
    }

    public boolean isEarlyGame(int moveCounter) {
        return moveCounter < 18 && (whitePieces.size() + blackPieces.size() > 27);
    }
    public boolean isMidGame(int moveCounter) {
        return (moveCounter < 40 || (whitePieces.size() > 7 && blackPieces.size() > 7)) && moveCounter < 55;
    }
}
