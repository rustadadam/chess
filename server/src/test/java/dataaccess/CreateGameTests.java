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
    @DisplayName("Successfully add game")
    public void successGetGameTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));

        GameData game = gameDatabase.getGame(11212);
        Assertions.assertNotNull(game);
    }

    @Test
    @DisplayName("Failure to add game")
    public void failGetGameTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));

        GameData game = gameDatabase.getGame(14212);
        Assertions.assertNull(game);
    }

    @Test
    @DisplayName("Successfully add game")
    public void successAddTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        try {
            gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Failure add game")
    public void failureAddTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        Assertions.assertThrows(DataAccessException.class, () -> gameDatabase.addGame(new GameData(12132, null, null, null, new ChessGame())));

    }

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
            Assertions.assertNull(gameData.blackUsername());
            Assertions.assertNotNull(gameData.gameName());
            Assertions.assertNull(gameData.whiteUsername());
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
