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

    public ChessGame.TeamColor color;
    public ChessPiece.PieceType type;

    //Move set
    public Collection<ChessMove> availableMoves;

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
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    //Move helper
    public void moveHelp(ChessBoard board, ChessPosition myPosition, int moveRow, int moveCol, boolean repeat) {

        //Continue
        boolean shouldContinue = true;
        int adjustRow = moveRow;
        int adjustCol = moveCol;

        while (shouldContinue) {
            if (!repeat) {
                shouldContinue = false;
            }

            //Make the moves
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + adjustRow, myPosition.getColumn() + adjustCol);


            if (newPosition.getRow() > 8 || newPosition.getRow() < 1) {
                shouldContinue = false;
                break;
            }
            if (newPosition.getColumn() > 8 || newPosition.getColumn() < 1) {
                shouldContinue = false;
                break;
            }

            //New move
            ChessMove move = new ChessMove(myPosition, newPosition, null);

            if (board.getPiece(newPosition) == null) {
                //We can add move!
                availableMoves.add(move);
                adjustRow += moveRow;
                adjustCol += moveCol;
            } else {
                //Position is occupeied
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    //Enemy color//We can add move!
                    availableMoves.add(move);
                    adjustRow += moveRow;
                    adjustCol += moveCol;
                }

                //This happens regardless
                shouldContinue = false;
            }

        }
    }

    public void checkPromotion(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition) {
        if (newPosition.getRow() == 8 || newPosition.getRow() == 1) {
            //You get promoted!
            for (ChessPiece.PieceType peiceType : ChessPiece.PieceType.values()) {
                if (peiceType != ChessPiece.PieceType.KING && peiceType != ChessPiece.PieceType.PAWN) {
                    //Add the moves!
                    ChessMove move = new ChessMove(myPosition, newPosition, peiceType);
                    availableMoves.add(move);
                }
            }

        }
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (availableMoves.size() > 0) {
            availableMoves = new ArrayList<>();
        }
        if (type == ChessPiece.PieceType.ROOK || type == ChessPiece.PieceType.QUEEN) {
            moveHelp(board, myPosition, 1, 0, true);
            moveHelp(board, myPosition, 0, 1, true);
            moveHelp(board, myPosition, -1, 0, true);
            moveHelp(board, myPosition, 0, -1, true);
        }
        if (type == ChessPiece.PieceType.BISHOP || type == ChessPiece.PieceType.QUEEN) {
            moveHelp(board, myPosition, 1, 1, true);
            moveHelp(board, myPosition, -1, -1, true);
            moveHelp(board, myPosition, -1, 1, true);
            moveHelp(board, myPosition, 1, -1, true);
        }
        if (type == ChessPiece.PieceType.KNIGHT) {
            moveHelp(board, myPosition, 2, -1, false);
            moveHelp(board, myPosition, 2, 1, false);
            moveHelp(board, myPosition, -2, 1, false);
            moveHelp(board, myPosition, -2, -1, false);
            moveHelp(board, myPosition, -1, 2, false);
            moveHelp(board, myPosition, 1, 2, false);
            moveHelp(board, myPosition, 1, -2, false);
            moveHelp(board, myPosition, -1, -2, false);
        }
        if (type == ChessPiece.PieceType.KING) {
            moveHelp(board, myPosition, 1, -1, false);
            moveHelp(board, myPosition, 1, 0, false);
            moveHelp(board, myPosition, 1, 1, false);
            moveHelp(board, myPosition, 0, -1, false);
            moveHelp(board, myPosition, 0, 1, false);
            moveHelp(board, myPosition, -1, -1, false);
            moveHelp(board, myPosition, -1, 0, false);
            moveHelp(board, myPosition, -1, 1, false);
        }
        if (type == ChessPiece.PieceType.PAWN) {
            if (color == ChessGame.TeamColor.BLACK) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    checkPromotion(board, myPosition, newPosition);
                    if (availableMoves.size() < 3) {
                        availableMoves.add(move);
                    }
                    if (myPosition.getRow() == 7) {
                        newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                        if (board.getPiece(newPosition) == null) {
                            move = new ChessMove(myPosition, newPosition, null);
                            availableMoves.add(move);
                            checkPromotion(board, myPosition, newPosition);
                        }
                    }
                }
                newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        checkPromotion(board, myPosition, newPosition);
                        if (availableMoves.size() < 3) {
                            availableMoves.add(move);
                        }
                    }
                }
                newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        availableMoves.add(move);
                        checkPromotion(board, myPosition, newPosition);
                    }
                }
            } else {
                //check to move forward
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    checkPromotion(board, myPosition, newPosition);
                    if (availableMoves.size() < 3) {
                        availableMoves.add(move);
                    }
                    if (myPosition.getRow() == 2) {
                        newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                        if (board.getPiece(newPosition) == null) {
                            move = new ChessMove(myPosition, newPosition, null);
                            availableMoves.add(move);
                            checkPromotion(board, myPosition, newPosition);
                        }
                    }
                }
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        checkPromotion(board, myPosition, newPosition);
                        if (availableMoves.size() < 3) {
                            availableMoves.add(move);
                        }
                    }
                }
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != color) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        availableMoves.add(move);
                        checkPromotion(board, myPosition, newPosition);
                    }
                }
            }
        }

        //throw new RuntimeException("Not implemented");
        return availableMoves;
    }

    //Add Overides
    @Override
    public String toString() {
        //Simple printing for board
        if (color == ChessGame.TeamColor.WHITE) {
            if (type == ChessPiece.PieceType.KING) {
                return "K";
            }
            if (type == ChessPiece.PieceType.QUEEN) {
                return "Q";
            }
            if (type == ChessPiece.PieceType.BISHOP) {
                return "B";
            }
            if (type == ChessPiece.PieceType.KNIGHT) {
                return "N";
            }
            if (type == ChessPiece.PieceType.ROOK) {
                return "R";
            }
            if (type == ChessPiece.PieceType.PAWN) {
                return "P";
            }
        }
        //Otherwise color is black
        if (type == ChessPiece.PieceType.KING) {
            return "k";
        }
        if (type == ChessPiece.PieceType.QUEEN) {
            return "q";
        }
        if (type == ChessPiece.PieceType.BISHOP) {
            return "b";
        }
        if (type == ChessPiece.PieceType.KNIGHT) {
            return "n";
        }
        if (type == ChessPiece.PieceType.ROOK) {
            return "r";
        }
        if (type == ChessPiece.PieceType.PAWN) {
            return "p";
        }

        //This never runs
        return null;
    }

    //Overide
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return that.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return 31 * this.toString().hashCode() + this.color.hashCode();
    }
}
