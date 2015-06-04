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


@WebSocket(maxIdleTime = 100000000)
public class CustomWebSocket {
    private Session session;
    private int userID;
    private UserLogic user;
    private AccountMap cache = new AccountMap();
    private int gameID;
    private WebSocketService webSocketService;
    private GameFactory gameFactory;

    public CustomWebSocket(int ID, WebSocketService webSocketService, GameFactory gameFactory) {
        userID = ID;
        gameID = 0;
        this.webSocketService = webSocketService;
        this.gameFactory = gameFactory;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            LogFactory.getInstance().getLogger(this.getClass()).debug("WebChatSocket/onMessage: " + data + "in user " + user.getUsername());
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
                        webSocketService.getDbService().closeSession(session);
                    }
                    request.put("author", user.getUsername());
                    webSocketService.sendPrivateMessage(request, receiver.getId());
                    break;
                }

                case "findGame" : {
                    gameFactory.FindGameLobby(user, webSocketService);
                    break;
                }
                case "gameAction" : {
                    if (gameID == 0) {
                        LogFactory.getInstance().getLogger(this.getClass()).info("Try to make game move while not in game");
                    } else {
                        gameFactory.getGameSession(gameID).doGameAction(request, userID);
                        break;
                    }
                }
                case "reconnect" : {
                    if (gameID != 0) {
                        gameFactory.getGameSession(gameID).reconnect(userID);
                    } else {
                        LogFactory.getInstance().getLogger(this.getClass()).error("Try to reconnect to game while not in game from user " + userID);
                    }
                    break;
                }
                case "exitQueue" : {
                    gameFactory.exitQueue(userID);
                    break;
                }
                case "newDeck" : {
                    gameFactory.buildDeck(userID, request.getJSONArray("deck"), webSocketService);
                }
                case "getDeck" : {
                    webSocketService.getDeck(userID);
                }
                case "getAllCards" : {
                    webSocketService.greetUser(userID);
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
            gameID = gameFactory.getUserGame(userID);
            if (gameID != 0) {
                webSocketService.notifyReconnectPossibility(userID);
            }
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
            gameFactory.freePlayer(userID);
        }
    }
    public int getGameID() {
        return gameID;
    }

    public int getUserID() {return  userID; }
}
