package app.WebSocket.WebSocketInterfaces;

import app.AccountMap.AccountMap;
import app.GameMechanics.Player;
import app.WebSocket.CustomWebSocket;
import org.json.JSONObject;

import java.util.HashSet;


public interface WebSocketService {
    public void sendPublicMessage(JSONObject json);
    public void sendPrivateMessage(JSONObject json, int receiverID);
    public void putNewSocket(int userID, CustomWebSocket webSocket);
    public void removeSocket(int userID, CustomWebSocket webSocket);
    public void sendJson(HashSet<CustomWebSocket> userSockets, JSONObject json);
    public void notifyNewGame(Player firstPlayer, Player secondPlayer, int gameID);
    public void notifyGameOver(Player firstPlayer, Player secondPlayer, int winner);
    public void notifyActionSet(Player playerSetter, Player playerObserver);
    public void notifyActionsReveal(Player firstPlayer, String firstAction, Player secondPlayer, String secondAction);
    public void notifyUserEnter(int userID);
    public void notifyUserExit(int userID);
//    public void sendPublicMessage();
//    public void sendPrivateMessage();
}
