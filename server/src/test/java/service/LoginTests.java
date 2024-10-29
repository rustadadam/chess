package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.AuthService;
import service.UserService;

public class LoginTests extends MyTests {

    @Test
    @DisplayName("Succesfully login a User")
    public void loginSucessTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        AuthService authService = new AuthService();

        //Create data -- We wrap it so it works like a request
        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");

        //Register the person so we can login them in
        userService.register(userData);

        //Make the same calls to the handler as LOGIN
        boolean isCorrect = userService.verifyPassword(userData);

        Assertions.assertTrue(isCorrect, "Incorrect password");

        //Get the auth
        AuthData newAuth = authService.getAuth(userData.username());

        //Check the username
        Assertions.assertTrue(newAuth.username().equals("adam"), "Usernames do not match");

        // Check we have an Auth Token
        Assertions.assertFalse(newAuth.authToken().equals(null), "Auth token is null");

    }

    @Test
    @DisplayName("Failure to login a User")
    public void loginFailureTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();

        //Create data
        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");

        //Register the person
        userService.register(userData);


        //Give a bad user
        try {
            UserData userData2 = new UserData("kevin", "AdamIsAwesome", "coolio.email.com");
            userService.verifyPassword(userData2);
            Assertions.fail("Username does not exist");

        } catch (Exception e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage(), "User not found Error not given");

        }

        //Give wrong password
        UserData userData3 = new UserData("adam", "lame", "coolio.email.com");
        Assertions.assertThrows(DataAccessException.class, () -> userService.verifyPassword(userData3));
    }
}
