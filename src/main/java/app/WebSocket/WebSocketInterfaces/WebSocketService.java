package app.WebSocket.WebSocketInterfaces;

import app.AccountMap.AccountMap;
import app.GameMechanics.Player;
import app.WebSocket.CustomWebSocket;
import org.json.JSONObject;
import service.DBService;
import util.RPS;

import java.util.HashSet;
import java.util.List;


public interface WebSocketService {
    public void sendPublicMessage(JSONObject json);
    public void sendPrivateMessage(JSONObject json, int receiverID);
    public void putNewSocket(int userID, CustomWebSocket webSocket);
    public void removeSocket(int userID, CustomWebSocket webSocket);
    public void sendJson(HashSet<CustomWebSocket> userSockets, JSONObject json);
    public void notifyNewGame(Player firstPlayer, Player secondPlayer, int gameID);
    public void notifyGameOver(Player firstPlayer, Player secondPlayer, RPS.RPSResult winner);
    public void notifyActionSet(Player playerSetter, Player playerObserver);
    public void notifyActionsReveal(Player firstPlayer, int firstCardID, Player secondPlayer, int secondCardID);
    public void notifyUserEnter(int userID);
    public void notifyUserExit(int userID);
    public DBService getDbService();
    public void greetUser(int userID);
    public void notifyGameState(Player firstPlayer, Player secondPlayer);
    public void notifyReconnectPossibility(int userID);
    public void notifyReconnect(JSONObject gameState, List<Integer> deck, int userID);
    public void notifyBadDeck(int userID);
    public void notifyGoodDeck(int userID);
    public void getDeck(int userID);
}
