package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> userTable = new HashMap<>();

    public void addUser(UserData data) {
        String userName = data.username();

        userTable.put(userName, data);
    }

    public UserData getUser(String Username) {
        return userTable.get(Username);
    }

    public void deleteUser(String Username) {
        userTable.remove(Username);
    }

    public void deleteAllUser() {
        userTable.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(userTable, that.userTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userTable);
    }

}
