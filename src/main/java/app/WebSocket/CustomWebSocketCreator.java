package app.WebSocket;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import util.LogFactory;


public class CustomWebSocketCreator implements WebSocketCreator {

    private WebSocketService webSocketService;

    public CustomWebSocketCreator(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public CustomWebSocket createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        int sessionID = (request.getSession().getAttribute("id") != null) ? (int)request.getSession().getAttribute("id") : 0;
        if (sessionID == 0) {
            LogFactory.getInstance().getLogger(this.getClass()).debug("Util.WebChatSocketCreator/createWebSocket Not Auth");
            return null;
        } else {
            return new CustomWebSocket(sessionID, webSocketService);
        }
    }

}
