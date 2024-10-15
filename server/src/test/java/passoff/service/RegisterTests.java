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

public class RegisterTests {
    @Test
    @DisplayName("Successfully Register a new User")
    public void successRegisterTest() throws DataAccessException {

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

        //Get the auth
        AuthData authData = authService.getAuth(req);

        //Check the username
        Assertions.assertTrue(authData.userName().equals("adam"), "Usernames do not match");

        // Check we have an Auth Token
        Assertions.assertFalse(authData.authToken().equals(null), "Auth token is null");
    }

    @Test
    @DisplayName("Failure to register a User")
    public void registerFailureTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();

        //Create data -- We wrap it so it works like a request
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("username", "adam");
        req.setQueryParam("email", "coolio.email.com");

        //Don't provide the password
        try {
            userService.register(req);
            Assertions.fail("Registration should fail");

        } catch (Exception e) {
            Assertions.assertEquals("Password not provided", e.getMessage(), "User not found Error not given");

        }

        //Create data -- We wrap it so it works like a request
        WrappedRequest req2 = new WrappedRequest();
        req2.setQueryParam("username", "adam");
        req2.setQueryParam("password", "myPassword");

        //Don't provide the correct password
        try {
            userService.register(req2);
            Assertions.fail("Registration should fail");

        } catch (Exception e) {
            Assertions.assertEquals("Email not provided", e.getMessage(), "User not found Error not given");
        }

        WrappedRequest req3 = new WrappedRequest();

        // Test userAlready taken
        req3.setQueryParam("username", "adam");
        req3.setQueryParam("password", "AdamIsAwesome");
        req3.setQueryParam("email", "coolio.email.com");

        //Register the person
        userService.register(req3);

        try {
            userService.register(req3);
            Assertions.fail("Registration already taken");

        } catch (Exception e) {
            Assertions.assertEquals("Cannot create new user", e.getMessage(), "User not found Error not given");
        }


    }
}
