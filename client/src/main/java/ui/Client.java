package ui;

import model.GameData;

import java.util.Arrays;

public interface Client {
    public String eval(String input) throws Exception;

    public State getState();

    public String getAuthToken();

    public void setGameData(GameData gameData);

    public GameData getGameData();
}
