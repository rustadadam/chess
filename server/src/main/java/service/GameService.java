package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import spark.Request;

import java.util.*;

public class GameService {

    private GameDAO dataAccess;
    private int gameID;

    public GameService() {
        this.gameID = 1000;
        this.dataAccess = new MemoryGameDAO();
    }


    public Map<String, Collection<GameData>> getGames() throws DataAccessException {
        HashMap<Integer, GameData> games = dataAccess.getGames();

        // Create a new collection to store the modified GameData objects
        Collection<GameData> gamesCollection = new HashSet<>();

        for (GameData gameData : games.values()) {
            // Clone each GameData object and set the game field to null
            GameData newGameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    null
            );
            gamesCollection.add(newGameData);
        }

        // Create a map to hold the error message
        Map<String, Collection<GameData>> gameList = new HashMap<>();
        gameList.put("games", gamesCollection);

        return gameList;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Error: bad request");
        }

        GameData newGame = new GameData(gameID++, null, null, gameName, new ChessGame());

        dataAccess.addGame(newGame);

        return newGame;
    }

    public GameData joinGame(String userName, String playerColor, int gameID) throws DataAccessException {

        //See if game already exists
        GameData game = dataAccess.getGame(gameID);

        if (game == null) {
            throw new DataAccessException("Error: bad request"); //Check this
        }

        if (playerColor == null) {
            throw new DataAccessException("Error: bad request"); //NOTE, spectator mode?
        } else if (playerColor.equalsIgnoreCase("white")) {
            if (game.whiteUsername() == null) {
                //Add player to game. :)
                dataAccess.addPlayerToGameData(game.gameID(), userName, true);
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            if (game.blackUsername() == null) {
                dataAccess.addPlayerToGameData(game.gameID(), userName, false);
            } else {
                throw new DataAccessException("Error: already taken");
            }
        }

        return dataAccess.getGame(gameID);
    }

    public void deleteAllGame() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllGame();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameService that = (GameService) o;
        return Objects.equals(dataAccess, that.dataAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataAccess);
    }
}