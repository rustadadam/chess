package chess;

import java.util.ArrayList;
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

    //Function to check moves.
    private String moveCheck(ChessBoard board, ChessPosition myPosition, int move_row, int move_col) {

        //Add the row and col to pos
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move_row, myPosition.getColumn() + move_col);

        //Check it the newPosition is fair
        //Check to make sure its on the board
        if (newPosition.getRow() > 7 || newPosition.getRow() < 0) {
            return "Out";
        }

        if (newPosition.getColumn() > 7 || newPosition.getColumn() < 0) {
            return "Out";
        }

        if (board.getPiece(newPosition) == null) {
            //No piece occupies there
            return "Empty";

        } else if (board.getPiece(newPosition).getTeamColor() != color) {
            //Opponent Piece occupies there
            return "Occupied_Opp";
        } else {
            //Ally Piece occupies there
            return "Occupied_Team";
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Create the object to return later, that we can append pieces too
        Collection<ChessMove> availableMoves = new ArrayList<>();

        //Create a move set for each peice?

        //Move Pawn. Check if the piece is a pawn
        if (type == ChessPiece.PieceType.PAWN) {

            if (color == ChessGame.TeamColor.WHITE) {

                //Check to see if can move forward
                if (moveCheck(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn()) == "Empty") {
                    //If true, can move forward
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    //Check if the pawn if the pawn gets promoted
                    if (newMove.getRow() == 0) {
                        //Pawn is promoted
                        for (PieceType type : PieceType.values()) {
                            ChessMove move = new ChessMove(myPosition, newMove, type);

                            //Add the move to the dictionary
                            availableMoves.add(move);
                        }
                    } else {

                        //Pawn is not promoted
                        ChessMove move = new ChessMove(myPosition, newMove, null);

                        //Add the move to the dictionary
                        availableMoves.add(move);
                    }
                }

                //Check to see if it can take diagonally
                if (moveCheck(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() + 1) == "Occupied_Opp") {
                    //Piece is occupied by Opp

                    //Create new position
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    //RETURN AT THE LINE ABOVE TODO
                }


            } else { //Color will be Black

            }

        }

        //Check the board for each move to see if the piece can move there with the position

        //

        // return Collection<ChessMove>
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
