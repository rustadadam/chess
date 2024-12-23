package ui;

import chess.*;
import com.google.gson.internal.LinkedTreeMap;
import model.AuthData;
import model.GameData;
import model.GameRequest;
import model.UserData;

import java.util.*;

import static ui.EscapeSequences.*;

public class GameClient implements Client {
    private final ServerFacade server;
    private GameData gameData;
    private State state = State.INGAME;
    private String authToken;
    private final Boolean isPlayerWhite;
    private WebSocketFacade ws;

    public GameClient(String serverUrl, String authToken, Boolean isPlayerWhite, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;

        this.isPlayerWhite = isPlayerWhite;
    }

    public void addWebSocket(WebSocketFacade ws) {
        try {
            this.ws = ws;
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: " + e.getMessage());
        }
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;

        //Now print the board
        printGame(false);
    }

    public String getAuthToken() {
        return authToken;
    }

    public State getState() {
        return state;
    }

    public String printGame(boolean highlight, String... params) {
        ChessGame game = gameData.game();
        String squareColor;
        ChessPiece[][] board = game.getBoard().board;
        squareColor = SET_BG_COLOR_LIGHT_GREY;
        Set<ChessPosition> moveSet = null;
        if (highlight) {
            moveSet = highlight(params);
            if (moveSet.isEmpty()) {
                return SET_TEXT_COLOR_RED + "Error. No piece selected." + RESET_TEXT_COLOR;
            }
        }
        if (!isPlayerWhite) {
            System.out.print("Printing battlefield as Black\n");
        } else {
            System.out.print("Printing battlefield as White\n");
            ChessPiece[][] newBoard = new ChessPiece[8][8];
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    newBoard[row][col] = board[7 - row][7 - col];
                }
            }
            board = newBoard;
        }
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            if (!isPlayerWhite) {
                sb.append(SET_TEXT_COLOR_MAGENTA).append(row + 1).append(" ");
            } else {
                sb.append(SET_TEXT_COLOR_MAGENTA).append(8 - row).append(" ");
            }
            if (squareColor.equals(SET_BG_COLOR_LIGHT_GREY)) {
                squareColor = SET_BG_COLOR_DARK_GREY;
            } else {
                squareColor = SET_BG_COLOR_LIGHT_GREY;
            }
            for (int column = 0; column < 8; column++) {
                if (squareColor.equals(SET_BG_COLOR_DARK_GREY)) {
                    squareColor = SET_BG_COLOR_LIGHT_GREY;
                } else {
                    squareColor = SET_BG_COLOR_DARK_GREY;
                }
                ChessPosition pos;
                if (!isPlayerWhite) {
                    pos = new ChessPosition((row + 1), (1 + column));
                } else {
                    pos = new ChessPosition(9 - (row + 1), 9 - (1 + column));
                }
                if (moveSet != null && moveSet.contains(pos)) {
                    sb.append(SET_BG_COLOR_YELLOW);
                } else {
                    sb.append(squareColor);
                }
                sb.append(" ");
                if (board[row][column] != null) {
                    sb.append(printPiece(board[row][column].toString())); //Remember to update pieces for this
                } else {
                    if (moveSet != null && moveSet.contains(pos)) {
                        sb.append(SET_TEXT_COLOR_YELLOW);
                    } else if (!squareColor.equals(SET_BG_COLOR_DARK_GREY)) {
                        sb.append(SET_TEXT_COLOR_LIGHT_GREY);
                    } else {
                        sb.append(SET_TEXT_COLOR_DARK_GREY);
                    }
                    sb.append(BLACK_PAWN);
                }
                sb.append(" ");
            }
            sb.append(RESET_BG_COLOR).append("\n");
        }
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
            case "redraw" -> printGame(false, params);
            case "help" -> help();
            case "highlight" -> printGame(true, params);
            case "leave" -> leave();
            case "clear" -> clear();
            case "resign" -> resign();
            case "move" -> move(params);
            default -> help();
        };

    }

    public Set<ChessPosition> highlight(String... params) {
        if (params.length >= 2) {
            Integer row = Integer.parseInt(params[0]);

            Integer col;
            try {
                col = Integer.parseInt(params[1]);

            } catch (Exception e) {
                String letter = params[1].toLowerCase();
                col = letter.charAt(0) - 'a' + 1;
            }

            //Get piece at location
            ChessPosition chessPosition = new ChessPosition(row, 9 - col);
            Collection<ChessMove> moveSet = gameData.game().validMoves(chessPosition);
            Set<ChessPosition> endSet = new HashSet<>();

            //Get just the end position
            if (moveSet == null) {
                System.out.print(SET_TEXT_COLOR_RED + "No Piece ocuppies that square.");
                return null;
            }
            for (ChessMove move : moveSet) {
                endSet.add(move.getEndPosition());
            }

            //add its own position
            endSet.add(chessPosition);

            return endSet;

        } else {
            System.out.print(SET_TEXT_COLOR_RED + "Highlight Failed. Expected: <Piece Row> <Piece Col>");
            return null;
        }
    }

    public String leave() throws Exception {
        state = State.SIGNEDIN;
        ws.leaveGame(authToken, gameData.gameID());
        return "You have left the battlefield";
    }

    public String resign() throws Exception {
        System.out.println(SET_TEXT_COLOR_RED + "You are resigning. Are you sure? [Yes] or [No]");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        if (line.equals("Yes")) {
            ws.resign(authToken, gameData.gameID());
            return "You have resigned";
        } else {
            return "You did not resign";
        }
    }

    public String clear() throws Exception {
        state = State.SIGNEDOUT; //Change later
        server.clearDataBase();
        return "Grandmaster has left the battlefield | Database cleared";
    }

    public String move(String... params) throws Exception {
        //Check to see if its their turn
        try {
            if (isPlayerWhite) {
                if (gameData.game().getTeamTurn() != ChessGame.TeamColor.WHITE) {
                    return SET_TEXT_COLOR_RED + "Error: Its not your turn";
                }
            } else {
                if (gameData.game().getTeamTurn() != ChessGame.TeamColor.BLACK) {
                    return SET_TEXT_COLOR_RED + "Error: Its not your turn";
                }
            }

            if (params.length >= 4) {
                Integer rowStart = Integer.parseInt(params[0]);
                Integer rowEnd = Integer.parseInt(params[2]);


                Integer colStart;
                Integer colEnd;
                try {
                    colEnd = Integer.parseInt(params[3]);

                } catch (Exception e) {
                    String letter = params[3].toLowerCase();
                    colEnd = letter.charAt(0) - 'a' + 1;
                }

                try {
                    colStart = Integer.parseInt(params[1]);

                } catch (Exception e) {
                    String letter = params[1].toLowerCase();
                    colStart = letter.charAt(0) - 'a' + 1;
                }

                //Make move
                ChessPosition startPosition;
                ChessPosition endPosition;

                startPosition = new ChessPosition(rowStart, 9 - colStart);
                endPosition = new ChessPosition(rowEnd, 9 - colEnd);

                ChessMove move = new ChessMove(startPosition, endPosition, null);


                ws.makeMove(authToken, gameData.gameID(), move);
                return " ";
            } else {
                System.out.print(SET_TEXT_COLOR_RED + "Highlight Failed. Expected: <Piece Row> <Piece Col>");
                return " ";
            }

        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: invalid move. Should be [num] [col] [num] [col]";
        }
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

