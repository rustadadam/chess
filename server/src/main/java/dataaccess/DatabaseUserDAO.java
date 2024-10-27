package dataaccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO {

    public DatabaseUserDAO() throws DataAccessException {
        // On init establish database
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  UserData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }

    public void addUser(UserData data) throws DataAccessException {

    }

    public UserData getUser(String id) throws DataAccessException {
        return null;
    }

    public void deleteAllUser() throws DataAccessException {
        var statement = "TRUNCATE UserData";
        executeUpdate(statement);
    }
}
