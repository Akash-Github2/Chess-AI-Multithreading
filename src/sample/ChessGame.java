package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ChessGame {

    //User does a move
    public void userMove(BoardClass boardClass, int initI, int initJ, int finI, int finJ, HashMap<String, Integer> boardFreq) {

        int totalPieces = boardClass.whitePieces.size() + boardClass.blackPieces.size();
        boolean mightIncrNoCapture = false;
        if (boardClass.board[initI][initJ].getPiece() != Piece.PAWN) {
            mightIncrNoCapture = true;
        }
        boardClass.makeMoveOverall(initI, initJ, finI, finJ, false); //Make Final Move
        if (boardClass.whitePieces.size() + boardClass.blackPieces.size() == totalPieces && mightIncrNoCapture) {
            boardClass.movesSinceNoCaptureOrPawn++;
        } else {
            boardClass.movesSinceNoCaptureOrPawn = 0;
        }
        //Use Unicode Values for this in the toString() - not big deal for now
        if (boardFreq.get(boardClass.toString()) == null) {
            boardFreq.put(boardClass.toString(), 1);
        } else {
            boardFreq.put(boardClass.toString(), boardFreq.get(boardClass.toString()) + 1);
        }
    }

    //Temporary - randomly chooses move
    public void computerMove(BoardClass boardClass) {
        ArrayList<String> allPosMoves = boardClass.getAllPossibleMoves(false);
        String move = getRandomMove(allPosMoves);
        int initI = Integer.parseInt(move.substring(0, 1));
        int initJ = Integer.parseInt(move.substring(2, 3));
        int finI = Integer.parseInt(move.substring(5, 6));
        int finJ = Integer.parseInt(move.substring(7, 8));
        boardClass.makeMoveOverall(initI, initJ, finI, finJ, false); //Make Final Move
    }
    public String getRandomMove(ArrayList<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
