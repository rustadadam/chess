package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.Collection;

public class ListGamesTests extends MyTests {

    @Test
    @DisplayName("Successfully list all games")
    public void successListGamesTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //We have no games yet
        Assertions.assertEquals(gameDatabase.getGames().size(), 0, "Games already exist.");

        //Add game
        gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));

        //Check game size
        Assertions.assertEquals(1, gameDatabase.getGames().size(), "Incorrect number of games.");
    }

    @Test
    @DisplayName("Failure listing all games")
    public void failListGamesTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseGameDAO gameDatabase = new DatabaseGameDAO();

        //Add game
        gameDatabase.addGame(new GameData(11212, null, null, "myGame", new ChessGame()));

        //Check if info is right
        for (GameData gameData : gameDatabase.getGames().values()) {
            Assertions.assertEquals(gameData.gameName(), "myGame", "Incorrect game name.");
        }
    }
}
