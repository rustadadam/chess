package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws DataAccessException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    try {
                        c.send(notification.toString());
                    } catch (IOException e) {
                        throw new DataAccessException("Failed to broadcast");
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void reportToUser(String sendName, ServerMessage notification) throws DataAccessException {
        for (var c : connections.values()) {
            if (c.visitorName.equals(sendName) && c.session.isOpen()) {
                try {
                    c.send(notification.toString());
                } catch (IOException e) {
                    throw new DataAccessException("Failed to broadcast");
                }
            }
        }
    }
}

