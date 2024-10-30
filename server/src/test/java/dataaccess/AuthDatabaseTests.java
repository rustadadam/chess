package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthDatabaseTests extends MyTests {

    @Test
    @DisplayName("Fail Add auth test")
    public void addAuthFailTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData(null, "Kansas");
        Assertions.assertThrows(DataAccessException.class, () -> auth.addAuth(authData));
    }

    @Test
    @DisplayName("Succesful Add auth test")
    public void addAuthSuccessTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData("146985", "Kansas");
        try {
            auth.addAuth(authData);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Get User from auth Sucess")
    public void getUserSuccess() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData("111111", "Kansas");

        auth.addAuth(authData);

        String username = auth.getUserFromAuth("111111");

        Assertions.assertNotNull(username);
        Assertions.assertEquals("Kansas", username);
    }

    @Test
    @DisplayName("Get User from auth Fail")
    public void getUserFail() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData("111111", "Kansas");

        auth.addAuth(authData);

        Assertions.assertThrows(DataAccessException.class, () -> auth.getUserFromAuth("11132411"));
    }

    @Test
    @DisplayName("Delete Auth Test")
    public void deleteAuthSuccess() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData("111111", "Kansas");
        auth.addAuth(authData);

        auth.deleteAuth("Kansas");
        Assertions.assertThrows(DataAccessException.class, () -> auth.getUserFromAuth("111111"));
    }

    @Test
    @DisplayName("Delete Auth Test Fail")
    public void deleteAuthFail() throws DataAccessException {

        //Create the following like the test does
        DatabaseAuthDAO auth = new DatabaseAuthDAO();

        //Add auth
        AuthData authData = new AuthData("111111", "Kansas");
        auth.addAuth(authData);

        auth.deleteAuth(null);
        Assertions.assertNotNull(auth.getUserFromAuth("111111"));
    }
}
