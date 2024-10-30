package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.Collection;
import java.util.Map;

public class CreateGameTests extends MyTests {

    @Test
    @DisplayName("Successfully Create games")
    public void successCreateGamesTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();

        //Add game
        gameService.createGame("myGame");

        //Check that the game was added
        Map<String, Collection<GameData>> games = gameService.getGames();
        Assertions.assertEquals(1, games.size(), "Game was not added.");

        //Check if info is right
        for (GameData gameData : games.get("games")) {
            Assertions.assertNotNull(gameData.gameID(), "Game ID was not added.");
            Assertions.assertNotNull(gameData.gameName(), "Game name was not added.");
            Assertions.assertNull(gameData.blackUsername(), "Black user name was incorrectly added.");
            Assertions.assertNull(gameData.whiteUsername(), "White user name was incorrectly added.");
        }
    }

    @Test
    @DisplayName("Fail to Create games")
    public void failCreateGamesTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();

        //Clear games
        gameService.deleteAllGame();

        //Add game
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(null));

        //Check that the game was added
        Map<String, Collection<GameData>> games = gameService.getGames();
        Assertions.assertEquals(1, games.size(), "Game was added.");

    }
}
