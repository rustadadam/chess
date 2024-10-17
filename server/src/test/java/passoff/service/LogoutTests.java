package passoff.service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.AuthService;
import service.UserService;

public class LogoutTests {

    @Test
    @DisplayName("Succesfully logout a User")
    public void logoutSucessTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        AuthService authService = new AuthService();
        AuthService authServiceEmpty = new AuthService();

        //Create data -- We wrap it so it works like a request
        WrappedRequest req = new WrappedRequest();
        req.setQueryParam("username", "adam");
        req.setQueryParam("password", "AdamIsAwesome");
        req.setQueryParam("email", "coolio.email.com");

        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");


        //Register the person and then login (verify password function)
        userService.register(userData);
        userService.verifyPassword(userData);
        authService.getAuth(userData.username());

        // Now log them out
        authService.logout(userData.username());

        //Check to see if both Authservice's databases's is empty
        Assertions.assertEquals(authServiceEmpty, authService, "Logout failed");

    }

    @Test
    @DisplayName("Failure to logout a User")
    public void logoutFailTest() throws DataAccessException {

        //Create the following like the test does
        UserService userService = new UserService();
        AuthService authService = new AuthService();
        AuthService authServiceEmpty = new AuthService();

        //Create data
        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");
        AuthData authData = new AuthData("myToken", "adam");

        //Register the person and then login
        userService.register(userData);
        userService.verifyPassword(userData);
        authService.getAuth(authData.userName());

        // Now log out the wrong person
        UserData userData2 = new UserData("Kevin", "AdamIsAwesome", "coolio.email.com");

        authService.logout(userData2.username());

        //Check to see if both Authservice's databases's is empty
        Assertions.assertNotEquals(authServiceEmpty, authService, "Logout happened when it shouldn't have");

    }
}
