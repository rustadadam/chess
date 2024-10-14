package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;


public class UserService {

    private UserDAO dataAccess;

    public UserService() {
        this.dataAccess = new MemoryUserDAO();
    }

    public void deleteAllUserData() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllUser();
    }

    public boolean verifyPassword(Request req) throws DataAccessException {
        //Get data from requests
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        // Find if username exsists
        UserData data = dataAccess.getUser(username);

        //If data doesn't exist
        if (data == null) {
            throw new DataAccessException("ERROR");
        } //Check for which new DataAccessException("ERROR");

        //Check password
        if (data.password().equals(password)) {
            return true;
        }

        return false;
    }

    public UserData register(Request req) throws DataAccessException {
        //Get data from requests
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String email = req.queryParams("email");

        // Find if username exsists
        UserData data = dataAccess.getUser(username);

        //If data does exsist
        if (data != null) {
            throw new DataAccessException("ERROR");
        }
        ; //Check for which new DataAccessException("ERROR");
        if (password == null) {
            throw new DataAccessException("ERROR");
        }
        ;
        if (email == null) {
            throw new DataAccessException("ERROR");
        }
        ;

        //Create new user data
        UserData newUser = new UserData(username, password, email);
        dataAccess.addUser(newUser);
        return newUser;
    }

//
//    public AuthData login(UserData user) {
//    }


    public void logout(AuthData auth) {
    }
}
