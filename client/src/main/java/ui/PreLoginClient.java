package ui;

import com.sun.nio.sctp.NotificationHandler;
import model.UserData;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreLoginClient implements Client {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                case "register" -> register(params);
                case "help" -> help();
                //case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            state = State.SIGNEDIN;

            //Create User
            String userName = params[0];
            String password = params[1];
            String email = params[2];

            UserData userData = new UserData(userName, password, email);

            server.register(userData);

            return String.format(WHITE_KING + SET_TEXT_BOLD + "Welcome Grand Master %s." + RESET_TEXT_BOLD_FAINT + WHITE_KING, userName);
        }
        throw new Exception("Login Failed. Expected: <USERNAME> <PASSWORD> <EMAIL>");
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

    private void assertSignedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }
}

