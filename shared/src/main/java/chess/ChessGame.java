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

    boolean isWhiteTurn;
    boolean whiteInCheck;
    boolean blackInCheck;
    ChessBoard board;

    Collection<ChessMove> allWhiteMoves;
    Collection<ChessMove> allBlackMoves;

    public ChessGame() {
        this.isWhiteTurn = true;

        //Create a chess board
        this.board = new ChessBoard();
        board.resetBoard();

        //Get all possible white and black moves
        allWhiteMoves = board.findAllMoves(TeamColor.WHITE);
        allBlackMoves = board.findAllMoves(TeamColor.BLACK);

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (isWhiteTurn) {
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
        isWhiteTurn = team == TeamColor.WHITE;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    private void updateMoveSet(TeamColor color) {
        if (color == TeamColor.WHITE) {
            allWhiteMoves = board.findAllMoves(TeamColor.WHITE);
        } else {
            allBlackMoves = board.findAllMoves(TeamColor.BLACK);
        }
    }

    private void updateOppMoveSet(TeamColor color) {
        if (color != TeamColor.WHITE) {
            allWhiteMoves = board.findAllMoves(TeamColor.WHITE);
        } else {
            allBlackMoves = board.findAllMoves(TeamColor.BLACK);
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
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);

        //We need to check to see if the peice is blocking a check //We can do this by "pre-moving" the peice, and then
        //check to see if the king is in check

        //Set start position to null
        board.removePiece(startPosition);

        //Store these values
        Collection<ChessMove> tempBlackMoves = allBlackMoves;
        Collection<ChessMove> tempWhiteMoves = allWhiteMoves;

        // Update move set
        updateOppMoveSet(piece.getTeamColor());

        //If not in check, we can return moves. MAJOR shortcut
        if (!isInCheck(piece.getTeamColor()) && piece.getPieceType() != ChessPiece.PieceType.KING) {
            board.addPiece(startPosition, piece);
            return allMoves;
        }

        // Possible moves
        Collection<ChessMove> validMoves = new ArrayList<>();

        //Loop through each move
        for (ChessMove move : allMoves) {
            //If it takes a piece, we need to return it
            ChessPiece takenPiece = board.getPiece(move.getEndPosition());

            //Add the piece to its move
            board.addPiece(move.getEndPosition(), piece);

            // Update move set
            updateOppMoveSet(piece.getTeamColor());

            //If not in check, we can add the move
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }

            //readd the taken peice
            board.addPiece(move.getEndPosition(), takenPiece);

        }

        //Replace the piece
        board.addPiece(startPosition, piece);

        //update what we need too
        allBlackMoves = tempBlackMoves;
        allWhiteMoves = tempWhiteMoves;


        //Return its moves
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        //Check Valid moves
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());

        //Check to see if there are possibleMoves
        //Check to see if move is in possibleMoves
        if (possibleMoves == null) {

            //Return error
            throw new InvalidMoveException();

        }

        //Check to see if move is in possibleMoves
        if (!possibleMoves.contains(move)) {

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
        if ((piece.getTeamColor() == TeamColor.WHITE) != isWhiteTurn) {

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
        isWhiteTurn = !isWhiteTurn;

        updateMoveSet(TeamColor.WHITE);
        updateMoveSet(TeamColor.BLACK);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Find the king position
        ChessPosition kingPos = board.findKingPos(teamColor);

        //Get enemy move set
        Collection<ChessMove> enemyMoveSet;

        if (teamColor == TeamColor.WHITE) {
            enemyMoveSet = allBlackMoves;
        } else {
            enemyMoveSet = allWhiteMoves;
        }

        //Convert those to positions
        Collection<ChessPosition> enemyPos = new ArrayList<>();
        for (ChessMove move : enemyMoveSet) {
            enemyPos.add(move.getEndPosition());
        }

        if (enemyPos.contains(kingPos)) {
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
        return !isThereValidMove(teamColor);

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
        return !isThereValidMove(teamColor);

        //throw new RuntimeException("Not implemented");
    }

    public boolean isThereValidMove(TeamColor color) {
        //Returns all valid moves

        //Create moves to return
        Collection<ChessMove> allMoves = new ArrayList<>();

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
        allWhiteMoves = board.findAllMoves(TeamColor.WHITE);
        allBlackMoves = board.findAllMoves(TeamColor.BLACK);

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

