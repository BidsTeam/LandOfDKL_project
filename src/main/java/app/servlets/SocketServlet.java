package app.servlets;

import app.util.WebChatSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by andreybondar on 16.03.15.
 */
@WebServlet(name = "WebSocketServlet", urlPatterns = {"/chat"})
public class SocketServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new WebChatSocketCreator());
    }

}
