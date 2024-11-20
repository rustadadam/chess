package server;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
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
        switch (action.getCommandType()) {
            case CONNECT -> connect(action.getGameID(), action.getAuthToken(), session);
            case MAKE_MOVE -> makeMove(message, session);
            case LEAVE -> exit(action.getGameID(), action.getAuthToken());
            case RESIGN -> resign(action.getGameID(), action.getAuthToken());
        }
    }

    public ConnectionManager getConnection(Integer gameID) {
        if (!connections.containsKey(gameID)) {
            connections.put(gameID, new ConnectionManager());
            isGameFinished.put(gameID, false);
        }

        return connections.get(gameID);
    }

    private void makeMove(String message, Session session) throws DataAccessException {
        //Hydrate class
        MakeMoveCommand action = new Gson().fromJson(message, MakeMoveCommand.class);
        String userName = databaseAuthDAO.getUserFromAuth(action.getAuthToken());
        getConnection(action.getGameID()).add(userName, session);

        if (isGameFinished.get(action.getGameID())) {
            ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    "Game is finished");
            getConnection(action.getGameID()).reportToUser(userName, errorMsg);
        } else {

            //Actually Make the move!
            try {
                databaseGameDAO.makeGameMove(action.getGameID(), action.move);

                var send_msg = String.format("%s has made the moved %s to %s", userName,
                        action.move.getStartPosition(), action.move.getEndPosition());

                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, send_msg);
                getConnection(action.getGameID()).broadcast(userName, notification);

            } catch (InvalidMoveException e) {
                ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Illegal Move");
                getConnection(action.getGameID()).reportToUser(userName, errorMsg);
            }
        }
    }

    private void exit(Integer gameID, String auth) throws DataAccessException {
        String userName = databaseAuthDAO.getUserFromAuth(auth);

        getConnection(gameID).remove(userName);
        var message = String.format("%s left the game", userName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        getConnection(gameID).broadcast(userName, notification);
    }

    private void resign(Integer gameID, String auth) throws DataAccessException {
        String userName = databaseAuthDAO.getUserFromAuth(auth);

        var message = String.format("%s admitted a crushing defeat", userName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        getConnection(gameID).broadcast(userName, notification);

        //Mark game as finished
        isGameFinished.put(gameID, true);
    }

    public GameData connect(Integer gameID, String auth, Session session) throws Exception {
        try {
            String userName = databaseAuthDAO.getUserFromAuth(auth).replace("@", "");
            GameData game = databaseGameDAO.getGame(gameID);

            //Add connection
            getConnection(gameID).add(userName, session);

            var message = String.format("%s joined game %s", userName, gameID);

            //Send join game
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            getConnection(gameID).broadcast("", notification);

            //Send game
            var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "");
            loadGame.addGame(game);
            getConnection(gameID).reportToUser(userName, notification);

            return game;

        } catch (Exception ex) {
            throw new Exception("Connection error with websocket");
        }
    }
}