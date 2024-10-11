package service;

import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;

public class GameService {

    private GameDAO dataAccess;

    public GameService() {
        this.dataAccess = new MemoryGameDAO();
    }

    public void deleteAllGame() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllGame();
    }
}