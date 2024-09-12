package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    // Create set ups fo we can create the things later
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;

    //--------------------- CONSTRUCTOR ---------------------
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Create a move set for each peice?

        //Check the board for each move to see if the piece can move there with the position

        //


        throw new RuntimeException("Not implemented");
    }

    //Overide the to string funciton
    @Override
    public String toString() {
        return "ChessPiece{" +
                "type='" + type + '\'' +
                ", color='" + color + '\'' +
                '}'; //Can I make this be the actual color laters?
    }
}
