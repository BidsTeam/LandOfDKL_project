package app.WebSocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomWebSocketCreator implements WebSocketCreator {

    private Set<CustomWebSocket> users;

    public CustomWebSocketCreator() {
        this.users = Collections.newSetFromMap(new ConcurrentHashMap<CustomWebSocket, Boolean>());
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        int sessionID = 0;
        try {
            sessionID = (int) request.getSession().getAttribute("id");
        } catch (Exception e)
        {
            sessionID = 0;
        }
        if (sessionID == 0) {
            System.out.println("not auth");
            return null;
        } else {
            return new CustomWebSocket(users, sessionID);
        }
    }

}
