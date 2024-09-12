package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    //Add Rows and col
    private int row;
    private int col;

    // ------- CONSTRUCTOR _-------
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    //Overide the to string funciton
    @Override
    public String toString() {
        return "ChessPosition:" + row + "," + col;
    }

    //Overide the to equal function
    @Override
    public boolean equals(Object o) {
        //Equals in memory
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;

        //Check to see if the positions match
        return row == that.row && col == that.col;
    }

    //Overide the hash Fucntion
    @Override
    public int hashCode() {
        return 31 * row + col;
    }


}
