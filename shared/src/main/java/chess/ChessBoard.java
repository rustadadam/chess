package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    // Variables
    // Create an 8 by 8 array
    ChessPiece[][] BoardPositions;

    // ----- CONSTRUCTOR -----
    public ChessBoard() {

        //Call reset Board to remove and then all the pieces
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        BoardPositions[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        //Return chess peice
        return BoardPositions[position.getRow()][position.getColumn()];
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        // Create an empty 8 by 8 array. (or nested)
        BoardPositions = new ChessPiece[8][8]; // NOTE. Are these NULL in here? The ans: Yes

        // Add pawns
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(1, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(6, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        // Add rooks
        addPiece(new ChessPosition(0, 0), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(0, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(7, 0), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(7, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        // Add knights
        addPiece(new ChessPosition(0, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(0, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(7, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(7, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        // Add bishops
        addPiece(new ChessPosition(0, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(0, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(7, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(7, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));

        // Add queens
        addPiece(new ChessPosition(0, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(7, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

        // Add kings
        addPiece(new ChessPosition(0, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(7, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));


        //throw new RuntimeException("Not implemented");
    }

    //Create a to_string function to print out the board -- Just the pawns for now
    @Override
    public String toString() {

        //Create a stringbuilder obejct
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < 8; row++) {

            //Add walls on left side
            sb.append('|');

            for (int col = 0; col < 8; col++) {

                //Print P if it's a pawn
                if (BoardPositions[row][col] != null) {
                    sb.append(' ').append("P").append(' ').append('|');
                } else {
                    sb.append(' ').append(" ").append(' ').append('|');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    //Create a debugging MAIN function
    public static void main(String[] args) {
        // Create an 8 by 8 array of ChessPiece objects
        ChessBoard chess_board = new ChessBoard();

        //Test get piece
        ChessPosition pos = new ChessPosition(0, 6);
        ChessPiece found_piece = chess_board.getPiece(pos);
        System.out.println(found_piece);

        //Print out the chess board
        System.out.println(chess_board.toString());
    }
}
