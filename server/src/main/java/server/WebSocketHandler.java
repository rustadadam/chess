package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashMap;

// This file just sends messages back and forth. It only needs to update the game in database

@WebSocket
public class WebSocketHandler {

    private final HashMap<Integer, ConnectionManager> connections = new HashMap<>();
    private final HashMap<Integer, Boolean> isGameFinished = new HashMap<>();


    private DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
    private DatabaseGameDAO databaseGameDAO = new DatabaseGameDAO();

    public WebSocketHandler() throws DataAccessException {
    }

    //private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        //Check valid auth
        try {
            databaseAuthDAO.getUserFromAuth(action.getAuthToken());

            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getGameID(), action.getAuthToken(), session);
                case MAKE_MOVE -> makeMove(message, session);
                case LEAVE -> exit(action.getGameID(), action.getAuthToken());
                case RESIGN -> resign(action.getGameID(), action.getAuthToken());
            }

        } catch (Exception e) {
            ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            errorMsg.setErrorMessage("Error: Invalid Auth");
            GameData game = databaseGameDAO.getGame(action.getGameID());
            //getConnection(action.getGameID()).reportToUser(game.blackUsername(), errorMsg);
            getConnection(action.getGameID()).reportToUser(game.whiteUsername(), errorMsg);
        }

    }

    public ConnectionManager getConnection(Integer gameID) {
        if (!connections.containsKey(gameID)) {
            connections.put(gameID, new ConnectionManager());
            isGameFinished.put(gameID, false);
        }

        return connections.get(gameID);
    }

    private void makeMove(String message, Session session) throws Exception {
        //Hydrate class
        MakeMoveCommand action = new Gson().fromJson(message, MakeMoveCommand.class);
        String userName = databaseAuthDAO.getUserFromAuth(action.getAuthToken()).replace("@", "");
        GameData gameData = databaseGameDAO.getGame(action.getGameID());

        //Check if its there move
        if (userName.equalsIgnoreCase(gameData.whiteUsername())) {
            if (gameData.game().getTeamTurn() != ChessGame.TeamColor.WHITE) {
                ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
                errorMsg.setErrorMessage("Error: You are white but its blacks turn");
                getConnection(action.getGameID()).reportToUser(userName, errorMsg);
            }
        } else if (userName.equalsIgnoreCase(gameData.blackUsername())) {
            if (gameData.game().getTeamTurn() != ChessGame.TeamColor.BLACK) {
                ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
                errorMsg.setErrorMessage("Error: You are black but its white's turn");
                getConnection(action.getGameID()).reportToUser(userName, errorMsg);
            }
        } else if (!userName.equalsIgnoreCase(gameData.blackUsername()) &&
                !userName.equalsIgnoreCase(gameData.whiteUsername())) {
            ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            errorMsg.setErrorMessage("Error: You are observer and can't make moves!");
            getConnection(action.getGameID()).reportToUser(userName, errorMsg);
        } else if (isGameFinished.get(action.getGameID())) {
            ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            errorMsg.setErrorMessage("Error: Can't make move when game is finished.");
            getConnection(action.getGameID()).reportToUser(userName, errorMsg);
        } else {

            //Actually Make the move!
            try {
                databaseGameDAO.makeGameMove(action.getGameID(), action.move);

                var send_msg = String.format("%s has made the moved %s to %s", userName,
                        action.move.getStartPosition(), action.move.getEndPosition());

                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, send_msg);
                getConnection(action.getGameID()).broadcast(userName, notification);

                //Load Game
                var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null);
                GameData game = databaseGameDAO.getGame(action.getGameID());
                loadGame.addGame(game);
                getConnection(action.getGameID()).broadcast("", loadGame);

                //Send checkmate messages
                isInCheck(action.getGameID(), ChessGame.TeamColor.BLACK);
                isInCheck(action.getGameID(), ChessGame.TeamColor.WHITE);

            } catch (Exception e) {
                ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
                errorMsg.setErrorMessage("Illegal Move");
                getConnection(action.getGameID()).reportToUser(userName, errorMsg);
            }
        }
    }

    private void exit(Integer gameID, String auth) throws DataAccessException {
        String userName = databaseAuthDAO.getUserFromAuth(auth).replace("@", "");

        //We apparently need to have them leave so others can join
        GameData game = databaseGameDAO.getGame(gameID);
        if (userName.equalsIgnoreCase(game.whiteUsername())) {
            databaseGameDAO.addPlayerToGameData(gameID, null, true);
        } else if (userName.equalsIgnoreCase(game.blackUsername())) {
            databaseGameDAO.addPlayerToGameData(gameID, null, false);
        }


        getConnection(gameID).remove(userName);
        var message = String.format("%s left the game", userName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        getConnection(gameID).broadcast(userName, notification);
    }

    private void resign(Integer gameID, String auth) throws DataAccessException {
        String userName = databaseAuthDAO.getUserFromAuth(auth).replace("@", "");

        //Check to make sure game isn't over
        if (isGameFinished.get(gameID)) {
            String errMsg = "Game is already finished";
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
            notification.setErrorMessage(errMsg);
            getConnection(gameID).reportToUser(userName, notification);
        } else {

            // check the observer didn't resing
            GameData game = databaseGameDAO.getGame(gameID);
            if (userName.equalsIgnoreCase(game.whiteUsername()) || userName.equalsIgnoreCase(game.blackUsername())) {
                var message = String.format("%s admitted a crushing defeat", userName);
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                getConnection(gameID).broadcast("", notification);

                //Mark game as finished
                isGameFinished.put(gameID, true);
            } else {
                String errMsg = "You can't resign as an observer";
                var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null);
                notification.setErrorMessage(errMsg);
                getConnection(gameID).reportToUser(userName, notification);
            }
        }
    }

    public void connect(Integer gameID, String auth, Session session) throws Exception {
        try {
            String userName = databaseAuthDAO.getUserFromAuth(auth).replace("@", "");
            GameData game = databaseGameDAO.getGame(gameID);

            //Add connection
            getConnection(gameID).add(userName, session);

            String message;

            if (userName.equalsIgnoreCase(game.blackUsername())) {
                message = String.format("%s joined game %s as a player as color black", userName, game.gameName());
            } else if (userName.equalsIgnoreCase(game.whiteUsername())) {
                message = String.format("%s joined game %s as a player white", userName, game.gameName());
            } else {
                message = String.format("%s joined game %s as an observer", userName, game.gameName());
            }

            //Send join game
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            getConnection(gameID).broadcast(userName, notification);

            //Send game
            var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null);
            loadGame.addGame(game);
            getConnection(gameID).reportToUser(userName, loadGame);

        } catch (Exception ex) {
            throw new Exception("Connection error with websocket");
        }
    }

    private void isInCheck(Integer gameID, ChessGame.TeamColor color) throws DataAccessException {
        String SET_BG_COLOR_RED = "\u001b" + "[48;5;" + "160m";
        String SET_TEXT_COLOR_WHITE = "\u001b" + "[38;5;" + "15m";
        String SET_TEXT_COLOR_RED = "\u001b" + "[38;5;" + "160m";
        String RESET_BG_COLOR = "\u001b" + "[49m";
        String RESET_TEXT_COLOR = "\u001b" + "[39m";

        GameData gameData = databaseGameDAO.getGame(gameID);
        ChessGame game = gameData.game();

        String playerName;
        if (color == ChessGame.TeamColor.BLACK) {
            playerName = gameData.blackUsername();
        } else {
            playerName = gameData.whiteUsername();
        }


        if (game.isInCheck(color)) {
            String send_msg;
            //Check if in checkmate
            if (game.isInCheckmate(color)) {
                send_msg = SET_BG_COLOR_RED + SET_TEXT_COLOR_WHITE + playerName.toUpperCase() +
                        "\n----------------------\n HAVE BEEN CHECKMATED!\n----------------------\n"
                        + RESET_BG_COLOR + SET_TEXT_COLOR_RED;
                send_msg += playerName + " please Resign by typing resign\n" + RESET_TEXT_COLOR;
            } else {
                send_msg = SET_TEXT_COLOR_RED + playerName.toUpperCase() +
                        " is in check! Quick, your king is in Danger!\n" + RESET_TEXT_COLOR;
            }

            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, send_msg);
            getConnection(gameID).broadcast("", notification);
        }
    }
}