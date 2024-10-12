package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    void addGame(GameData data) throws DataAccessException;

    GameData getGame(String id) throws DataAccessException;

    public HashMap<Integer, GameData> getGames() throws DataAccessException;


    void deleteGame(String id) throws DataAccessException;

    void deleteAllGame() throws DataAccessException;
}
