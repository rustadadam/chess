package service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.Objects;

public class AuthService {

    private AuthDAO dataAccess;
    private Integer authCounter;

    public AuthService() {
        this.dataAccess = new MemoryAuthDAO();
        this.authCounter = 1111111;
    }

    public void deleteAllAuth() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllAuth();
    }

    public AuthData getAuth(String username) throws DataAccessException {

        authCounter++;

        //Find if user exists
        String authToken = dataAccess.getAuth(username);


        //Create auth if its null
        while (authToken != null) {
            //logout(username);
            //To create extra usernames so all auths work
            username = "@" + username;
            authToken = dataAccess.getAuth(username);
        }

        AuthData withToken = new AuthData(authCounter.toString(), username);//Check to see if this exists
        dataAccess.addAuth(withToken);
        withToken = new AuthData(authCounter.toString(), username.replaceAll("@", ""));//Check to see if this exists

        return withToken;

    }

    public void logout(String user) throws DataAccessException {
        //Find if user exists -- Simply to throw error if its null
        String authToken = dataAccess.getAuth(user);

        if (authToken != null) {
            dataAccess.deleteAuth(user);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public String verifyAuth(String authToken) throws DataAccessException {
        String username = dataAccess.getUserFromAuth(authToken);

        return username.replaceAll("@", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthService that = (AuthService) o;
        return Objects.equals(dataAccess, that.dataAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataAccess);
    }
}
