package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseAuthDAO implements AuthDAO {

    public DatabaseAuthDAO() throws DataAccessException { //public DatabaseUserDAO()
        // On init establish database
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX (`authToken`),
              INDEX (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }

    public void addAuth(AuthData data) throws DataAccessException {
        //Create our query for the table
        String statement = "INSERT INTO AuthData (username, authToken) VALUES (?, ?)";

        //Add it to the table
        DatabaseManager.executeUpdate(statement, data.username(), data.username(), data.authToken());
    }

    public String getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM AuthData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs).authToken();
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    public String getUserFromAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs).username();
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    public void deleteAuth(String username) throws DataAccessException {
        DatabaseManager.executeUpdate("DELETE FROM GameData WHERE username=" + username);
    }

    public void deleteAllAuth() throws DataAccessException {
        var statement = "TRUNCATE AuthData";
        DatabaseManager.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        //Rebuild the user data
        String username = rs.getString("username");
        String authToken = rs.getString("email");

        return new AuthData(authToken, username);
    }
}
