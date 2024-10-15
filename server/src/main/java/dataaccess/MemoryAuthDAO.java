package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, String> authTable = new HashMap<>();

    public void addAuth(AuthData data) {
        String token = data.authToken();
        String username = data.userName();

        authTable.put(username, token);
    }

    public String getAuth(String id) {
        return authTable.get(id);
    }

    public void deleteAuth(String id) {
        authTable.remove(id);
    }

    public void deleteAllAuth() {
        authTable.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(authTable, that.authTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authTable);
    }

}
