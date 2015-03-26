package app.WebSocket;

import app.GameMechanics.Player;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import util.LogFactory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by andreybondar on 26.03.15.
 */
public class CustomWebSocketService implements WebSocketService {
    private HashMap<Integer, HashSet<CustomWebSocket>> userWebSockets;

    public void putNewSocket(int userID, CustomWebSocket webSocket) {
        HashSet<CustomWebSocket> webSocketsSet = userWebSockets.get(userID);
        if (webSocketsSet == null) {
            webSocketsSet = new HashSet<>();
            webSocketsSet.add(webSocket);
            userWebSockets.put(userID, webSocketsSet);
        } else {
            webSocketsSet.add(webSocket);
        }
    }

    public void sendJson(HashSet<CustomWebSocket> userSockets, JSONObject json) {
        try {
            if (!userSockets.isEmpty()){
                for (CustomWebSocket userSocket : userSockets) {
                    userSocket.getSession().getRemote().sendString(json.toString());
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal(e);
        }
    }

    public void setGameID(HashSet<CustomWebSocket> userSockets, int gameID) {


        try {
            if (!userSockets.isEmpty()){
                for (CustomWebSocket userSocket : userSockets) {
                    userSocket.setGameID(gameID);
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getSessionLogger().fatal(e);
        }
    }

    public void notifyNewGame(Player firstPlayer, Player secondPlayer, int gameID) {
        JSONObject responseForFirst = new JSONObject();
        responseForFirst.put("action", "new_game");
        responseForFirst.put("gameID", gameID);
        responseForFirst.put("opponent_name", secondPlayer.getUsername());

        JSONObject responseForSecond = new JSONObject();
        responseForSecond.put("action", "new_game");
        responseForSecond.put("gameID", gameID);
        responseForSecond.put("opponent_name", firstPlayer.getUsername());

        HashSet<CustomWebSocket> firstUserSockets = userWebSockets.get(firstPlayer.getUserID());
        HashSet<CustomWebSocket> secondUserSockets = userWebSockets.get(secondPlayer.getUserID());

        setGameID(firstUserSockets, gameID);
        setGameID(secondUserSockets, gameID);
        sendJson(firstUserSockets, responseForFirst);
        sendJson(secondUserSockets, responseForSecond);
    }



    public void notifyGameOver(Player firstPlayer, Player secondPlayer, boolean isFirstWinner) {
        JSONObject responseForFirst = new JSONObject();
        responseForFirst.put("action", "game_over");

        JSONObject responseForSecond = new JSONObject();
        responseForSecond.put("action", "game_over");

        if (isFirstWinner) {
            responseForFirst.put("is_winner", true);
            responseForSecond.put("is_winner", false);
        } else {
            responseForFirst.put("is_winner", false);
            responseForSecond.put("is_winner", true);
        }

        HashSet<CustomWebSocket> firstUserSockets = userWebSockets.get(firstPlayer.getUserID());
        HashSet<CustomWebSocket> secondUserSockets = userWebSockets.get(secondPlayer.getUserID());

        setGameID(firstUserSockets, 0);
        setGameID(secondUserSockets, 0);
        sendJson(firstUserSockets, responseForFirst);
        sendJson(secondUserSockets, responseForSecond);
    }


}
