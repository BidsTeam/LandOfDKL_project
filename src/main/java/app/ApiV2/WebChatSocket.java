package app.ApiV2;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;
import util.LogFactory;

import java.lang.reflect.Method;
import java.net.HttpCookie;
import java.util.List;
import java.util.Set;


@WebSocket
public class WebChatSocket {
    private Set<WebChatSocket> users;
    private Session session;
    private int userID;
    private UserLogic user;
    WebChat webChat = WebChat.getChatInstance();
    AccountMap cache = AccountMap.getInstance();

    public WebChatSocket(Set<WebChatSocket> users, int ID) {
        System.out.println("test");
        userID = ID;
        this.users = users;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            LogFactory.getInstance().getSessionLogger().debug("WebChatSocket/onMessage: "+data);
            JSONObject message = new JSONObject(data);
            message.put("author", user.getUsername());
            if (message.getInt("status") == 1) {
                UserLogic receiver = Factory.getInstance().getUserDAO()
                        .getUserByUsername(message.getString("receiverName"));

                webChat.sendPrivateMessage(message, receiver.getId());
            } else {
                webChat.sendMessage(message);
            }
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal("WebChatSocket/onMessage",e);
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
            LogFactory.getInstance().getSessionLogger().fatal("WebChatSocket/onOpen", e);
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
        cache.removeSession(userID, session);
        users.remove(this);
    }
}
