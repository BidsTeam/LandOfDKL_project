package app.WebSocket;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.GameMechanics.GameFactory;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
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
  //  private WebChat webChat = WebChat.getChatInstance();
    private AccountMap cache = AccountMap.getInstance();
    private int gameID;
    private WebSocketService webSocketService;

    public CustomWebSocket(int ID, WebSocketService webSocketService) {
        userID = ID;
        gameID = 0;
        this.webSocketService = webSocketService;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            LogFactory.getInstance().getSessionLogger().debug("WebChatSocket/onMessage: " + data);
            JSONObject request = new JSONObject(data);

            switch (request.getString("action")) {
                case "publicMessage": {
                    request.put("author", user.getUsername());
                    webSocketService.sendPublicMessage(request);
                    break;
                }

                case "privateMessage": {
                    UserLogic receiver = Factory.getInstance().getUserDAO()
                            .getUserByUsername(request.getString("receiverName"));
                    request.put("author", user.getUsername());
                    webSocketService.sendPrivateMessage(request, receiver.getId());
                    break;
                }

                case "findGame" : {
                    GameFactory.getInstance().FindGameLobby(user, webSocketService);
                    break;
                }
                case "gameAction" : {
                    if (gameID == 0) {
                        LogFactory.getInstance().getApiLogger().info("Try to make game move while not in game");
                    } else {
                        GameFactory.getInstance().getGameSession(gameID).doGameAction(request, userID);
                        break;
                    }
                }
                default: {
                    LogFactory.getInstance().getApiLogger().debug("Wrong json in socket");
                    break;
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
            webSocketService.putNewSocket(userID, this);
            user = cache.getUser(userID);
            webSocketService.notifyUpdateChatUsers(cache);
            LogFactory.getInstance().getSessionLogger().debug("WebSocket.CustomWebSocket/onOpen: " + user.getUsername());
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal("WebSocket.CustomWebSocket/onOpen: ", e);
        }
    }


    @OnWebSocketError
    public void onError(Throwable cause) {
        webSocketService.removeSocket(userID, this);
        LogFactory.getInstance().getSessionLogger().fatal("WebSocket.CustomWebSocket/onError: ", cause);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        webSocketService.removeSocket(userID, this);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setGameID(int gameID){
        this.gameID = gameID;
        if (gameID == 0) {
            GameFactory.getInstance().freePlayer(userID);
        }
    }
    public int getGameID() {
        return gameID;
    }

    public int getUserID() {return  userID; }
}
