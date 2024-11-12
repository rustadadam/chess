package ui;

import model.AuthData;
import model.UserData;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreLoginClient implements Client {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String getAuthToken() {
        return authToken;
    }

    public State getState() {
        return state;
    }

    public String eval(String input) throws Exception {

        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "help" -> help();
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };

    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {

            //Create User
            String userName = params[0];
            String password = params[1];
            String email = params[2];

            UserData userData = new UserData(userName, password, email);

            AuthData auth = server.register(userData);
            authToken = auth.authToken();
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

            AuthData auth = server.login(userData);
            authToken = auth.authToken();
            state = State.SIGNEDIN;

            return String.format(WHITE_KING + SET_TEXT_BOLD + "Welcome Grand Master %s." + RESET_TEXT_BOLD_FAINT + WHITE_KING, userName);
        }
        throw new Exception("Login Failed. Expected: <USERNAME> <PASSWORD>");
    }

    public String help() {
        return SET_TEXT_COLOR_RED + "Choose one of the following options:\n" + SET_TEXT_COLOR_BLUE +
                """
                        - register <USERNAME> <PASSWORD> <EMAIL> | to create an account!
                        - login <USERNAME> <PASSWORD>  | to play Chess!
                        - quit | to leave application!
                        - help | to print the command options!
                        """;
    }

}

