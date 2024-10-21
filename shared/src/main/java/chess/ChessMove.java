package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition start;
    private ChessPosition end;
    private ChessPiece.PieceType type;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        this.start = startPosition;
        this.end = endPosition;
        this.type = promotionPiece;
    }


    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        //Idex on one
        return start;

        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    //Add Overides
    @Override
    public String toString() {
        if (type == null) {
            return getStartPosition().toString() + " to " + getEndPosition().toString();
        } else {
            return getStartPosition().toString() + " to " + getEndPosition().toString() + " prom: " + type;
        }
    }


    //Overide
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove that = (ChessMove) o;
        return that.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return 31 * this.toString().hashCode();
    }

}
