package sample;

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

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private String directoryPath = "file:///Users/akash/software/Akash/JavaFX Projects/Chess-AI/src/sample/ChessPieces/";
    @FXML
    private GridPane boardContainer;

    private HashMap<String, Image> pieceNameToImg = new HashMap<>();
    private final String[] diffPiecesNoColor = new String[]{"king", "queen", "rook", "knight", "bishop", "pawn"};
    public BoardClass boardClass = new BoardClass();

    @FXML
    protected void initialize() {
        //Making it easier to access the chess piece images
        for (int i = 0; i < diffPiecesNoColor.length; i++) {
            pieceNameToImg.put(diffPiecesNoColor[i] + "_white", new Image(directoryPath + diffPiecesNoColor[i] + "_white.png"));
            pieceNameToImg.put(diffPiecesNoColor[i] + "_black", new Image(directoryPath + diffPiecesNoColor[i] + "_black.png"));
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
    }

    public void drawBoard() {
        int[] selectedLocArr = boardClass.getSelectedLoc(); //default {-1,-1} -> none selected
        int selectedI = selectedLocArr[0];
        int selectedJ = selectedLocArr[1];
        ArrayList<Integer[]> posMoves = new ArrayList<>();
        if (selectedI != -1) {
            posMoves = boardClass.board[selectedI][selectedJ].getPossibleMoves(boardClass);
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
                    clickedLocationHandler(iTemp, jTemp, shouldHighlightTemp);
                });
                boardContainer.add(tempCont, j, i);
            }
        }
    }

    //Determines what happens when the user selects a tile
    public void clickedLocationHandler(int i, int j, boolean isHighlighted) {
        int selectedI = boardClass.getSelectedLoc()[0];
        int selectedJ = boardClass.getSelectedLoc()[1];
        //if nothing is currently selected or the highlighted tile is selected or the selected tile isn't already highlighted (aka. isn't a valid move)
        if (selectedI == -1 || (selectedI == i && selectedJ == j) || !isHighlighted) {
            if (boardClass.board[i][j] != null) { //can only select a tile that isn't blank
                boardClass.toggleSelectedLoc(i, j);
            } else {
                boardClass.toggleSelectedLoc(-1, -1);
            }
        } else { //if a tile selected is a possible move (it will move to the new position)
            boardClass.movePiece(new int[]{selectedI, selectedJ}, new int[]{i,j});
            boardClass.toggleSelectedLoc(-1, -1);
        }

        drawBoard();
    }

    //Condition, given the selected pieces, determines if the curr location should be highlighted
    //Now, it's only selecting if it's the selected tile or the one in front of it
    //Will change to selecting all the tiles that are possible moves
    public boolean shouldHighlight(int currI, int currJ, ArrayList<Integer[]> posMoves) { //posMoves is the list of possible moves from the selectedI and selectedJ
        //No tile is selected
        int selectedI = boardClass.getSelectedLoc()[0];
        int selectedJ = boardClass.getSelectedLoc()[1];
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

}
