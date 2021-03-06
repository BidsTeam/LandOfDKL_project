package app.WebSocket;

import app.GameMechanics.GameFactory;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import util.LogFactory;


public class CustomWebSocketCreator implements WebSocketCreator {

    private WebSocketService webSocketService;
    private GameFactory gameFactory;

    public CustomWebSocketCreator(WebSocketService webSocketService, GameFactory gameFactory) {
        this.webSocketService = webSocketService;
        this.gameFactory = gameFactory;
    }

    @Override
    public CustomWebSocket createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        int sessionID = 0;
        if (request.getSession() != null) {
            sessionID = (request.getSession().getAttribute("id") != null) ? (int) request.getSession().getAttribute("id") : 0;
        }
        if (sessionID == 0) {
            LogFactory.getInstance().getLogger(this.getClass()).debug("Util.WebChatSocketCreator/createWebSocket Not Auth");
            return null;
        } else {
            return new CustomWebSocket(sessionID, webSocketService, gameFactory);
        }
    }

}
