package sample;

public abstract class ChessPiece {
    protected String name; //pawn, knight, bishop, rook, queen, king
    protected double numVal; //1, 3, 3, 5, 10, 100
    protected int[] location = new int[2]; // "ij" - use substring() to get i and j

    protected ChessPiece(String name, double numVal, int[] location) {
        this.name = name;
        this.numVal = numVal;
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    public String getName() {
        return name;
    }
    public double getNumVal() {
        return numVal;
    }
    public int[] getLocation() {
        return location;
    }
    public void setLocation(int[] location) {
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    protected abstract String getImageName();
    protected abstract boolean isWhite();
}
