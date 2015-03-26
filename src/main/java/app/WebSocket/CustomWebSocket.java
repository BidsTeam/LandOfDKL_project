package app.WebSocket;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.GameMechanics.GameFactory;
import app.WebSocket.MessageSystem.WebChat;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;
import util.LogFactory;

import java.util.Set;


@WebSocket
public class CustomWebSocket {
    private Session session;
    private int userID;
    private UserLogic user;
    private WebChat webChat = WebChat.getChatInstance();
    private AccountMap cache = AccountMap.getInstance();
    private int gameID;


    public CustomWebSocket(int ID) {
        userID = ID;
        gameID = 0;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            LogFactory.getInstance().getSessionLogger().debug("WebChatSocket/onMessage: " + data);
            JSONObject request = new JSONObject(data);

            switch (request.getString("action")) {
                case "public_message": {
                    request.put("author", user.getUsername());
                    webChat.sendMessage(request);
                    break;
                }

                case "private_message": {
                    UserLogic receiver = Factory.getInstance().getUserDAO()
                            .getUserByUsername(request.getString("receiverName"));
                    webChat.sendPrivateMessage(request, receiver.getId());
                    break;
                }

                case "find_game" : {
                    GameFactory.getInstance().FindGameLobby(user);
                    break;
                }
                default: {
                    LogFactory.getInstance().getApiLogger().debug("Wrong json in socket");
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal("WebChatSocket/onMessage",e);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        try {
            cache.putNewSession(userID, session);
            user = cache.getUser(userID);
            LogFactory.getInstance().getSessionLogger().debug("WebSocket.CustomWebSocket/onOpen: " + user.getUsername());
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal("WebSocket.CustomWebSocket/onOpen: ", e);
        }
    }


    @OnWebSocketError
    public void onError(Throwable cause) {
        LogFactory.getInstance().getSessionLogger().fatal("WebSocket.CustomWebSocket/onError: ", cause);
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
    }

    public void setGameID(int gameID) { this.gameID = gameID; }
    public int getGameID() { return gameID; }
}
