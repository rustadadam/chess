package dataaccess;

import model.UserData;
import model.UserData;

public interface UserDAO {

    void addUser(UserData data) throws DataAccessException;

    UserData getUser(String id) throws DataAccessException;

    void deleteUser(String id) throws DataAccessException;

    void deleteAllUser() throws DataAccessException;

}
