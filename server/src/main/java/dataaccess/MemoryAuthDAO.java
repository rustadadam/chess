package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<Integer, AuthData> authTable = new HashMap<>();

    public void addAuth(AuthData data) {
        int id = data.getUserID();

        authTable.put(id, data);
    }

    public AuthData getAuth(int id) {
        return authTable.get(id);
    }

    public void deleteAuth(int id) {
        authTable.remove(id);
    }

    public void deleteAllAuth() {
        authTable.clear();
    }

}
