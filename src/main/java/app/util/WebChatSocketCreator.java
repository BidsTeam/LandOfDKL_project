package app.util;

import app.ApiV2.WebChatSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by andreybondar on 16.03.15.
 */
public class WebChatSocketCreator implements WebSocketCreator {

    private Set<WebChatSocket> users;

    public WebChatSocketCreator() {
        this.users = Collections.newSetFromMap(new ConcurrentHashMap<WebChatSocket, Boolean>());
    }

    private static String getSessionId(List<HttpCookie> cookieList) {
        for(HttpCookie c : cookieList) {
            if (c.getName().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
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
            return new WebChatSocket(users, sessionID);
        }
    }

}
