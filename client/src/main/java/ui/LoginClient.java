package ui;

import model.AuthData;
import model.UserData;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class LoginClient implements Client {
    private final ServerFacade server;
    private State state = State.SIGNEDIN;

    public LoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        System.out.print(WHITE_QUEEN + SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_ITALIC + "Grand Master Authorized" + WHITE_QUEEN);
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
                case "logout" -> register(params);
                case "help" -> help();
                case "create" -> login();
                case "list" -> login();
                case "play" -> login();
                case "observe" -> login();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {

            //Create User
            String userName = params[0];
            String password = params[1];
            String email = params[2];

            UserData userData = new UserData(userName, password, email);

            AuthData auth = server.register(userData);
            state = State.SIGNEDIN;

            return String.format(WHITE_KING + SET_TEXT_BOLD + "Welcome Grand Master %s." + RESET_TEXT_BOLD_FAINT + WHITE_KING, userName);
        }
        throw new Exception("Login Failed. Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws Exception {
        if (params.length >= 2) {

            //Create User
            String password = params[1];
            String userName = params[0];
            UserData userData = new UserData(userName, password, null);

            server.login(userData);
            state = State.SIGNEDIN;

            return String.format(WHITE_KING + SET_TEXT_BOLD + "Welcome Grand Master %s." + RESET_TEXT_BOLD_FAINT + WHITE_KING, userName);
        }
        throw new Exception("Login Failed. Expected: <USERNAME> <PASSWORD>");
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

