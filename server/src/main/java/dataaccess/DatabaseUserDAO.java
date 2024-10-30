package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO {

    public static void main(String[] args) {
        // THIS IS MERELY A DEBUGGING functions
        try {
            //Drop Database if exists
            DatabaseManager.executeUpdate("DROP TABLE IF EXISTS UserData;");

            // Initialize DatabaseUserDAO
            DatabaseUserDAO dao = new DatabaseUserDAO();

            // Add a new user
            UserData newUser = new UserData("Adam", "Hawaii", "Too@cool");
            dao.addUser(newUser);

            UserData oldUser = new UserData("Kevin", "SpamAndEggs", "Always@cool");
            dao.addUser(oldUser);

            UserData middleAgedUser = new UserData("George", "BananaPeel", "Yellow@cool");
            dao.addUser(middleAgedUser);

            //See if the things worked
            System.out.print("\n\nFirst Table\n");
            viewUsers();

            //Get a user
            UserData foundKevin = dao.getUser("Kevin");
            System.out.print("\n ---------------\n Found Kevin's information: " + foundKevin.toString() + "\n");

            //Delete all users
            dao.deleteAllUser();
            System.out.print("\n\nTable after truncations\n");
            viewUsers();


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public DatabaseUserDAO() throws DataAccessException { //public DatabaseUserDAO()
        // On init establish database
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  UserData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              UNIQUE KEY `username` (`username`),
              INDEX (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }

    public static void viewUsers() throws Exception {
        String query = "SELECT * FROM UserData";
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.createStatement(); var rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                System.out.printf("ID: %d, Username: %s, Email: %s, Password: %s%n", id, username, email, password);
            }
        } catch (DataAccessException ex) {
            throw new DataAccessException("Error viewing users: " + ex.getMessage());
        }
    }

    public void addUser(UserData data) throws DataAccessException {
        //Create our query for the table
        String statement = "INSERT INTO UserData (username, email, password) VALUES (?, ?, ?)";

        //Add it to the table
        DatabaseManager.executeUpdate(statement, data.username(), data.email(), data.password());
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM UserData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }

        return null;
    }

    public void deleteAllUser() throws DataAccessException {
        var statement = "TRUNCATE UserData";
        DatabaseManager.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        //Rebuild the user data
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");

        return new UserData(username, password, email);
    }
}
