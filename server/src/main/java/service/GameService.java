package service;

import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameService {

    private GameDAO dataAccess;

    public GameService() {
        this.dataAccess = new MemoryGameDAO();
    }

    public Collection<GameData> getGames() {
        HashMap<Integer, GameData> games = dataAccess.getGames();
        //Create an empty collection
        Collection<GameData> gamesCollection = games.values();

        return gamesCollection;
    }

    public void deleteAllGame() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllGame();
    }
}