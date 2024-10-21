package dataaccess;

import model.GameData;

import java.util.Objects;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> gameTable = new HashMap<>();

    public void addGame(GameData data) {
        int id = data.gameID();
        gameTable.put(id, data);
    }

    public void addPlayerToGameData(int gameID, String userName, boolean addWhite) throws DataAccessException {
        GameData game = gameTable.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game doesn't exist");
        }

        GameData updatedGame;
        if (addWhite) {
            updatedGame = new GameData(game.gameID(), userName, game.blackUsername(), game.gameName(), game.game());
        } else {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), userName, game.gameName(), game.game());
        }

        gameTable.put(gameID, updatedGame);
    }


    public GameData getGame(int id) {
        return gameTable.get(id);
    }

    public HashMap<Integer, GameData> getGames() {
        return gameTable;
    }

    public void deleteAllGame() {
        gameTable.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryGameDAO that = (MemoryGameDAO) o;
        return Objects.equals(gameTable, that.gameTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameTable);
    }

}
