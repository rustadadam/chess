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
        AuthService authService = new AuthService();

        //Create data -- We wrap it so it works like a request
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("username", "adam");
        req.setQueryParam("password", "AdamIsAwesome");
        req.setQueryParam("email", "coolio.email.com");

        //Register the person so we can login them in
        userService.register(req);

        //Make the same calls to the handler as LOGIN
        boolean is_correct = userService.verifyPassword(req);

        Assertions.assertTrue(is_correct, "Incorrect password");

        //Get the auth
        AuthData newAuth = authService.getAuth(req);

        //Check the username
        Assertions.assertTrue(newAuth.userName().equals("adam"), "Usernames do not match");

        // Check we have an Auth Token
        Assertions.assertFalse(newAuth.authToken().equals(null), "Auth token is null");

    }

    @Test
    @DisplayName("Failure to login a User")
    public void loginFailureTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        AuthService authService = new AuthService();

        //Create data -- We wrap it so it works like a request
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("username", "adam");
        req.setQueryParam("password", "AdamIsAwesome");
        req.setQueryParam("email", "coolio.email.com");

        //Register the person
        userService.register(req);


        //Give a bad user
        try {
            req.setQueryParam("username", "Kevin");
            userService.verifyPassword(req);
            Assertions.fail("Username does not exist");

        } catch (Exception e) {
            Assertions.assertEquals("User not found", e.getMessage(), "User not found Error not given");

        }

        //Give wrong password
        req.setQueryParam("username", "adam");
        req.setQueryParam("password", "CrazyPassword");
        boolean is_correct = userService.verifyPassword(req);

        Assertions.assertFalse(is_correct, "Incorrect password registered as correct");
    }
}
