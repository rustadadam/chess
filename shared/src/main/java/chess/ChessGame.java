package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    boolean is_white_turn;
    ChessBoard board;

    public ChessGame() {
        this.is_white_turn = true;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (is_white_turn) {
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        is_white_turn = team == TeamColor.WHITE;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        //Get the peice
        ChessPiece piece = board.getPiece(startPosition);

        //Return its moves
        return piece.availableMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        //Check Valid moves
        Collection<ChessMove> possible_moves = validMoves(move.getStartPosition());

        //Check to see if move is in possible_moves
        if (!possible_moves.contains(move)) { //Could see if it puts the king in check

            //Return error
            throw new InvalidMoveException();

        }

        //Make the move

        //Get the piece
        ChessPiece piece = board.getPiece(move.getStartPosition());

        //Remove piece at location
        board.removePiece(move.getStartPosition());

        //Add the piece at the new location
        board.addPiece(move.getEndPosition(), piece);

        //Change whose turn it is
        is_white_turn = !is_white_turn;


        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }

    // Override functions
    @Override
    public String toString() {
        return board.toString() + "\n Turn: " + getTeamTurn();
    }

    //Overide
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame that = (ChessGame) o;
        return that.toString().equals(this.toString());
    }

    @Override
    public int hashCode(){
        return 31 * this.toString().hashCode();
    }
}
}
