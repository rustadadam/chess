package service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import spark.Request;

import java.util.Objects;

public class AuthService {

    private final AuthDAO dataAccess;

    public AuthService() {
        this.dataAccess = new MemoryAuthDAO();
    }

    public void deleteAllAuth() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllAuth();
    }

    public AuthData getAuth(Request req) throws DataAccessException {
        //Build out the request
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        //Find if user exists
        String authToken = dataAccess.getAuth(username);

        //Create authdata to return
        AuthData data = new AuthData(authToken, username);

        //Create auth if its null
        if (data.authToken() == null) {
            AuthData withToken = new AuthData("NewToken", data.userName()); //Check to see if this exists
            dataAccess.addAuth(withToken);
            return withToken;
        }

        return data;


    }

    public void logout(Request req) throws DataAccessException {
        String username = req.queryParams("username");
        dataAccess.deleteAuth(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthService that = (AuthService) o;
        return Objects.equals(dataAccess, that.dataAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataAccess);
    }
}
