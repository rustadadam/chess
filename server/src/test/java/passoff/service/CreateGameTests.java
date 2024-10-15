package passoff.service;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.GameService;

import java.util.Collection;

public class CreateGameTests {

    @Test
    @DisplayName("Successfully Create games")
    public void successListGamesTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();

        //Add game
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("gameName", "myGame");

        gameService.createGame(req);

        //Check that the game was added
        Collection<GameData> games = gameService.getGames();
        Assertions.assertEquals(1, games.size(), "Game was not added.");

        //Check if info is right
        for (GameData gameData : games) {
            Assertions.assertNotNull(gameData.gameID(), "Game ID was not added.");
            Assertions.assertNotNull(gameData.gameName(), "Game name was not added.");
            Assertions.assertNull(gameData.blackUserName(), "Black user name was incorrectly added.");
            Assertions.assertNull(gameData.whiteUserName(), "White user name was incorrectly added.");
        }
    }
}
