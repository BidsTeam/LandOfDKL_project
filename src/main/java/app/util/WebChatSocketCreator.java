package app.util;

import app.ApiV2.WebChatSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import util.LogFactory;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebChatSocketCreator implements WebSocketCreator {

    private Set<WebChatSocket> users;

    public WebChatSocketCreator() {
        this.users = Collections.newSetFromMap(new ConcurrentHashMap<WebChatSocket, Boolean>());
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
            LogFactory.getInstance().getSessionLogger().debug("Util.WebChatSocketCreator/createWebSocket Not Auth");
            return null;
        } else {
            return new WebChatSocket(users, sessionID);
        }
    }

}
