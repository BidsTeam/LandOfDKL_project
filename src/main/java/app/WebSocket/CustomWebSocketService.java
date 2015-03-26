package app.WebSocket;

import app.GameMechanics.Player;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import util.LogFactory;

import java.util.HashMap;
import java.util.HashSet;


public class CustomWebSocketService implements WebSocketService {
    private HashMap<Integer, HashSet<CustomWebSocket>> userWebSockets;

    public CustomWebSocketService() {
        userWebSockets = new HashMap<>();
    }

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

    public void removeSocket(int userID, CustomWebSocket webSocket) {
        HashSet<CustomWebSocket> sockets = userWebSockets.get(userID);
        sockets.remove(webSocket);
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
        //todo скорее всего потом сделаю, передачу array player'ов. Удобнее обрабатывать
        JSONObject response = new JSONObject();
        response.put("action", "new_game");
        response.put("gameID", gameID);
        HashSet<CustomWebSocket> userSocket = null;
        for (int i = 0; i < 2; i++){
            if (i == 0){
                response.put("opponent_name", secondPlayer.getUsername());
                userSocket = userWebSockets.get(firstPlayer.getUserID());
            } else {
                response.put("opponent_name", firstPlayer.getUsername());
                userSocket = userWebSockets.get(secondPlayer.getUserID());
            }
            setGameID(userSocket, gameID);
            sendJson(userSocket, response);
        }
    }


    public void notifyGameOver(Player firstPlayer, Player secondPlayer, boolean isFirstWinner) {
        //todo скорее всего потом сделаю, передачу array player'ов. Удобнее обрабатывать
        HashSet<CustomWebSocket> userSocket = null;
        JSONObject response = new JSONObject();
        response.put("action", "game_over");
        response.put("is_winner",false);

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                if (isFirstWinner) {
                    response.put("is_winner", true);
                }
                userSocket = userWebSockets.get(firstPlayer.getUserID());

            } else {
                if (!isFirstWinner) {
                    response.put("is_winner", true);
                }
                userSocket = userWebSockets.get(secondPlayer.getUserID());
            }
            setGameID(userSocket, 0);
            sendJson(userSocket, response);
        }

    }

    public void notifyActionSet(Player playerSetter, Player playerObserver) {
        for(int i = 0; i < 2; i++) {
            JSONObject response = new JSONObject();
            response.put("action", "game_action_set");
            HashSet<CustomWebSocket> userSockets = null;
            if (i == 0) {
                response.put("is_setter", true);
                userSockets = userWebSockets.get(playerSetter.getUserID());
            } else {
                response.put("is_setter", false);
                userSockets = userWebSockets.get(playerObserver.getUserID());
            }
            sendJson(userSockets, response);
        }
    }

    public void notifyActionsReveal(Player firstPlayer, String firstAction, Player secondPlayer, String secondAction) {
        for(int i = 0; i < 2; i++) {
            JSONObject response = new JSONObject();
            response.put("action", "game_action_reveal");
            HashSet<CustomWebSocket> userSockets = null;
            if (i == 0) {
                response.put("user_action", firstAction);
                response.put("opponent_action", secondAction);
                userSockets = userWebSockets.get(firstPlayer.getUserID());
            } else {
                response.put("user_action", secondAction);
                response.put("opponent_action", firstAction);
                userSockets = userWebSockets.get(secondPlayer.getUserID());
            }
            sendJson(userSockets, response);
        }
    }


}
