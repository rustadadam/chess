package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> gameTable = new HashMap<>();

    public void addGame(GameData data) {
//        int id = data.getUserID();
//
//        gameTable.put(id, data);
    }

    public GameData getGame(String id) {
        return gameTable.get(id);
    }

    public HashMap<Integer, GameData> getGames() {
        return gameTable;
    }

    public void deleteGame(String id) {
        gameTable.remove(id);
    }

    public void deleteAllGame() {
        gameTable.clear();
    }

}
