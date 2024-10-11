package service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;

public class AuthService {

    private final AuthDAO dataAccess;

    public AuthService() {
        this.dataAccess = new MemoryAuthDAO();
    }

    public void deleteAllAuth() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllAuth();
    }
}
