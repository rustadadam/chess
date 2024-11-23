package ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import chess.ChessGame;
import model.GameData;
import websocket.messages.ServerMessage;


import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private Client client;
    private State state = State.SIGNEDOUT;
    private final String serverUrl;
    private boolean isPlayerWhite;
    private WebSocketFacade ws;
    private boolean myTurn;

    public Repl(String serverUrl) {
        client = new PreLoginClient(serverUrl);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome Grand Master back to Chess. Sign in to start.");
        try {
            System.out.print(client.eval("help"));
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }


        //The Following is the loop
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.getMessage();

                if (Objects.equals(msg, "failure: 400")) {
                    msg = "Error: bad request";
                } else if (Objects.equals(msg, "failure: 401")) {
                    msg = "Error: unauthorized";
                } else if (Objects.equals(msg, "failure: 403")) {
                    msg = "Error: already taken";
                } else if (Objects.equals(msg, "failure: 500")) {
                    msg = "Error: unknown error";
                }

                System.out.print(SET_TEXT_COLOR_RED + msg);
            }

            //Change client
            if (state != client.getState()) {
                changeClient();
            }
        }
        System.out.println();
    }

    private void changeClient() {
        state = client.getState();
        if (state == State.SIGNEDOUT) {
            client = new PreLoginClient(serverUrl);
        } else if (state == State.SIGNEDIN) {
            String authToken = client.getAuthToken();
            LoginClient loginClient = new LoginClient(serverUrl, authToken);

            //Create a new websocket
            try {
                this.ws = new WebSocketFacade(serverUrl, this);
            } catch (Exception e) {
                System.out.print(SET_TEXT_COLOR_RED + "ERROR: Failed to connect to websocket");
            }

            loginClient.addWebSocket(ws);
            client = loginClient;
        } else if (state == State.INGAME) {
            LoginClient loginClient = (LoginClient) client;
            this.isPlayerWhite = loginClient.isPlayerWhite;
            String authToken = client.getAuthToken();
            GameClient gameClient = new GameClient(serverUrl, authToken, isPlayerWhite, this);
            gameClient.addWebSocket(ws);
            client = gameClient;
        }
    }

    public void notify(ServerMessage notification) {
        System.out.println(SET_TEXT_COLOR_YELLOW + "-> " + notification.message + RESET_TEXT_COLOR);

        if (notification.game != null) {
            client.setGameData(notification.game);

            myTurn = notification.game.game().getTeamTurn() != ChessGame.TeamColor.WHITE;

        }

        printPrompt();
    }

    private void printPrompt() {
        String str = "";
        if (state == State.INGAME) {
            if (!myTurn) {
                str += " - White's Turn";
            } else {
                str += " - Black's Turn";
            }

        }
        str += "] " + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN;

        System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + SET_TEXT_BOLD + "[" + state + str);
    }

}

