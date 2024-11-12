package ui;

import java.util.Arrays;

public interface Client {
    public String eval(String input) throws Exception;

    public State getState();

    public String getAuthToken();
}
