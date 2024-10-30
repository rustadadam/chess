package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

public class LoginTests extends MyTests {

    @Test
    @DisplayName("Succesfully login a User")
    public void loginSucessTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Create data -- We wrap it so it works like a request
        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");

        //Register the person so we can login them in
        user.addUser(userData);

        //Add auth
        AuthData authData = new AuthData("woagnsd", "adam");
        auth.addAuth(authData);

        String authToken = auth.getAuth("adam");

        // Check we have an Auth Token
        Assertions.assertFalse(authToken.equals(null), "Auth token is null");

    }

    @Test
    @DisplayName("Failure to login a User")
    public void loginFailureTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Create data
        UserData userData = new UserData("john", "AdamIsAwesome", "coolio.email.com");

        //Register the person so we can login them in
        user.addUser(userData);

        //Add auth
        AuthData authData = new AuthData("woagnsd", "john");
        auth.addAuth(authData);

        String authToken = auth.getAuth("kevin");
        Assertions.assertNull(authToken, "Auth token is null");

        //Give wrong password
        Assertions.assertNotEquals(auth.getAuth("john"), auth.getAuth("kevin"));
    }
}
