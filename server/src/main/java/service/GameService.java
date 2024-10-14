package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import spark.Request;

import java.util.Collection;
import java.util.HashMap;

public class GameService {

    private GameDAO dataAccess;
    private int gameID;

    public GameService() {
        this.gameID = 0;
        this.dataAccess = new MemoryGameDAO();
    }


    public Collection<GameData> getGames() throws DataAccessException {
        HashMap<Integer, GameData> games = dataAccess.getGames();
        //Create an empty collection
        Collection<GameData> gamesCollection = games.values();

        return gamesCollection;
    }

    public GameData createGame(Request req) throws DataAccessException {
        String gameName = req.queryParams("gameName");

        //See if game already exists -- I Don't think I need to do that
//        GameData game = dataAccess.getGame(gameName);
//
//        if (game != null) {
//            throw new DataAccessException("Game Exists");
//        } //Check how to do Error

        GameData new_game = new GameData(gameID++, null, null, gameName, new ChessGame());

        dataAccess.addGame(new_game);

        return new_game;
    }

    public GameData joinGame(Request req) throws DataAccessException {
        String playerColor = req.queryParams("playerColor");
        String userName = req.queryParams("userName");
        int joinGameID = Integer.parseInt(req.queryParams("gameID"));

        //See if game already exists
        GameData game = dataAccess.getGame(joinGameID);

        if (game == null) {
            throw new DataAccessException("Game Exists"); //Check this
        }

        if (playerColor == null) {
            throw new DataAccessException("Player Color is null"); //NOTE, spectator mode?
        } else if (playerColor.equals("white")) {
            if (game.whiteUserName() == null) {
                //Add player to game. :)
                dataAccess.addPlayerToGameData(game.gameID(), userName, true);
            } else {
                throw new DataAccessException("There is already white player");
            }
        } else {
            if (game.blackUserName() == null) {
                dataAccess.addPlayerToGameData(game.gameID(), userName, false);
            } else {
                throw new DataAccessException("There is already black player");
            }
        }

        return game;
    }

    public void deleteAllGame() throws DataAccessException {
        //Delete all Auth in the memory
        dataAccess.deleteAllGame();
    }
}