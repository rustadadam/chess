package ui;


import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

//import org.eclipse.jetty.websocket.api.Session;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String auth, Integer gameID) throws Exception {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void makeMove(String auth, Integer gameID, ChessMove move) throws Exception {
        try {
            var action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    auth, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void leaveGame(String auth, Integer gameID) throws Exception {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void resign(String auth, Integer gameID) throws Exception {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

}

