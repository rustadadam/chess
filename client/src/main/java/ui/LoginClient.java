package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class LoginClient implements Client {
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private String authToken;
    private Integer gameID = 0;

    public LoginClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        System.out.print("\n\n" + SET_TEXT_COLOR_LIGHT_GREY + WHITE_QUEEN + SET_TEXT_ITALIC + "Grand Master Authorized" + WHITE_QUEEN);
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public State getState() {
        return state;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "help" -> help();
                case "create" -> create(params);
                case "list" -> logout(params);
                case "play" -> logout(params);
                case "observe" -> logout(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String logout(String... params) throws Exception {

        server.logout(authToken);
        state = State.SIGNEDOUT;

        return String.format(WHITE_KING + SET_TEXT_BOLD + "Enjoy your break Grand Master." + RESET_TEXT_BOLD_FAINT + WHITE_KING);

    }

    public String create(String... params) throws Exception {
        if (params.length >= 1) {

            //Create User
            String gameName = params[0];
            GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
            gameID++;

            server.createGame(gameData, authToken);

            return String.format("Battleground at Game ID %s is prepared." + RESET_TEXT_BOLD_FAINT, gameID - 1);
        }
        throw new Exception("Create Game Failed. Expected: <Game Name>");
    }

    public String help() {
        return SET_TEXT_COLOR_RED + "Choose one of the following options:\n" + SET_TEXT_COLOR_BLUE +
                """
                        - create <Game Name> | Create a Game to Crush Your Opponents!
                        - list | to see all available games!
                        - join <Game ID> <WHITE or BLACK> | Face down a Dragon in Chess!
                        - observe <Game ID> | Keep an eye on the competition...
                        - logout | When you are done avenging the death of your Father.
                        - quit | When you realize that you can make more money doing math than chess.
                        - help | You really don't need this. :)
                        """;
    }

    private void assertSignedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }
}

