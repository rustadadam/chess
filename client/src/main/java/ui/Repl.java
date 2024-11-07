package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.NotificationHandler;

public class Repl {
    private Client client;
    private State state = State.SIGNEDOUT;
    private final String serverUrl;

    public Repl(String serverUrl) {
        client = new PreLoginClient(serverUrl);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome Grand Master back to Chess. Sign in to start.");
        System.out.print(client.eval("help"));


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
                var msg = e.toString();
                System.out.print(msg);
            }
            changeClient();
        }
        System.out.println();
    }

    private void changeClient() {
        state = client.getState();
        if (state == State.SIGNEDOUT) {
            client = new PreLoginClient(serverUrl);
        }
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + "[" + state + "] " + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}

