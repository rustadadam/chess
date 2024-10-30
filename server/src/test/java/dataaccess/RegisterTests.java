package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

public class RegisterTests extends MyTests {

    @Test
    @DisplayName("Successfully Register a new User")
    public void successRegisterTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        UserData userData = new UserData("adam", "AdamIsAwesome", "coolio.email.com");


        AuthData authData = new AuthData("woagnsd", "adam");
        auth.addAuth(authData);

        //Register the person so we can login them in
        user.addUser(userData);

        //Check the username
        Assertions.assertNotNull(auth.getAuth("adam"));

        // Check we have an Auth Token
        Assertions.assertNotNull(user.getUser("adam"));
    }

    @Test
    @DisplayName("Failure to register a User")
    public void registerFailureTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();

        UserData userData = new UserData("adam", null, "coolio.email.com");

        //Register the person so we can login them in
        Assertions.assertThrows(DataAccessException.class, () -> user.addUser(userData));

        UserData userData2 = new UserData("adam", "heyall", null);

        //Register the person so we can login them in
        Assertions.assertThrows(DataAccessException.class, () -> user.addUser(userData2));


    }
}
