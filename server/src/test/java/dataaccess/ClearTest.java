package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

public class ClearTest extends MyTests {

    @Test
    @DisplayName("Clear Database")
    public void testClearDataBase() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();
        DatabaseGameDAO gameData = new DatabaseGameDAO();

        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");


        user.addUser(userData);
        gameData.addGame(new GameData(11212, "hi", "you", "myGame", new ChessGame()));

        //Call the following
        user.deleteAllUser();
        gameData.deleteAllGame();

        //Check to see if there is no data
        Assertions.assertNull(user.getUser("adam"));
        Assertions.assertNull(gameData.getGame(11212));

    }

}
