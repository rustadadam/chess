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
        String squareColor;
        ChessPiece[][] board = game.getBoard().board;

        if (isPlayerWhite) {
            System.out.print("Printing battlefield as White\n");
            squareColor = SET_BG_COLOR_LIGHT_GREY;
        } else {
            System.out.print("Printing battlefield as Black\n");
            squareColor = SET_BG_COLOR_DARK_GREY;

            //Reverse board
            ChessPiece[][] new_board = game.getBoard().board;
            for (int row = 0; row < 8; row++) {
                new_board[row] = board[7 - row].clone();
            }

            board = new_board;
        }

        StringBuilder sb = new StringBuilder();

        //Loop through rows
        for (int row = 0; row < 8; row++) {

            if (squareColor.equals(SET_BG_COLOR_DARK_GREY)) {
                squareColor = SET_BG_COLOR_LIGHT_GREY;
            } else {
                squareColor = SET_BG_COLOR_DARK_GREY;
            }

            for (int column = 0; column < 8; column++) {

                if (squareColor.equals(SET_BG_COLOR_DARK_GREY)) {
                    squareColor = SET_BG_COLOR_LIGHT_GREY;
                } else {
                    squareColor = SET_BG_COLOR_DARK_GREY;
                }

                sb.append(squareColor);

                sb.append(" ");
                if (board[row][column] != null) {
                    sb.append(printPiece(board[row][column].toString())); //Remember to update pieces for this
                } else {
                    if (!squareColor.equals(SET_BG_COLOR_DARK_GREY)) {
                        sb.append(SET_TEXT_COLOR_LIGHT_GREY);
                    } else {
                        sb.append(SET_TEXT_COLOR_DARK_GREY);
                    }
                    //This will be hidden
                    sb.append(BLACK_PAWN);
                }
                sb.append(" ");
            }

            sb.append(RESET_BG_COLOR);
            sb.append("\n");
        }

        System.out.print(sb + RESET_BG_COLOR);
        return "";
    }

    public String printPiece(String pieceChar) {
        return switch (pieceChar) {
            case "R" -> SET_TEXT_COLOR_GREEN + EscapeSequences.WHITE_ROOK;
            case "N" -> SET_TEXT_COLOR_GREEN + EscapeSequences.WHITE_KNIGHT;
            case "B" -> SET_TEXT_COLOR_GREEN + EscapeSequences.WHITE_BISHOP;
            case "Q" -> SET_TEXT_COLOR_GREEN + EscapeSequences.WHITE_QUEEN;
            case "K" -> SET_TEXT_COLOR_GREEN + EscapeSequences.WHITE_KING;
            case "P" -> SET_TEXT_COLOR_GREEN + EscapeSequences.WHITE_PAWN;
            case "r" -> SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_ROOK;
            case "n" -> SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_KNIGHT;
            case "b" -> SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_BISHOP;
            case "q" -> SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_QUEEN;
            case "k" -> SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_KING;
            case "p" -> SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_PAWN;
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

