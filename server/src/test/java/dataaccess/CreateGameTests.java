package dataaccess;

import chess.ChessGame;
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
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));

        //Check that the game was added
        Map<Integer, GameData> games = gameDatabase.getGames();
        Assertions.assertEquals(1, games.size(), "Game was not added.");

        //Check if info is right
        for (GameData gameData : games.values()) {
            Assertions.assertNotNull(gameData.gameName(), "Game name was not added.");
            Assertions.assertNull(gameData.blackUsername(), "Black user name was incorrectly added.");
            Assertions.assertNull(gameData.whiteUsername(), "White user name was incorrectly added.");
        }
    }

    @Test
    @DisplayName("Fail to Create games")
    public void failCreateGamesTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Clear games
        gameDatabase.deleteAllGame();

        //Add game
        Assertions.assertThrows(DataAccessException.class, () -> gameDatabase.addGame(new GameData(11212, null, null, null, new ChessGame())));
    }
}
