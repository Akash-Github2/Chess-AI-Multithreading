package sample;

public class BlackPiece extends ChessPiece {
    public BlackPiece(String name, double numVal, int i, int j) {
        super(name, numVal, i + "" + j);
    }
    public String getImageName() {
        return name + "_black";
    }
    public boolean isWhite() {
        return false;
    }
}
