package app.WebSocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import util.LogFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomWebSocketCreator implements WebSocketCreator {


    public CustomWebSocketCreator() {
    }

    @Override
    public CustomWebSocket createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        int sessionID = (request.getSession().getAttribute("id") != null) ? (int)request.getSession().getAttribute("id") : 0;
        if (sessionID == 0) {
            LogFactory.getInstance().getSessionLogger().debug("Util.WebChatSocketCreator/createWebSocket Not Auth");
            return null;
        } else {
            return new CustomWebSocket(sessionID);
        }
    }

}
