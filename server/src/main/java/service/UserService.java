package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;

import java.util.Objects;


public class UserService {

    private UserDAO dataAccess;

    public UserService() {
        this.dataAccess = new MemoryUserDAO();
    }

    public void deleteAllUserData() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllUser();
    }

    public boolean verifyPassword(UserData userData) throws DataAccessException {
        //Get data from requests
        String username = userData.username();
        String password = userData.password();

        // Find if username exsists
        UserData data = dataAccess.getUser(username);

        //If data doesn't exist
        if (data == null) {
            throw new DataAccessException("Error: unauthorized");
        } //Check for which new DataAccessException("ERROR");

        //Check password
        if (data.password().equals(password)) {
            return true;
        }

        throw new DataAccessException("Error: unauthorized");
    }

    public void register(UserData userData) throws DataAccessException {
        // Find if username exsists
        UserData oldData = dataAccess.getUser(userData.username());

        //If data does exsist
        if (oldData != null) {
            throw new DataAccessException("Error: already taken");
        }
        ; //Check for which new DataAccessException("ERROR");
        if (userData.password() == null) {
            throw new DataAccessException("Error: bad request");
        }
        ;
        if (userData.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        ;

        //Create new user data
        dataAccess.addUser(userData);
        // return newUser;
    }

//
//    public AuthData login(UserData user) {
//    }


//    public void logout() throws DataAccessException {
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserService that = (UserService) o;
        return Objects.equals(dataAccess, that.dataAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataAccess);
    }
}
