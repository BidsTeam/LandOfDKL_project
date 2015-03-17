package app.util;

import app.ApiV2.UserSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by andreybondar on 16.03.15.
 */
public class SocketCreator implements WebSocketCreator {

    private Set<UserSocket> users;

    public SocketCreator() {
        this.users = Collections.newSetFromMap(new ConcurrentHashMap<UserSocket, Boolean>());
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        return new UserSocket(users);
    }

}
