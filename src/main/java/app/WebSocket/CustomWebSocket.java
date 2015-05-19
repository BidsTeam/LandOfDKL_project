package app.WebSocket;

import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.GameMechanics.GameFactory;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;

import java.util.Set;


@WebSocket
public class CustomWebSocket {
    private Session session;
    private int userID;
    private UserLogic user;
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
            LogFactory.getInstance().getLogger(this.getClass()).debug("WebChatSocket/onMessage: " + data);
            JSONObject request = new JSONObject(data);

            switch (request.getString("action")) {
                case "publicMessage": {
                    request.put("author", user.getUsername());
                    webSocketService.sendPublicMessage(request);
                    break;
                }

                case "privateMessage": {
                    org.hibernate.Session session = webSocketService.getDbService().getSession();
                    UserLogic receiver = null;
                    try {
                        receiver = webSocketService.getDbService().getUserService(session).
                                getUserByUsername(request.getString("receiverName"));
                    } finally {
                        session.close();
                    }
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
                        LogFactory.getInstance().getLogger(this.getClass()).info("Try to make game move while not in game");
                    } else {
                        GameFactory.getInstance().getGameSession(gameID).doGameAction(request, userID);
                        break;
                    }
                }
                default: {
                    LogFactory.getInstance().getLogger(this.getClass()).debug("Wrong json in socket");
                    break;
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).fatal("WebChatSocket/onMessage",e);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        try {
            webSocketService.putNewSocket(userID, this);
            user = cache.getUser(userID);
            LogFactory.getInstance().getLogger(this.getClass()).debug("WebSocket.CustomWebSocket/onOpen: " + user.getUsername());
            webSocketService.greetUser(userID);
            webSocketService.notifyUserEnter(userID);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).fatal("WebSocket.CustomWebSocket/onOpen: ", e);
        }
    }


    @OnWebSocketError
    public void onError(Throwable cause) {
        webSocketService.removeSocket(userID, this);
        LogFactory.getInstance().getLogger(this.getClass()).fatal("WebSocket.CustomWebSocket/onError: ", cause);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        webSocketService.removeSocket(userID, this);
        webSocketService.notifyUserExit(userID);
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
