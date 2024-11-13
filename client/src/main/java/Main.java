import chess.*;
import ui.Repl;
import ui.ServerFacade;

import static ui.EscapeSequences.*;

//Note -> Work on storing auths and then do the http add request property


public class Main {
    public static void main(String[] args) {

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println(RESET_TEXT_COLOR + "♕ 240 Chess Client: " + piece);

        Repl repl = new Repl("http://localhost:64994");
        repl.run();
    }
}