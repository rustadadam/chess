package dataaccess;


import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseGameDAO implements GameDAO {

    //Example Function. Can be deleted
    public static void main(String[] args) throws Exception {

        DatabaseManager.createDatabase();

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        }
    }

    public DatabaseGameDAO() throws DataAccessException { // int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        // On init establish database
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  GameData (
              `id` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game_json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }

    public void addGame(GameData data) throws DataAccessException {
        //Create our query for the table
        String statement = "INSERT INTO GameData (id, whiteUsername, blackUsername, gameName, game_json) VALUES (?, ?, ?, ?, ?)";

        var json = new Gson().toJson(data.game());

        //Add it to the table
        DatabaseManager.executeUpdate(statement, data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), json);

    }

    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    public HashMap<Integer, GameData> getGames() throws DataAccessException {
        return null;
    }

    public void addPlayerToGameData(int gameID, String userName, boolean addWhite) throws DataAccessException {

    }

    public void deleteAllGame() throws DataAccessException {

    }
}
