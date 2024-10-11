package dataaccess;

import model.GameData;

public interface GameDAO {
    void addGame(GameData data) throws DataAccessException;

    GameData getGame(int id) throws DataAccessException;

    void deleteGame(int id) throws DataAccessException;

    void deleteAllGame() throws DataAccessException;
}
