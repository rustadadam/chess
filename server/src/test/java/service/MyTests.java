package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;

public class MyTests {
    @BeforeEach
    public void setup() throws DataAccessException {
        //Create the following like the test does
        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthService authService = new AuthService();

        gameService.deleteAllGame();
        authService.deleteAllAuth();
        userService.deleteAllUserData();
    }
}
