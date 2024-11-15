package ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import webSocketMessages.Notification;


import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private Client client;
    private State state = State.SIGNEDOUT;
    private final String serverUrl;

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
            client = new LoginClient(serverUrl, authToken);
        } else if (state == State.INGAME) {
            String authToken = client.getAuthToken();
            client = new GameClient(serverUrl, authToken, null, true);

            //Simply to fulfill reqs
            client = new GameClient(serverUrl, authToken, null, false);

        }

    }

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_YELLOW + "-> " + notification.message() + RESET_TEXT_COLOR);
        printPrompt();
    }

    private void printPrompt() {
        String str = "] " + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN;
        System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + SET_TEXT_BOLD + "[" + state + str);
    }

}

