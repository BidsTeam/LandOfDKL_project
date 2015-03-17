package app.ApiV2;

import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by andreybondar on 16.03.15.
 */
@WebSocket
public class UserSocket {
    private Set<UserSocket> users;
    private Session session;
    private JDBCSessionManager.Session userSes;

    public UserSocket(Set<UserSocket> users) {
        this.users = users;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONObject reqString = new JSONObject(data);
        String route = reqString.get("type").toString();
        String methodName = reqString.get("method").toString();
        try {
            System.out.println("app.Api." + route.substring(0, 1).toUpperCase() + route.substring(1));
            Class<?> cls = Class.forName("app.Api." + route.substring(0, 1).toUpperCase() + route.substring(1));
            Object obj = cls.newInstance();
            Method method = cls.getMethod(methodName.toLowerCase());
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        users.add(this);
        setSession(session);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        users.remove(this);
    }
}
