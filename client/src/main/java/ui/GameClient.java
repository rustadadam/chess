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
        if (isPlayerWhite) {
            System.out.print("Printing battlefield as White");
        } else {
            System.out.print("Printing battlefield as Black");
        }

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
                case "leave" -> leave();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String leave() {
        state = State.SIGNEDIN;
        return "Grandmaster has left the battlefield";
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

