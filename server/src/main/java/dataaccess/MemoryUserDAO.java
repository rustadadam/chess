package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> userTable = new HashMap<>();

    public void addUser(UserData data) {
        String username = data.username();

        userTable.put(username, data);
    }

    public UserData getUser(String username) {
        return userTable.get(username);
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
