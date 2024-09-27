package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    boolean is_white_turn;
    boolean white_in_check;
    boolean black_in_check;
    ChessBoard board;

    Collection<ChessMove> all_white_moves;
    Collection<ChessMove> all_black_moves;

    public ChessGame() {
        this.is_white_turn = true;

        //Create a chess board
        this.board = new ChessBoard();
        board.resetBoard();

        //Get all possible white and black moves
        all_white_moves = board.find_all_moves(TeamColor.WHITE);
        all_black_moves = board.find_all_moves(TeamColor.BLACK);

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

//    public Collection<ChessMove> find_all_moves(TeamColor color) {
//        Collection<ChessPiece> pieces = board.getPieces(color);
//
//        for (ChessPiece piece : pieces) {
//            piece.
//        }
//    }

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


    private void update_move_set(TeamColor color) {
        if (color == TeamColor.WHITE) {
            all_white_moves = board.find_all_moves(TeamColor.WHITE);
        } else {
            all_black_moves = board.find_all_moves(TeamColor.BLACK);
        }
    }

    private void update_opp_move_set(TeamColor color) {
        if (color != TeamColor.WHITE) {
            all_white_moves = board.find_all_moves(TeamColor.WHITE);
        } else {
            all_black_moves = board.find_all_moves(TeamColor.BLACK);
        }
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

        if (piece == null) {
            return null;
        }

        // get all possible moves
        Collection<ChessMove> all_moves = piece.pieceMoves(board, startPosition);

        //We need to check to see if the peice is blocking a check //We can do this by "pre-moving" the peice, and then
        //check to see if the king is in check

        //Set start position to null
        board.removePiece(startPosition);

        //Store these values
        Collection<ChessMove> temp_black_moves = all_black_moves;
        Collection<ChessMove> temp_white_moves = all_white_moves;

        // Update move set
        update_opp_move_set(piece.getTeamColor());

        //If not in check, we can return moves. MAJOR shortcut
        if (!isInCheck(piece.getTeamColor()) && piece.getPieceType() != ChessPiece.PieceType.KING) {
            board.addPiece(startPosition, piece);
            return all_moves;
        }

        // Possible moves
        Collection<ChessMove> valid_moves = new ArrayList<>();

        //Loop through each move
        for (ChessMove move : all_moves) {
            //If it takes a piece, we need to return it
            ChessPiece taken_piece = board.getPiece(move.getEndPosition());

            //Add the piece to its move
            board.addPiece(move.getEndPosition(), piece);

            // Update move set
            update_opp_move_set(piece.getTeamColor());

            //If not in check, we can add the move
            if (!isInCheck(piece.getTeamColor())) {
                valid_moves.add(move);
            }

            //readd the taken peice
            board.addPiece(move.getEndPosition(), taken_piece);

        }

        //Replace the piece
        board.addPiece(startPosition, piece);

        //update what we need too
        all_black_moves = temp_black_moves;
        all_white_moves = temp_white_moves;


        //Return its moves
        return valid_moves;
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

        //Check to see if there are possible_moves
        //Check to see if move is in possible_moves
        if (possible_moves == null) {

            //Return error
            throw new InvalidMoveException();

        }

        //Check to see if move is in possible_moves
        if (!possible_moves.contains(move)) {

            //Return error
            throw new InvalidMoveException();

        }

        //Get the piece
        ChessPiece piece = board.getPiece(move.getStartPosition());

        //Check to see if there is a piece there
        if (piece == null) {
            throw new InvalidMoveException();
        }

        //Check if its their turn
        if ((piece.getTeamColor() == TeamColor.WHITE) != is_white_turn) {

            //Return error
            throw new InvalidMoveException();

        }

        //Remove piece at location
        board.removePiece(move.getStartPosition());

        //Change piece to its promotion
        if (move.getPromotionPiece() != null) {
            piece.type = move.getPromotionPiece(); //High Coupling. BAD code design
        }

        //Add the piece at the new location
        board.addPiece(move.getEndPosition(), piece);

        //Change whose turn it is
        is_white_turn = !is_white_turn;

        update_move_set(TeamColor.WHITE);
        update_move_set(TeamColor.BLACK);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Find the king position
        ChessPosition king_pos = board.find_king_pos(teamColor);

        //Get enemy move set
        Collection<ChessMove> enemy_move_set;

        if (teamColor == TeamColor.WHITE) {
            enemy_move_set = all_black_moves;
        } else {
            enemy_move_set = all_white_moves;
        }

        //Convert those to positions
        Collection<ChessPosition> enemy_pos = new ArrayList<>();
        for (ChessMove move : enemy_move_set) {
            enemy_pos.add(move.getEndPosition());
        }

        if (enemy_pos.contains(king_pos)) {
            return true;
        } else {
            return false;
        }

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //check if in check
        if (!isInCheck(teamColor)) {
            return false;
        }

        //Check if there is a valid move
        return !is_there_a_valid_move(teamColor);

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        //check if in check
        if (isInCheck(teamColor)) {
            return false;
        }

        //Check if there is a valid move
        return !is_there_a_valid_move(teamColor);

        //throw new RuntimeException("Not implemented");
    }

    private boolean is_there_a_valid_move(TeamColor color) {
        //Returns all valid moves

        //Create moves to return
        Collection<ChessMove> all_moves = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {

                //Create chess board position
                ChessPosition position = new ChessPosition(row, column);

                //Get piece there
                ChessPiece piece = board.getPiece(position);

                //Make sure the peice exsists
                if (piece != null && piece.getTeamColor() == color) {

                    //Check to see if you can move
                    if (!validMoves(position).isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;

        //Get all possible white and black moves
        all_white_moves = board.find_all_moves(TeamColor.WHITE);
        all_black_moves = board.find_all_moves(TeamColor.BLACK);

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
        //throw new RuntimeException("Not implemented");
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
    public int hashCode() {
        return 31 * this.toString().hashCode();
    }
}

