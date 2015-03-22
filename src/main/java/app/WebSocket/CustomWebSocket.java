package app.WebSocket;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.AccountCache.AccountCache;
import app.WebSocket.MessageSystem.WebChat;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;

import java.util.Set;


@WebSocket
public class CustomWebSocket {
    private Set<CustomWebSocket> users;
    private Session session;
    private int userID;
    private UserLogic user;
    WebChat webChat = WebChat.getChatInstance();
    AccountCache cache = AccountCache.getInstance();

    public CustomWebSocket(Set<CustomWebSocket> users, int ID) {
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
                webChat.sendMessage(message);
            }
        } catch (Exception e) {
            //todo отправить человеку, что произошла ошибка и его сообщение не доставлено
            System.err.println(e.getMessage() + "File: " + e.getStackTrace()[2].getFileName() +" Line number: "+ e.getStackTrace()[2].getLineNumber());
            //e.printStackTrace();
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
        cache.removeSession(userID, session);
        users.remove(this);
    }
}
