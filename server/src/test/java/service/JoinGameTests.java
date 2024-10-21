package service;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.GameService;

import java.util.Collection;
import java.util.Map;

public class JoinGameTests {

    @Test
    @DisplayName("Successfully join Games")
    public void successCreateJoinTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();
        UserService userService = new UserService();

        //Add game
        gameService.createGame("myGame");

        //Check that the game was added
        Map<String, Collection<GameData>> games = gameService.getGames();
        Assertions.assertEquals(1, games.size(), "Game was not added.");

        //Check if info is right
        GameData joinedGame = gameService.joinGame("Adam", "WHITE", 1000);
        Assertions.assertEquals("Adam", joinedGame.whiteUsername());

    }

    @Test
    @DisplayName("Fail Join Games")
    public void FailCreateJoinTest() throws DataAccessException {

        //Create the following like the test does
        GameService gameService = new GameService();

        //Add game
        gameService.createGame("myGame");

        //Check that the game was added
        Map<String, Collection<GameData>> games = gameService.getGames();
        Assertions.assertEquals(1, games.size(), "Game was not added.");

        //Check if info is right
        GameData joinedGame = gameService.joinGame("Adam", "WHITE", 1000);
        Assertions.assertEquals("Adam", joinedGame.whiteUsername());

        //Throw Error
        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame("Kevin", "WHITE", 1000));
    }
}

