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
}
