package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.internal.LinkedTreeMap;
import model.AuthData;
import model.GameData;
import model.GameRequest;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static ui.EscapeSequences.*;

public class GameClient implements Client {
    private final ServerFacade server;
    private GameData gameData;
    private State state = State.INGAME;
    private String authToken;
    private Boolean isPlayerWhite;

    public GameClient(String serverUrl, String authToken, GameData gameData, Boolean isPlayerWhite) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameData = gameData;
        this.isPlayerWhite = isPlayerWhite;
        printGame();
    }

    public String getAuthToken() {
        return authToken;
    }

    public State getState() {
        return state;
    }

    public String printGame() {
        //Placeholder for now
        ChessGame game = new ChessGame();

        if (isPlayerWhite) {
            System.out.print("Printing battlefield as White");
        } else {
            System.out.print("Printing battlefield as Black");
        }

        ChessPiece[][] board = game.getBoard().board;

        StringBuilder sb = new StringBuilder();

        //Loop through rows
        String squareColor = SET_BG_COLOR_DARK_GREY;
        for (int row = 0; row < 8; row++) {
            if (squareColor.equals(SET_BG_COLOR_DARK_GREY)) {
                squareColor = SET_BG_COLOR_LIGHT_GREY;
            } else {
                squareColor = SET_BG_COLOR_DARK_GREY;
            }

            sb.append(" ");

            for (int column = 0; column < 8; column++) {
                if (board[row][column] != null) {
                    sb.append(printPiece(board[row][column].toString())); //Remember to update pieces for this
                } else {
                    sb.append(" ");
                }
            }
        }

        System.out.print(sb);
        return "";
    }

    public String printPiece(String pieceChar) {
        return switch (pieceChar) {
            case "R" -> EscapeSequences.WHITE_ROOK;
            case "N" -> EscapeSequences.WHITE_KNIGHT;
            case "B" -> EscapeSequences.WHITE_BISHOP;
            case "Q" -> EscapeSequences.WHITE_QUEEN;
            case "K" -> EscapeSequences.WHITE_KING;
            case "P" -> EscapeSequences.WHITE_PAWN;
            case "r" -> EscapeSequences.BLACK_ROOK;
            case "n" -> EscapeSequences.BLACK_KNIGHT;
            case "b" -> EscapeSequences.BLACK_BISHOP;
            case "q" -> EscapeSequences.BLACK_QUEEN;
            case "k" -> EscapeSequences.BLACK_KING;
            case "p" -> EscapeSequences.BLACK_PAWN;
            case " " -> EscapeSequences.EMPTY;
            default -> " ";
        };
    }

    public String eval(String input) throws Exception {

        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "show" -> printGame();
            case "help" -> help();
            case "leave" -> leave();
            default -> help();
        };

    }

    public String leave() throws Exception {
        state = State.SIGNEDOUT; //Change later
        server.clearDataBase();
        return "Grandmaster has left the battlefield | Database cleared";
    }


    public String help() {
        return SET_TEXT_COLOR_RED + "Choose one of the following options:\n" + SET_TEXT_COLOR_BLUE +
                """
                        - play <Game Name> | Create a Game to Crush Your Opponents!
                        - make move
                        - Finish later
                        """;
    }
}

