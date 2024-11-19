package ui;

import chess.ChessGame;
import com.google.gson.internal.LinkedTreeMap;
import model.AuthData;
import model.GameData;
import model.GameRequest;
import model.UserData;

import java.util.*;

import static ui.EscapeSequences.*;

public class LoginClient implements Client {
    private final ServerFacade server;
    private State state = State.SIGNEDIN;
    private String authToken;
    private Integer gameID = 1;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    public Integer joinedGameID;


    public LoginClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        System.out.print("\n\n" + SET_TEXT_COLOR_LIGHT_GREY + WHITE_QUEEN + SET_TEXT_ITALIC + "Grand Master Authorized" + WHITE_QUEEN);
        this.authToken = authToken;

    }

    public void addNotificationHandler(String serverUrl, NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;

        try {
            this.ws = new WebSocketFacade(serverUrl, notificationHandler);
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Error: " + e.getMessage());
        }
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
            case "observe" -> observe(params);
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
            this.joinedGameID = joinID + 1000;

            return "The Grandmaster has entered battlefield " + params[0] + RESET_TEXT_BOLD_FAINT + "\n";
        }
        throw new Exception("Join Game Failed. Expected: <Game ID> <WHITE or BLACK>");
    }

    public String observe(String... params) throws Exception {
        if (params.length >= 1) {

            int joinID;
            try {
                joinID = Integer.parseInt(params[0]);
            } catch (Exception ex) {
                throw new Exception("Observe Game Failed. Expected: <Game ID>");
            }

            GameRequest gameReq = new GameRequest();
            gameReq.setGameID(joinID + 1000);
            gameReq.setPlayerColor("observer");

            server.joinGame(gameReq, authToken);
            state = State.INGAME;

            return "You are spying on battlefield " + params[0] + RESET_TEXT_BOLD_FAINT + "\n";
        }
        throw new Exception("Observe Game Failed. Expected: <Game ID>");
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
        Collections.sort(gamesList, new Comparator<LinkedTreeMap<String, Object>>() {
            @Override
            public int compare(LinkedTreeMap<String, Object> game1, LinkedTreeMap<String, Object> game2) {
                // Extract gameIDs and compare them
                Double gameID1 = (Double) game1.get("gameID");
                Double gameID2 = (Double) game2.get("gameID");
                return gameID1.compareTo(gameID2);
            }
        });

        for (LinkedTreeMap<String, Object> game : gamesList) {
            // Extract gameName
            String gameName = (String) game.get("gameName");
            String whitePlayer = (String) game.get("whiteUsername");
            String blackPlayer = (String) game.get("blackUsername");

            str.append("    - Battleground " + SET_TEXT_COLOR_YELLOW + SET_TEXT_BOLD)
                    .append(gameName)
                    .append(RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_BLUE)
                    .append(" at position ")
                    .append(SET_TEXT_COLOR_YELLOW)
                    .append(pos)
                    .append(SET_TEXT_COLOR_BLUE);
            str.append("\n");
            str.append("        > White contestant: " + SET_TEXT_COLOR_MAGENTA)
                    .append(whitePlayer).append(SET_TEXT_COLOR_BLUE).append("\n");
            str.append("        > Black contestant: " + SET_TEXT_COLOR_MAGENTA)
                    .append(blackPlayer).append(SET_TEXT_COLOR_BLUE).append("\n");
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
                        - help | You really don't need this. :)
                        """;
    }

}

