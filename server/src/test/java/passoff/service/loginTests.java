package passoff.service;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.AuthService;
import service.GameService;
import service.UserService;

public class loginTests {

    @Test
    @DisplayName("Succesfully login a User")
    public void loginSucessTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthService authService = new AuthService();

        //Create data -- We wrap it so it works like a request
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("username", "adam");
        req.setQueryParam("password", "AdamIsAwesome");
        req.setQueryParam("email", "coolio.email.com");

        //Register the person
        userService.register(req);

        //Make the same calls to the handler
        boolean is_correct = userService.verifyPassword(req);

        Assertions.assertTrue(is_correct, "Incorrect password");

        //Get the auth
        AuthData newAuth = authService.getAuth(req);

        //Check the username
        Assertions.assertTrue(newAuth.userName().equals("adam"), "Usernames do not match");

        // Check we have an Auth Token
        Assertions.assertFalse(newAuth.authToken().equals(null), "Auth token is null");
        
    }
}
