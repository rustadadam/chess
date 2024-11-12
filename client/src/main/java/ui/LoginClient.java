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

public class LoginClient implements Client {
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private String authToken;
    private Integer gameID = 1;

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

    public String eval(String input) throws Exception {

        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "logout" -> logout(params);
            case "help" -> help();
            case "create" -> create(params);
            case "list" -> list(params);
            case "join" -> join(params);
            case "observe" -> logout(params);
            default -> help();
        };

    }

    public String logout(String... params) throws Exception {

        server.logout(authToken);
        state = State.SIGNEDOUT;

        return String.format(WHITE_KING + SET_TEXT_BOLD + "Enjoy your break Grand Master." + RESET_TEXT_BOLD_FAINT + WHITE_KING);

    }

    public String join(String... params) throws Exception {
        if (params.length >= 2) {

            int joinID;

            try {
                joinID = Integer.parseInt(params[0]);
            } catch (Exception ex) {
                throw new Exception("Join Game Failed. Expected: <Game ID> <WHITE or BLACK>");
            }

            String color = params[1];
            GameRequest gameReq = new GameRequest();
            gameReq.setGameID(joinID + 1000);
            gameReq.setPlayerColor(color);

            server.joinGame(gameReq, authToken);
            state = State.INGAME;

            //At a later point, we may need to store the game data

            return "The Grandmaster has entered battlefield " + params[0] + RESET_TEXT_BOLD_FAINT + "\n";
        }
        throw new Exception("Join Game Failed. Expected: <Game ID> <WHITE or BLACK>");
    }

    public String create(String... params) throws Exception {
        if (params.length >= 1) {

            String gameName = params[0];
            GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
            gameID++;

            server.createGame(gameData, authToken);

            return String.format("Battleground at %s is prepared." + RESET_TEXT_BOLD_FAINT, gameName);
        }
        throw new Exception("Create Game Failed. Expected: <Game Name>");
    }

    public String list(String... params) throws Exception {

        StringBuilder str = new StringBuilder("Battles available to join: \n");
        LinkedTreeMap<String, Object> games = (LinkedTreeMap<String, Object>) server.listGames(authToken);

        ArrayList<LinkedTreeMap<String, Object>> gamesList = (ArrayList<LinkedTreeMap<String, Object>>) games.get("games");

        Integer pos = 1;
        for (LinkedTreeMap<String, Object> game : gamesList) {
            // Extract gameID and gameName
            String gameName = (String) game.get("gameName");

            str.append("    - Battleground " + SET_TEXT_COLOR_YELLOW + SET_TEXT_BOLD).append(gameName).append(RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_BLUE).append(" at position ").append(pos.toString());
            str.append("\n");
            pos++;
        }

        return str + SET_TEXT_COLOR_BLUE;
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

