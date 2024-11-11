package ui;

package ui;

import chess.ChessGame;
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
    private ChessGame game;
    private State state = State.INGAME;
    private String authToken;

    public GameClient(String serverUrl, String authToken, ChessGame game) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.game = game;
        printGame();
    }

    public String getAuthToken() {
        return authToken;
    }

    public State getState() {
        return state;
    }

    public String printGame(){
        System.out.print("Printing battlefield");

        return "";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "show" -> printGame();
                case "help" -> help();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
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
}

