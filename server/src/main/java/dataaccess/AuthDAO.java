package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void addAuth(AuthData data) throws DataAccessException;

    AuthData getAuth(int id) throws DataAccessException;

    void deleteAuth(int id) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}
