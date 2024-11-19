package server;

import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

// This file just sends messages back and forth. It only needs to update the game in database

@WebSocket
public class WebSocketHandler {

    private final HashMap<Integer, ConnectionManager> connections = new HashMap<>();
    //private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(action.getGameID(), action.getAuthToken());
            case MAKE_MOVE -> makeMove(message, session);
            case LEAVE -> exit(action.visitorName());
            case RESIGN -> resign(action.resign());
        }
    }

    public void addConnection(Integer gameID) {
        if (!connections.containsKey(gameID)) {
            connections.put(gameID, new ConnectionManager());
        }
    }

    private void makeMove(String message, Session session) throws IOException {
        //Hydrate class
        MakeMoveCommand action = new Gson().fromJson(message, MakeMoveCommand.class);


        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new ServerMessage(UserGameCommand.CommandType.LEAVE, message);
        connections.broadcast(visitorName, notification);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new ServerMessage(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void connect(Integer gameID, String auth) throws Exception {
        try {
            var message = String.format("%s joined game %s", userName, gameName);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new Exception("Connection error with websocket");
        }
    }
}