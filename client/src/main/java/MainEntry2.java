import chess.*;
import ui.Repl;
import ui.ServerFacade;

// Change error message for invalid move format
// Observe from white

public class MainEntry2 {
    public static void main(String[] args) {

        Repl repl = new Repl("http://localhost:8080");
        repl.run();
    }
}