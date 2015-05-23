package app.servlets;

import app.GameMechanics.GameFactory;
import app.WebSocket.CustomWebSocketCreator;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import service.DBService;

import javax.servlet.annotation.WebServlet;


@WebServlet(name = "WebSocketServlet", urlPatterns = {"/socket"})
public class SocketServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private WebSocketService webSocketService;
    private GameFactory gameFactory;

    public SocketServlet(WebSocketService webSocketService, GameFactory gameFactory) {
        this.webSocketService = webSocketService;
        this.gameFactory = gameFactory;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new CustomWebSocketCreator(webSocketService, gameFactory));
    }

}
