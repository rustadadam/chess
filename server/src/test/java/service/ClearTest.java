package service;

import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;
import passoff.WrappedRequest;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;

import spark.Response;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

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
