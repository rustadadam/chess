package service;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class ListGamesTests extends MyTests {

    @Test
    @DisplayName("Successfully list all games")
    public void successListGamesTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();

        //We have no games yet
        Assertions.assertEquals(gameService.getGames().get("games").size(), 0, "Games already exist.");

        //Add game
        gameService.createGame("myGame");

        //Check game size
        Collection<GameData> games = gameService.getGames().get("games");
        Assertions.assertEquals(1, games.size(), "Incorrect number of games.");

        //Check if info is right
        for (GameData gameData : games) {
            Assertions.assertEquals(gameData.gameName(), "myGame", "Incorrect game name.");
        }
    }

    @Test
    @DisplayName("Failure listing all games")
    public void failListGamesTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();

        //Add game
        gameService.createGame("myGame2");

        //Check game size
        Collection<GameData> games = gameService.getGames().get("games");
        Assertions.assertEquals(1, games.size(), "Incorrect number of games.");

        //Check if info is right
        for (GameData gameData : games) {
            Assertions.assertEquals(gameData.gameName(), "myGame2", "Incorrect game name.");
        }
    }
}
