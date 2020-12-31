package sample;

public abstract class ChessPiece {
    protected String name; //pawn, knight, bishop, rook, queen, king
    protected double numVal; //1, 3, 3, 5, 10, 100
    protected String location; // "ij" - use substring() to get i and j

    protected ChessPiece(String name, double numVal, String location) {
        this.name = name;
        this.numVal = numVal;
        this.location = location;
    }

    public String getName() {
        return name;
    }
    public double getNumVal() {
        return numVal;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    protected abstract String getImageName();
    protected abstract boolean isWhite();
}
