package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;


public class UserService {

    private UserDAO dataAccess;

    public UserService() {
        this.dataAccess = new MemoryUserDAO();
    }

    public void deleteAllUsers() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllUsers();
    }
//    public AuthData register(UserData user) {
//    }
//
//    public AuthData login(UserData user) {
//    }


    public void logout(AuthData auth) {
    }
}
