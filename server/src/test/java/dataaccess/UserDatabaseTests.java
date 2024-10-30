package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDatabaseTests extends MyTests {

    @Test
    @DisplayName("Fail Add user test")
    public void addUserFailTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();

        //User Data
        UserData data = new UserData("adam", "kansa", "eggs");

        user.addUser(data);
        Assertions.assertThrows(DataAccessException.class, () -> user.addUser(data));
    }

    @Test
    @DisplayName("Successful Add user test")
    public void addUserSuccessTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();

        //User Data
        UserData data = new UserData("adam", "kansa", "eggs");

        user.addUser(data);
        UserData newData = user.getUser("adam");
        Assertions.assertNotNull(newData);
    }

    @Test
    @DisplayName("Successful get user test")
    public void getUserSuccessTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();

        //User Data
        UserData data = new UserData("adam", "National", "eggs");
        user.addUser(data);

        UserData newData = user.getUser("adam");
        Assertions.assertEquals(newData.username(), data.username());
    }

    @Test
    @DisplayName("Failure get user test")
    public void getUserFailTest() throws DataAccessException {

        //Create the following like the test does
        DatabaseUserDAO user = new DatabaseUserDAO();

        //User Data
        UserData data = new UserData("adam", "National", "eggs");
        user.addUser(data);

        Assertions.assertNull(user.getUser("LargeMan"));
    }
}
