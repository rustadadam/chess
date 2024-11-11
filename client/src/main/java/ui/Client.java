package ui;

import java.util.Arrays;

public interface Client {
    public String eval(String input);

    public State getState();
    
    public String getAuthToken();
}
