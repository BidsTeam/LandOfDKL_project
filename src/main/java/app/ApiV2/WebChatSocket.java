package app.ApiV2;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.AccountCache.AccountCache;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.HttpCookie;
import java.util.List;
import java.util.Set;

/**
 * Created by andreybondar on 16.03.15.
 */
@WebSocket
public class WebChatSocket {
    private Set<WebChatSocket> users;
    private Session session;
    private int userID;
    private UserLogic user;
    WebChat webChat = WebChat.getChatInstance();
    AccountCache cache = AccountCache.getInstance();

    public WebChatSocket(Set<WebChatSocket> users, int ID) {
        System.out.println("test");
        userID = ID;
        this.users = users;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            System.out.println(data);
            JSONObject message = new JSONObject(data);
            message.put("author", user.getUsername());
            if (message.getInt("status") == 1) {
                UserLogic recevier = Factory.getInstance().getUserDAO()
                        .getUserByUsername(message.getString("recevierName"));

                webChat.sendPrivateMessage(message, recevier.getId());
            } else {
                webChat.sendMessege(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        users.add(this);
        setSession(session);
        try {
            cache.putNewSession(userID, session);
            user = cache.getUser(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnWebSocketError
    public void onError(Throwable cause) {
        System.out.println("Close: statusCode=" + cause.toString());
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
