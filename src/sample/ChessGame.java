package sample;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChessGame {

    public int moveCounter = 0;
    public final ArrayList<Double[]> depthValToSkip = new ArrayList<>(); // 2,3,4,5...

    public void startGame() { // Assigns initial data
        //Will deal with these values later
        depthValToSkip.add(new Double[]{ 2.0, 2.2, 2.4, 2.7, 3.0, 3.4, 4.0, 4.5 }); //depth2valToSkip
        depthValToSkip.add(new Double[]{ 2.0, 2.2, 2.4, 2.8, 3.3, 3.8, 4.8, 5.5 }); //depth3valToSkip
        depthValToSkip.add(new Double[]{ 2.0, 2.2, 2.4, 2.8, 3.3, 3.8, 4.8, 6.0 }); //depth4valToSkip
        depthValToSkip.add(new Double[]{ 2.5, 2.8, 3.0, 3.5, 3.9, 4.5, 5.4, 6.8 }); //depth5valToSkip
        depthValToSkip.add(new Double[]{ 3.0, 3.8, 4.4, 5.1, 5.8, 6.3, 7.4, 8.5 }); //depth6valToSkip
        System.out.print("\033[H\033[2J"); // Clear Console Command
        System.out.println("Welcome to My Chess Game!");
    }

    //User does a move
    public void userMove(BoardClass boardClass, int initI, int initJ, int finI, int finJ) {

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
        if (boardClass.boardFreq.get(boardClass.toString()) == null) {
            boardClass.boardFreq.put(boardClass.toString(), 1);
        } else {
            boardClass.boardFreq.put(boardClass.toString(), boardClass.boardFreq.get(boardClass.toString()) + 1);
        }
    }

    //Temporary - randomly chooses move (replace with AI choosing the move) - make it so the AI can be black or white
//    public void computerMove(BoardClass boardClass, boolean isWhite) {
//        ArrayList<String> allPosMoves = boardClass.getAllPossibleMoves(isWhite);
//        String move = getRandomMove(allPosMoves);
//        int initI = Integer.parseInt(move.substring(0, 1));
//        int initJ = Integer.parseInt(move.substring(2, 3));
//        int finI = Integer.parseInt(move.substring(5, 6));
//        int finJ = Integer.parseInt(move.substring(7, 8));
//        boardClass.makeMoveOverall(initI, initJ, finI, finJ, false); //Make Final Move
//    }
//    public String getRandomMove(ArrayList<String> list) {
//        Random rand = new Random();
//        return list.get(rand.nextInt(list.size()));
//    }

    //------Multithreading AI Section-------\\
    public List<Double[]> mainList = Collections.synchronizedList(new ArrayList<>()); //list of all moves and scores to determine which is the best
    public synchronized void addToList(double score, int initI, int initJ, int finI, int finJ) {
        mainList.add(new Double[]{score, (double) initI, (double) initJ, (double) finI, (double) finJ});
    }

    public void findBestMove(BoardClass boardClass, int maxDepth, boolean isComputerWhite) {
        mainList.clear();
        ArrayList<String> allPosMoves = boardClass.getAllPossibleMoves(isComputerWhite);
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(allPosMoves.size(), 10)); //each move route is done in a different thread
        for (String move : allPosMoves) {
            int initI = Integer.parseInt(move.substring(0, 1));
            int initJ = Integer.parseInt(move.substring(2, 3));
            int finI = Integer.parseInt(move.substring(5, 6));
            int finJ = Integer.parseInt(move.substring(7, 8));
            executor.execute(new AIThread(boardClass, initI, initJ, finI, finJ, maxDepth, isComputerWhite));
        }
        while (mainList.size() != allPosMoves.size()) { //waits for the all threads to finish

        }
        double maxScore = -2000; //Lower than any possible score could be
        String bestMove;
        List<String> optMoves = new ArrayList<>();
        for (Double[] arr : mainList) {
            if (arr[0] > maxScore) {
                maxScore = arr[0];
                optMoves.clear();
                optMoves.add(arr[1].intValue() + "," + arr[2].intValue() + "->" + arr[3].intValue() + "," + arr[4].intValue());
            } else if (arr[0] == maxScore) {
                optMoves.add(arr[1].intValue() + "," + arr[2].intValue() + "->" + arr[3].intValue() + "," + arr[4].intValue());
            }
        }

        int randInd = (int) (Math.random() * optMoves.size());
        bestMove = optMoves.get(randInd);
        int initI = Integer.parseInt(bestMove.substring(0, 1));
        int initJ = Integer.parseInt(bestMove.substring(2, 3));
        int finI = Integer.parseInt(bestMove.substring(5, 6));
        int finJ = Integer.parseInt(bestMove.substring(7, 8));
        userMove(boardClass, initI, initJ, finI, finJ); //make the final move

        executor.shutdown();
    }

    public class AIThread implements Runnable {
        private final BoardClass boardClass;
        private final int initI;
        private final int initJ;
        private final int finI;
        private final int finJ;
        private final int maxDepth;
        private final boolean isComputerWhite;
        public AIThread(BoardClass boardClass, int initI, int initJ, int finI, int finJ, int maxDepth, boolean isComputerWhite) {
            this.boardClass = new BoardClass(boardClass);
            this.initI = initI;
            this.initJ = initJ;
            this.finI = finI;
            this.finJ = finJ;
            this.maxDepth = maxDepth;
            this.isComputerWhite = isComputerWhite;
        }
        @Override
        public void run() {
            double origValDiff = boardClass.getBlackVal(moveCounter) - boardClass.getWhiteVal(moveCounter);
            if (isComputerWhite) {
                origValDiff *= -1;
            }
            double newValDiff = boardClass.getBlackVal(moveCounter+1) - boardClass.getWhiteVal(moveCounter+1);
            if (isComputerWhite) {
                origValDiff *= -1;
            }
            double diff = newValDiff - origValDiff;
            userMove(boardClass, initI, initJ, finI, finJ);
            double score = findBestMoveMain(boardClass, false, 1, maxDepth, moveCounter + 1, isComputerWhite) + diff;
            System.out.println(initI + "," + initJ + "->" + finI + "," + finJ + " - Score: " + Util.rounded(score));
            addToList(score, initI, initJ, finI, finJ);
        }
    }

    //This is the last part left, the multithreading section is done
    private double findBestMoveMain(BoardClass boardClass, boolean isComputerTurn, int depth, int maxDepth, int tempMoveCounter, boolean isComputerWhite) {
        boolean isWhite = true;
        if ((isComputerTurn && !isComputerWhite) || (!isComputerTurn && isComputerWhite)) {
            isWhite = false;
        }

        if (boardClass.isCheckMate(!isComputerWhite)) {
            return 1000 + (maxDepth - depth);
        } else if (boardClass.isCheckMate(isComputerWhite)) {
            return -1000 - (maxDepth - depth);
        } else if (boardClass.isTie(isWhite)) {
            //If the difference between white and black values isn't much, it's not a great move
            if (Math.abs(boardClass.getWhiteVal(tempMoveCounter) - boardClass.getBlackVal(tempMoveCounter)) < 2.0) {
                return -1;
            }
            //If computer is winning, tying the game isn't the best move
            if (isComputerWhite) {
                return (boardClass.getWhiteVal(tempMoveCounter) - boardClass.getBlackVal(tempMoveCounter)) * -1.5;
            } else {
                return (boardClass.getBlackVal(tempMoveCounter) - boardClass.getWhiteVal(tempMoveCounter)) * -1.5;
            }
        }
        //If computer turn, it will try to find the highest value, otherwise it will try to find the lowest value
        double optVal = (isComputerTurn) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        ArrayList<String> allPosMoves = boardClass.getAllPossibleMoves(isWhite);

        if (depth < maxDepth) {
            //Loops through all the possible moves to see which one is the best
            for (String move : allPosMoves) {
                int initI = Integer.parseInt(move.substring(0, 1));
                int initJ = Integer.parseInt(move.substring(2, 3));
                int finI = Integer.parseInt(move.substring(5, 6));
                int finJ = Integer.parseInt(move.substring(7, 8));
                //At the start of the game, the pawn should move first
                if (moveCounter <= 2 && boardClass.board[initI][initJ].getPiece() != Piece.PAWN) {
                    continue;
                }

                ChessPiece origPiece = boardClass.board[finI][finJ];
                double origValDiff = boardClass.getBlackVal(tempMoveCounter) - boardClass.getWhiteVal(tempMoveCounter);
                if (isComputerWhite) {
                    origValDiff *= -1;
                }

                boolean didPawnPromo = boardClass.makeMoveOverall(initI, initJ, finI, finJ, true); // Try Move

                //Adds current board to the boardFreq in case a tie occurs later
                String triedBoard = boardClass.toString();
                boardClass.boardFreq.merge(triedBoard, 1, Integer::sum);

                double newValDiff = boardClass.getBlackVal(tempMoveCounter) - boardClass.getWhiteVal(tempMoveCounter);
                if (isComputerWhite) {
                    newValDiff *= -1;
                }

                double diff = newValDiff - origValDiff; //change in difference values after the move was made

                double n = findBestMoveMain(boardClass, !isComputerTurn, depth + 1, maxDepth, tempMoveCounter + 1,
                        isComputerWhite) + diff;

                boolean shouldChange = n > optVal;
                if (!isComputerTurn) {
                    shouldChange = n < optVal;
                }
                if (shouldChange) {
                    optVal = n;
                }

                //reset it
                boardClass.fullyResetMove(initI, initJ, finI, finJ, !isWhite, didPawnPromo, origPiece);
                if (boardClass.boardFreq.get(triedBoard) == 1) {
                    boardClass.boardFreq.remove(triedBoard);
                } else {
                    boardClass.boardFreq.put(triedBoard, boardClass.boardFreq.get(triedBoard) - 1);
                }
            }
        } else {
            return 0;
        }

        return optVal;
    }
}
