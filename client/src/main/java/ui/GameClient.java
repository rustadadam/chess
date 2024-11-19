package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
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
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;

    public GameClient(String serverUrl, String authToken, GameData gameData, Boolean isPlayerWhite, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameData = gameData;
        this.isPlayerWhite = isPlayerWhite;
        printGame();
    }

    public void addNotificationHandler(String serverUrl, NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;

        try {
            this.ws = new WebSocketFacade(serverUrl, notificationHandler);
        } catch (Exception e) {
            System.out.print(SET_TEXT_COLOR_RED + "ERROR: Failed to connect to websocket");
        }
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
        squareColor = SET_BG_COLOR_LIGHT_GREY;

        if (isPlayerWhite) {
            System.out.print("Printing battlefield as White\n");
        } else {
            System.out.print("Printing battlefield as Black\n");

            //Reverse board
            ChessPiece[][] newBoard = new ChessPiece[8][8];
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    newBoard[row][col] = board[7 - row][7 - col];
                }
            }

            board = newBoard;
        }

        StringBuilder sb = new StringBuilder();

        //Loop through rows
        for (int row = 0; row < 8; row++) {

            //Line numbers
            if (!isPlayerWhite) {
                sb.append(SET_TEXT_COLOR_MAGENTA).append(row + 1).append(" ");
            } else {
                sb.append(SET_TEXT_COLOR_MAGENTA).append(8 - row).append(" ");
            }

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

        //Print line numbers
        sb.append("  ");
        boolean addSpace = true;

        String[] strList;
        if (isPlayerWhite) {
            strList = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        } else {
            strList = new String[]{"H", "G", "F", "E", "D", "C", "B", "A"};
        }

        for (String letter : strList) {
            sb.append(SET_TEXT_COLOR_MAGENTA).append("  ").append(letter).append("  ");
            if (addSpace) {
                sb.append(" ");
                addSpace = false;
            }

            if (letter.equals("C")) {
                addSpace = true;
            }

            if (letter.equals("F")) {
                addSpace = true;
            }
        }

        System.out.print(sb + RESET_BG_COLOR + "\n");
        return "";
    }

    public String printPiece(String pieceChar) {
        return switch (pieceChar) {
            case "R" -> SET_TEXT_COLOR_GREEN + WHITE_ROOK;
            case "N" -> SET_TEXT_COLOR_GREEN + WHITE_KNIGHT;
            case "B" -> SET_TEXT_COLOR_GREEN + WHITE_BISHOP;
            case "Q" -> SET_TEXT_COLOR_GREEN + WHITE_QUEEN;
            case "K" -> SET_TEXT_COLOR_GREEN + WHITE_KING;
            case "P" -> SET_TEXT_COLOR_GREEN + WHITE_PAWN;
            case "r" -> SET_TEXT_COLOR_BLUE + BLACK_ROOK;
            case "n" -> SET_TEXT_COLOR_BLUE + BLACK_KNIGHT;
            case "b" -> SET_TEXT_COLOR_BLUE + BLACK_BISHOP;
            case "q" -> SET_TEXT_COLOR_BLUE + BLACK_QUEEN;
            case "k" -> SET_TEXT_COLOR_BLUE + BLACK_KING;
            case "p" -> SET_TEXT_COLOR_BLUE + BLACK_PAWN;
            case " " -> EMPTY;
            default -> " ";
        };
    }

    public String eval(String input) throws Exception {

        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> printGame();
            case "help" -> help();
            case "highlight" -> highlight();
            case "leave" -> leave();
            case "clear" -> clear();
            default -> help();
        };

    }

    public String highlight(String... params) throws Exception {
        if (params.length >= 2) {

            int row;
            int col;

            try {
                row = Integer.parseInt(params[0]);
                col = Integer.parseInt(params[1]);
            } catch (Exception ex) {
                throw new Exception("Highlight Failed. Expected: <Piece Row> <Piece Col>");
            }

            //Get piece at location
            ChessPosition chessPosition = new ChessPosition(row, col);
            ChessPiece piece = gameData.game().getBoard().getPiece(chessPosition);

            gameData.game().validMoves(chessPosition);

            return "The Grandmaster has entered battlefield " + params[0] + RESET_TEXT_BOLD_FAINT + "\n";
        }
        throw new Exception("Join Game Failed. Expected: <Game ID> <WHITE or BLACK>");
    }

    public String leave() throws Exception {
        state = State.SIGNEDIN; //Change later
        return "Grandmaster has left the battlefield";
    }

    public String clear() throws Exception {
        state = State.SIGNEDOUT; //Change later
        server.clearDataBase();
        return "Grandmaster has left the battlefield | Database cleared";
    }


    public String help() {
        return SET_TEXT_COLOR_RED + "Choose one of the following options:\n" + SET_TEXT_COLOR_BLUE +
                """
                        - redraw | Print the game again!
                        - move <Piece Row> <Piece Col> <Move Row> <Move Col> | Deliver a devastating blow!
                        - highlight <Piece Row> <Piece Col> | Highlights the possible moves for your selected piece
                        - leave | If your opponent smells...
                        - resign | Hope you didn't bet your career on this game!
                        - help | Put on training wheels. 
                        """;
    }
}

