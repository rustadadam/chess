package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.WrappedRequest;
import service.AuthService;
import service.UserService;

public class LogoutTests extends MyTests {

    @Test
    @DisplayName("Succesfully logout a User")
    public void logoutSucessTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData("woagnsd", "adam");
        auth.addAuth(authData);

        Assertions.assertNotNull(auth.getAuth("adam"));

        //logout
        auth.deleteAuth("adam");

        Assertions.assertNull(auth.getAuth("adam"));

    }

    @Test
    @DisplayName("Failure to logout a User")
    public void logoutFailTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData(null, "adam");
        Assertions.assertThrows(DataAccessException.class, () -> auth.addAuth(authData));

        //See that its fake!
        Assertions.assertNull(auth.getAuth("adam"));

    }
}
