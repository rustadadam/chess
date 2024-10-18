package passoff.service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.AuthService;
import service.GameService;
import service.UserService;

public class RegisterTests {
    @Test
    @DisplayName("Successfully Register a new User")
    public void successRegisterTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        AuthService authService = new AuthService();

        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");

        //Register the person
        userService.register(userData);

        //Get the auth
        AuthData authData = authService.getAuth(userData.username());

        //Check the username
        Assertions.assertTrue(authData.username().equals("adam"), "Usernames do not match");

        // Check we have an Auth Token
        Assertions.assertFalse(authData.authToken().equals(null), "Auth token is null");
    }

    @Test
    @DisplayName("Failure to register a User")
    public void registerFailureTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();

        //Create data
        UserData userData = new UserData("adam", null, "coolio.email.com");


        //Don't provide the password
        try {
            userService.register(userData);
            Assertions.fail("Registration should fail");

        } catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage(), "User not found Error not given");

        }

        //Create data
        UserData userData2 = new UserData("adam", "myPassword", null);


        //Don't provide the correct password
        try {
            userService.register(userData2);
            Assertions.fail("Registration should fail");

        } catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage(), "User not found Error not given");
        }

        //Create data
        UserData userData3 = new UserData("adam", "myPassword", "myEmails");

        //Register the person
        userService.register(userData3);

        try {
            userService.register(userData3);
            Assertions.fail("Registration already taken");

        } catch (Exception e) {
            Assertions.assertEquals("Error: already taken", e.getMessage(), "User not found Error not given");
        }


    }
}
