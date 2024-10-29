package dataaccess;


import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
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
              INDEX(`gameName`)
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
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM GameData WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    public HashMap<Integer, GameData> getGames() throws DataAccessException {
        //Create our game map
        HashMap<Integer, GameData> gamesMap = new HashMap<>();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM GameData";
            try (var ps = conn.prepareStatement(statement);
                 var rs = ps.executeQuery()) {

                int index = 1; //Start on one?
                while (rs.next()) {
                    GameData game = readGame(rs);
                    gamesMap.put(index, game);
                    index++;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Failure to retrieve games: " + e.getMessage());
        }

        return gamesMap;
    }

    public void addPlayerToGameData(int gameID, String userName, boolean addWhite) throws DataAccessException {
        //First get the game
        GameData game = getGame(gameID);

        GameData updatedGame;
        if (!addWhite) {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), userName, game.gameName(), game.game());
        } else {
            updatedGame = new GameData(game.gameID(), userName, game.blackUsername(), game.gameName(), game.game());
        }

        //Add the game back and delete the old one
        DatabaseManager.executeUpdate("DELETE FROM GameData WHERE id=" + gameID);
        addGame(updatedGame);

    }

    public void deleteAllGame() throws DataAccessException {
        var statement = "TRUNCATE GameData";
        DatabaseManager.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        Gson gson = new Gson();

        //Rebuild the user data
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        int gameID = rs.getInt("id");
        String gameName = rs.getString("gameName");
        String json = rs.getString("game_json");

        ChessGame game = gson.fromJson(json, ChessGame.class);


        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
