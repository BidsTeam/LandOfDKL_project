package app.WebSocket.WebSocketInterfaces;

import app.GameMechanics.Player;
import app.WebSocket.CustomWebSocket;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * Created by andreybondar on 26.03.15.
 */
public interface WebSocketService {
    public void putNewSocket(int userID, CustomWebSocket webSocket);
    public void removeSocket(int userID, CustomWebSocket webSocket);
    public void sendJson(HashSet<CustomWebSocket> userSockets, JSONObject json);
    public void notifyNewGame(Player firstPlayer, Player secondPlayer, int gameID);
    public void notifyGameOver(Player firstPlayer, Player secondPlayer, boolean isFirstWon);
//    public void sendPublicMessage();
//    public void sendPrivateMessage();
}
