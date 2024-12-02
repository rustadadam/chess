import chess.*;
import ui.Repl;
import ui.ServerFacade;

// Change error message for invalid move format
// Observe from white
// Switch board around A1 should be a black || Queen should be on D column


public class MainEntry2 {
    public static void main(String[] args) {

        Repl repl = new Repl("http://localhost:8080");
        repl.run();
    }
}