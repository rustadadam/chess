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
    //Create the object to return later, that we can append pieces too
    private Collection<ChessMove> availableMoves;

    //--------------------- CONSTRUCTOR ---------------------
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        this.availableMoves = new ArrayList<>();
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

    //Pawn promotion funciton
    private void check_promotion(ChessPosition myPosition, ChessPosition newMove) {
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Create a move set for each peice?

        //Move Pawn. Check if the piece is a pawn
        if (type == ChessPiece.PieceType.PAWN) {
            //Check color
            if (color == ChessGame.TeamColor.WHITE) {

                //Check to see if can move forward
                if (moveCheck(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn()) == "Empty") {
                    //If true, can move forward
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    //This adds the moves to the dictionary
                    check_promotion(myPosition, newMove);
                }

                //Check to see if it can take diagonally
                if (moveCheck(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() + 1) == "Occupied_Opp") {

                    //Create new position
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    //This adds the moves to the dictionary
                    check_promotion(myPosition, newMove);
                }

                //Check to see if it can take diagonally on the other side
                if (moveCheck(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() - 1) == "Occupied_Opp") {

                    //Create new position
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

                    //This adds the moves to the dictionary
                    check_promotion(myPosition, newMove);
                }


            } else {
                //Color is black
                //Check to see if can move forward
                if (moveCheck(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn()) == "Empty") {
                    //If true, can move forward
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());

                    //This adds the moves to the dictionary
                    check_promotion(myPosition, newMove);
                }

                //Check to see if it can take diagonally
                if (moveCheck(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() + 1) == "Occupied_Opp") {

                    //Create new position
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());

                    //This adds the moves to the dictionary
                    check_promotion(myPosition, newMove);
                }

                //Check to see if it can take diagonally on the other side
                if (moveCheck(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() - 1) == "Occupied_Opp") {

                    //Create new position
                    ChessPosition newMove = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());

                    //This adds the moves to the dictionary
                    check_promotion(myPosition, newMove);
                }
            }
        }

        if (type == ChessPiece.PieceType.ROOK) {
            //Move right
            int row_move = myPosition.getRow() + 1;
            String move_string = moveCheck(board, myPosition, row_move, myPosition.getColumn());
            while (move_string != "Out" && move_string != "Occupied_Team") {

                //Add move
                ChessPosition newMove = new ChessPosition(row_move, myPosition.getColumn());
                ChessMove move = new ChessMove(myPosition, newMove, null);
                availableMoves.add(move);

                if (move_string == "Occupied_Opp") {
                    //Stop the loop.
                    move_string = "Out";
                } else {
                    //Index row_move
                    row_move++;
                    move_string = moveCheck(board, myPosition, row_move, myPosition.getColumn());
                }

            }

            //Move Left
            row_move = myPosition.getRow() - 1;
            move_string = moveCheck(board, myPosition, row_move, myPosition.getColumn());
            while (move_string != "Out" && move_string != "Occupied_Team") {

                //Add move
                ChessPosition newMove = new ChessPosition(row_move, myPosition.getColumn());
                ChessMove move = new ChessMove(myPosition, newMove, null);
                availableMoves.add(move);

                if (move_string == "Occupied_Opp") {
                    //Stop the loop.
                    move_string = "Out";
                } else {
                    //Index row_move
                    row_move--;
                    move_string = moveCheck(board, myPosition, row_move, myPosition.getColumn());
                }

            }

            //Move up
            int col_move = myPosition.getColumn() + 1;
            move_string = moveCheck(board, myPosition, myPosition.getRow(), col_move);
            while (move_string != "Out" && move_string != "Occupied_Team") {

                //Add move
                ChessPosition newMove = new ChessPosition(row_move, myPosition.getColumn());
                ChessMove move = new ChessMove(myPosition, newMove, null);
                availableMoves.add(move);

                if (move_string == "Occupied_Opp") {
                    //Stop the loop.
                    move_string = "Out";
                } else {
                    //Index row_move
                    col_move++;
                    move_string = moveCheck(board, myPosition, col_move, myPosition.getColumn());
                }

            }

            //Move down
            col_move = myPosition.getColumn() - 1;
            move_string = moveCheck(board, myPosition, myPosition.getRow(), col_move);
            while (move_string != "Out" && move_string != "Occupied_Team") {

                //Add move
                ChessPosition newMove = new ChessPosition(row_move, myPosition.getColumn());
                ChessMove move = new ChessMove(myPosition, newMove, null);
                availableMoves.add(move);

                if (move_string == "Occupied_Opp") {
                    //Stop the loop.
                    move_string = "Out";
                } else {
                    //Index row_move
                    col_move--;
                    move_string = moveCheck(board, myPosition, col_move, myPosition.getColumn());
                }

            }

        }

        if (type == ChessPiece.PieceType.KING) {
            //Code the kings moves
        }


        // return Collection<ChessMove>
        return availableMoves;
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
