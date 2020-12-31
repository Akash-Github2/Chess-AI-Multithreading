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

        drawBoard();
    }

    public void drawBoard() {
        for (int i = 0; i < boardClass.board.length; i++) {
            for (int j = 0; j < boardClass.board[i].length; j++) {
                //Background
                VBox tempCont = new VBox();
                tempCont.setAlignment(Pos.CENTER);
                tempCont.prefHeight(70);
                tempCont.prefWidth(70);
                if ((i + j) % 2 == 0) { //cross pattern background
                    tempCont.setStyle("-fx-background-color: #EEEED2;");
                } else {
                    tempCont.setStyle("-fx-background-color: #769657;");
                }

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
                boardContainer.add(tempCont, j, i);
            }
        }
    }

}
