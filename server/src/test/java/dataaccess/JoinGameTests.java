package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.Collection;
import java.util.Map;

public class JoinGameTests extends MyTests {

    @Test
    @DisplayName("Successfully join Games")
    public void successCreateJoinTest() throws DataAccessException {
        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));

        //Check that the game was added
        Map<Integer, GameData> games = gameDatabase.getGames();
        Assertions.assertEquals(1, games.size(), "Game was not added.");


        //Check if info is right
        gameDatabase.addPlayerToGameData(11212, "Adam", true);
        Assertions.assertEquals("Adam", gameDatabase.getGame(11212).whiteUsername());

    }

    @Test
    @DisplayName("Fail Join Games")
    public void failCreateJoinTest() throws DataAccessException {

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

