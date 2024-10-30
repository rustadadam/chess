package dataaccess;

import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;

import org.junit.jupiter.api.*;

public class ClearTest extends MyTests {

    @Test
    @DisplayName("Clear Database")
    public void testClearDataBase() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthService authService = new AuthService();

        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");


        userService.register(userData);
        GameData game = gameService.createGame("MyGame");

        //Call the following
        userService.deleteAllUserData();
        gameService.deleteAllGame();
        authService.deleteAllAuth();

        //Check to see if there is no data
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.verifyPassword(userData);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame("adam", "WHITE", game.gameID());
        });

    }

}
