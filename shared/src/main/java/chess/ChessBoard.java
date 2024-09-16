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

        //Just create an empty board
        BoardPositions = new ChessPiece[8][8]; // NOTE. Are these NULL in here? The ans: Yes

        //Call reset Board to remove and then all the pieces
        //resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        BoardPositions[position.getRow() - 1][position.getColumn() - 1] = piece;
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
        return BoardPositions[position.getRow() - 1][position.getColumn() - 1];
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
            addPiece(new ChessPosition(2, col + 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col + 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        // Add rooks
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        // Add knights
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        // Add bishops
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));

        // Add queens
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        // Add kings
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));


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
                if (BoardPositions[row][col] == null) {
                    sb.append(' ').append('|');
                } else {
                    //Get Chess piece
                    ChessPiece piece = BoardPositions[row][col];

                    //Check to see if uppercase or not
                    if (piece.getTeamColor() != ChessGame.TeamColor.BLACK) {

                        //Print whatever it is
                        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                            sb.append('p').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                            sb.append('k').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                            sb.append('q').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                            sb.append('r').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                            sb.append('n').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                            sb.append('b').append('|');
                        }
                    } else {
                        //Print whatever it is
                        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                            sb.append('P').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                            sb.append('K').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                            sb.append('Q').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                            sb.append('R').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                            sb.append('N').append('|');
                        }
                        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                            sb.append('B').append('|');
                        }
                    }
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
        chess_board.resetBoard();

        //Print out the chess board
        System.out.println(chess_board.toString());
    }

    //Overide the to equal function
    @Override
    public boolean equals(Object o) {
        //Equals in memory
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;

        //Check to see if the positions match
        return that.toString().equals(this.toString());
    }

    //Overide the hash Fucntion
    @Override
    public int hashCode() {
        return 31 * toString().hashCode();
    }
}
