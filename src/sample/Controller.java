package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private final String directoryPath = "file:///Users/akash/software/Akash/JavaFX Projects/Chess-AI/src/sample/ChessPieces/";
    @FXML
    private GridPane boardContainer;
    private final HashMap<String, Image> pieceNameToImg = new HashMap<>();

    @FXML
    protected void initialize() {
        //Making it easier to access the chess piece images
        for (Piece piece : Piece.values()) { //iterates over the values of the enum
            pieceNameToImg.put(piece + "_white", new Image(directoryPath + piece + "_white.png"));
            pieceNameToImg.put(piece + "_black", new Image(directoryPath + piece + "_black.png"));
        }
        pieceNameToImg.put("empty", new Image(directoryPath + "empty.png"));

        //Set up Board UI Constraints
        ColumnConstraints c = new ColumnConstraints();
        c.setHalignment(HPos.CENTER);
        c.setMaxWidth(70);
        c.setMinWidth(70);
        c.setPrefWidth(70);
        RowConstraints r = new RowConstraints();
        r.setValignment(VPos.CENTER);
        r.setMaxHeight(70);
        r.setMinHeight(70);
        r.setPrefHeight(70);

        for (int i = 0; i < boardClass.board.length; i++) {
            boardContainer.getColumnConstraints().add(c);
            boardContainer.getRowConstraints().add(r);
        }
        //Initializes the board to the default configurations
        drawBoard();
        //Starting the game
        startGame();
        new AnimationTimer() { //probably don't use animation timer
            public void handle(long currentNanoTime) {
                if (!currPlayerIsWhite) {
                    moveCounter++;
                    boardClass.printAllPossibleMoves(currPlayerIsWhite);
                    String gamePhase = "";
                    if (boardClass.isEarlyGame(moveCounter)) {
                        gamePhase = " (Early Game)";
                    } else if (boardClass.isMidGame(moveCounter)) {
                        gamePhase = " (Mid Game)";
                    } else {
                        gamePhase = " (End Game)";
                    }
//                callAI(gamePhase, boardClass, Math.max(whiteDepth, blackDepth) == blackDepth, blackDepth, "Black");
                    game.computerMove(boardClass);
                    drawBoard();
                    //Checking if game is over
//                boolean isCheckmate = boardClass.isCheckMate(!currPlayerIsWhite);
//                boolean isTie = false;
//                if (!isCheckmate) {
//                    isTie = boardClass.isTie(!currPlayerIsWhite, boardFreq);
//                }
//                if (isCheckmate) {
//                    System.out.println((currPlayerIsWhite) ? "White Wins!" : "Black Wins!");
//                } else if (isTie) {
//                    System.out.println("Tie game!");
//                }
//                if (isCheckmate || isTie) {
//                    System.out.println("Finished Game Board:");
//                    this.stop();
//                }
                currPlayerIsWhite = !currPlayerIsWhite;
                }
            }
        }.start();
    }

    public void drawBoard() {
        int[] selectedLocArr = boardClass.getSelectedLoc(); //default {-1,-1} -> none selected
        int selectedI = selectedLocArr[0];
        int selectedJ = selectedLocArr[1];
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        if (selectedI != -1) {
            posMoves = boardClass.getPosMoves4PieceAtLoc(selectedI, selectedJ);
        }

        for (int i = 0; i < boardClass.board.length; i++) {
            for (int j = 0; j < boardClass.board[i].length; j++) {
                //Background
                VBox tempCont = new VBox();
                tempCont.setAlignment(Pos.CENTER);
                tempCont.prefHeight(70);
                tempCont.prefWidth(70);

                //Determines what color the tile background will be
                String bgHexStr = "";
                boolean shouldHighlight = shouldHighlight(i, j, posMoves);
                if (shouldHighlight) {
                    bgHexStr = "FFFF00";
                } else {
                    if ((i + j) % 2 == 0) { //cross pattern background
                        bgHexStr = "EEEED2";
                    } else {
                        bgHexStr = "769657";
                    }
                }
                tempCont.setStyle("-fx-background-color: #" + bgHexStr + ";");

                //Get Image to place in Box
                String imgName = "empty";
                if (boardClass.board[i][j] != null) { //if the box has a piece in it
                    imgName = boardClass.board[i][j].getImageName();
                }
                ImageView tempIW = new ImageView(pieceNameToImg.get(imgName));
                tempIW.setFitHeight(45);
                tempIW.setFitWidth(45);
                tempIW.setPickOnBounds(true);
                tempIW.setPreserveRatio(true);

                tempCont.getChildren().add(tempIW);

                //needed because values in the event handler must be of type final
                final int iTemp = i;
                final int jTemp = j;
                final boolean shouldHighlightTemp = shouldHighlight;
                //Event Handler for clicking on a tile
                tempCont.setOnMouseClicked(e -> {
                    if (currPlayerIsWhite && !(selectedI == -1 && (boardClass.board[iTemp][jTemp] != null && !boardClass.board[iTemp][jTemp].isWhite()))) {
                        clickedLocationHandler(iTemp, jTemp, shouldHighlightTemp);
                    }
                });
                boardContainer.add(tempCont, j, i);
            }
        }
    }

    //Determines what happens when the user selects a tile
    public void clickedLocationHandler(int i, int j, boolean isHighlighted) {
        int[] selectedLocArr = boardClass.getSelectedLoc();
        int selectedI = selectedLocArr[0];
        int selectedJ = selectedLocArr[1];
        //if nothing is currently selected or the highlighted tile is selected or the selected tile isn't already highlighted (aka. isn't a valid move)
        if (selectedI == -1 || (selectedI == i && selectedJ == j) || !isHighlighted) {
            if (boardClass.board[i][j] != null) { //can only select a tile that isn't blank
                boardClass.toggleSelectedLoc(i, j);
            } else {
                boardClass.toggleSelectedLoc(-1, -1);
            }
        } else { //if a tile selected is a possible move (it will move to the new position)
            game.userMove(boardClass, selectedI, selectedJ, i, j, boardFreq);
            boardClass.toggleSelectedLoc(-1, -1);
            currPlayerIsWhite = !currPlayerIsWhite;
        }

        drawBoard();

    }

    //Condition, given the selected pieces, determines if the curr location should be highlighted
    //Show the overall possible moves (eliminates some of the other options when it would result in it still being check)
    public boolean shouldHighlight(int currI, int currJ, ArrayList<Integer[]> posMoves) { //posMoves is the list of possible moves from the selectedI and selectedJ
        int[] selectedLocArr = boardClass.getSelectedLoc();
        int selectedI = selectedLocArr[0];
        int selectedJ = selectedLocArr[1];
        //No tile is selected
        if (selectedI == -1) { //if either that or selectedJ = -1, it's false
            return false;
        }
        //Highlight the tile that is pressed
        if (currI == selectedI && currJ == selectedJ) {
            return true;
        }
        //Check to see if the current tile is a possible move from the selected tile and highlight it if it is
        for (Integer[] location : posMoves) {
            if (currI == location[0] && currJ == location[1]) {
                return true;
            }
        }
        return false;
    }

    //--------Chess Game Related Stuff--------\\

    public ChessGame game = new ChessGame();
    public BoardClass boardClass = new BoardClass();
    private int moveCounter = 0;
    private final HashMap<String, Integer> boardFreq = new HashMap<>(); // Checks for 3 fold rule (tie)
    private final ArrayList<Double[]> depthValToSkip = new ArrayList<>(); // 2,3,4,5...
    private boolean currPlayerIsWhite = true;

    public void startGame() { // Driver
        //Will deal with these values later
        depthValToSkip.add(new Double[]{ 2.0, 2.2, 2.4, 2.7, 3.0, 3.4, 4.0, 4.5 }); //depth2valToSkip
        depthValToSkip.add(new Double[]{ 2.0, 2.2, 2.4, 2.8, 3.3, 3.8, 4.8, 5.5 }); //depth3valToSkip
        depthValToSkip.add(new Double[]{ 2.0, 2.2, 2.4, 2.8, 3.3, 3.8, 4.8, 6.0 }); //depth4valToSkip
        depthValToSkip.add(new Double[]{ 2.5, 2.8, 3.0, 3.5, 3.9, 4.5, 5.4, 6.8 }); //depth5valToSkip
        depthValToSkip.add(new Double[]{ 3.0, 3.8, 4.4, 5.1, 5.8, 6.3, 7.4, 8.5 }); //depth6valToSkip
        System.out.print("\033[H\033[2J"); // Clear Console Command
        System.out.println("Welcome to My Chess Game!");
//        playGame(4);
    }

    //Game Loop
    public void playGame(int aiDepth) {

        moveCounter++;
//        boardClass.printAllPossibleMoves(currPlayerIsWhite);
        String gamePhase = "";
        if (boardClass.isEarlyGame(moveCounter)) {
            gamePhase = " (Early Game)";
        } else if (boardClass.isMidGame(moveCounter)) {
            gamePhase = " (Mid Game)";
        } else {
            gamePhase = " (End Game)";
        }
        if (!currPlayerIsWhite) {
            while(currPlayerIsWhite) {
                //waits for user to do turn
            }
        } else {
//                callAI(gamePhase, boardClass, Math.max(whiteDepth, blackDepth) == blackDepth, blackDepth, "Black");
            game.computerMove(boardClass);
            drawBoard();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Checking if game is over
        boolean isCheckmate = boardClass.isCheckMate(!currPlayerIsWhite);
        boolean isTie = false;
        if (!isCheckmate) {
            isTie = boardClass.isTie(!currPlayerIsWhite, boardFreq);
        }
        if (isCheckmate) {
            System.out.println((currPlayerIsWhite) ? "White Wins!" : "Black Wins!");
        } else if (isTie) {
            System.out.println("Tie game!");
        }
        if (isCheckmate || isTie) {
            System.out.println("Finished Game Board:");
            return;
        }
        currPlayerIsWhite = !currPlayerIsWhite;
    }
}
