package passoff.service;

import dataaccess.DataAccessException;
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

public class ClearTest {

    @Test
    @DisplayName("Clear Database")
    public void testClearDataBase() throws DataAccessException {

        //Create the following like the test does
        UserService userServiceEmpty = new UserService();
        GameService gameServiceEmpty = new GameService();
        AuthService authServiceEmpty = new AuthService();

        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthService authService = new AuthService();

        //Create data -- We wrap it so it works like a request
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("gameName", "FirstGame");
        req.setQueryParam("username", "adam");
        req.setQueryParam("password", "AdamIsAwesome");
        req.setQueryParam("email", "coolio.email.com");

        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");


        userService.register(userData);
        gameService.createGame(req);

        //Call the following
        userService.deleteAllUserData();
        gameService.deleteAllGame();
        authService.deleteAllAuth();

        //Check to see if there is no data
        Assertions.assertTrue(userService.equals(userServiceEmpty), "Failed on User Service");
        Assertions.assertTrue(gameService.equals(gameServiceEmpty), "Failed on Game Service");
        Assertions.assertTrue(authService.equals(authServiceEmpty), "Failed on Auth Service");
    }

}
