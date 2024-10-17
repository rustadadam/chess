package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void addAuth(AuthData data) throws DataAccessException;

    String getAuth(String id) throws DataAccessException;

    String getUserFromAuth(String id) throws DataAccessException;

    void deleteAuth(String id) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}
