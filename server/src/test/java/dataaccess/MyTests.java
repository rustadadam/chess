package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import service.AuthService;
import service.GameService;
import service.UserService;

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
