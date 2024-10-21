package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board;

    public ChessBoard() {
        //Create Board
        board = new ChessPiece[8][8]; //Initalizes with Null
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
        //throw new RuntimeException("Not implemented");
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow() - 1][position.getColumn() - 1] = null;
        //throw new RuntimeException("Not implemented");
    }

    // Create a function that returns all the pieces of the given color
    public Collection<ChessMove> findAllMoves(ChessGame.TeamColor color) {
        //Create moves to return
        Collection<ChessMove> allMoves = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {

                //Create chess board position
                ChessPosition position = new ChessPosition(row, column);

                //Get piece there
                ChessPiece piece = getPiece(position);

                //Make sure the peice exsists
                if (piece != null && piece.getTeamColor() == color) {

                    //Find where that piece can move
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(this, position);

                    //Add those moves to our move set
                    allMoves.addAll(pieceMoves);
                }
            }
        }

        return allMoves;
    }

    public ChessPosition findKingPos(ChessGame.TeamColor color) {

        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {

                //Create chess board position
                ChessPosition position = new ChessPosition(row, column);

                //Get piece there
                ChessPiece piece = getPiece(position);

                //Make sure the its a king
                if (piece != null) {
                    if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                        return position;
                    }
                }
            }
        }

        // This will never run.
        return null;

    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        //Check if off board
        if (position.getColumn() - 1 < 0 || position.getColumn() - 1 > 7) {
            return null;
        }
        //Remember 0 indexing
        return board[position.getRow() - 1][position.getColumn() - 1];

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //throw new RuntimeException("Not implemented");

        //Place the pieces on the board

        for (int col = 1; col < 9; col++) {
            ChessPosition position = new ChessPosition(2, col);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position, pawn);
        }
        for (int col = 1; col < 9; col++) {
            addPiece(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        //Add rooks
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        //Add Bishops
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));

        //Add Knights
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        //Add kings
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //Add kings
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

    }


    //Add Overides
    @Override
    public String toString() {
        //Create stringbuilder

        StringBuilder sb = new StringBuilder();

        //Loop through rows
        for (int row = 0; row < 8; row++) {

            sb.append("|"); //Add baord columns


            for (int column = 0; column < 8; column++) {
                if (board[row][column] != null) {
                    sb.append(board[row][column].toString()); //Remember to update pieces for this
                } else {
                    sb.append(" ");
                }

            }
        }
        return sb.toString();
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
        ChessBoard that = (ChessBoard) o;
        return that.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return 31 * this.toString().hashCode();
    }
}
