package dataaccess;

import model.UserData;

public interface UserDAO {

    void insertUser(UserData u) throws DataAccessException;

    UserData findUserByName(String name);              // Find a user by ID

    void updateUser(User user);             // Update an existing user

    void deleteUser(int id);                // Delete a user by ID
}
