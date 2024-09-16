package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    //Create the variables
    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        //Initialize the board
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
        //throw new RuntimeException("Not implemented");
    }

    @Override
    public String toString() {
        if (promotionPiece == null) {
            return "Chess move: " + startPosition.toString() + " - " + endPosition.toString();

        } else {
            return "Chess move: " + startPosition.toString() + " - " + endPosition.toString() + " - " + promotionPiece.toString();
        }
    }

    @Override
    public int hashCode() {
        if (promotionPiece == null) {
            return 31 * (startPosition.hashCode() + endPosition.hashCode() + 101);
        } else {
            return 31 * (startPosition.hashCode() + endPosition.hashCode() + promotionPiece.hashCode());
        }
    }

    @Override
    public boolean equals(Object o) {
        //Determine if memory is the same
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove that = (ChessMove) o;

        //If all of these equal they are the same
        if (this.promotionPiece == null) {
            //Both will have to be false
            if (that.promotionPiece != null) {
                return false;
            }

            //Compare start and end positions
            return this.startPosition.equals(that.startPosition) && this.endPosition.equals(that.endPosition);

        }

        //Compare all the the positions
        return this.startPosition.equals(that.startPosition) && this.endPosition.equals(that.endPosition) && this.promotionPiece.equals(that.promotionPiece);
    }
}
