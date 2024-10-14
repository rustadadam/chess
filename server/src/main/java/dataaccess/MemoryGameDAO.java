package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> gameTable = new HashMap<>();

    public void addGame(GameData data) {
//        int id = data.getUserID();
//
//        gameTable.put(id, data);
    }

    public void addPlayerToGameData(int gameID, String userName, boolean addWhite) throws DataAccessException {
        GameData game = gameTable.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game doesn't exist");
        }

        GameData updatedGame;
        if (addWhite) {
            updatedGame = new GameData(game.gameID(), userName, game.blackUserName(), game.gameName(), game.game());
        } else {
            updatedGame = new GameData(game.gameID(), game.whiteUserName(), userName, game.gameName(), game.game());
        }

        gameTable.put(gameID, updatedGame);
    }


    public GameData getGame(int id) {
        return gameTable.get(id);
    }

    public HashMap<Integer, GameData> getGames() {
        return gameTable;
    }

    public void deleteGame(int id) {
        gameTable.remove(id);
    }

    public void deleteAllGame() {
        gameTable.clear();
    }

}
