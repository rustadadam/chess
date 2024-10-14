package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    void addGame(GameData data) throws DataAccessException;

    GameData getGame(int id) throws DataAccessException;

    public HashMap<Integer, GameData> getGames() throws DataAccessException;

    void addPlayerToGameData(int gameID, String userName, boolean addWhite) throws DataAccessException;

    void deleteGame(int id) throws DataAccessException;

    void deleteAllGame() throws DataAccessException;
}
