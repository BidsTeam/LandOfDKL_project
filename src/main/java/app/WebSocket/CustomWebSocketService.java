package app.WebSocket;

import app.GameMechanics.Player;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import util.LogFactory;

import java.util.HashMap;
import java.util.HashSet;


public class CustomWebSocketService implements WebSocketService {
    private HashMap<Integer, HashSet<CustomWebSocket>> userWebSockets;
    private static final Integer WINNER = 1;
    private static final Integer LOSER = -1;
    private static final Integer DRAW = 0;

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
        for (int i = 1; i < 3; i++){
            if (i == 1){
                response.put("opponentName", secondPlayer.getUsername());
                userSocket = userWebSockets.get(firstPlayer.getUserID());
            } else {
                response.put("opponentName", firstPlayer.getUsername());
                userSocket = userWebSockets.get(secondPlayer.getUserID());
            }
            setGameID(userSocket, gameID);
            sendJson(userSocket, response);
        }
    }


    public void notifyGameOver(Player firstPlayer, Player secondPlayer, int winner) {
        //todo скорее всего потом сделаю, передачу array player'ов. Удобнее обрабатывать
        HashSet<CustomWebSocket> userSocket = null;
        JSONObject response = new JSONObject();

        response.put("action", "game_over");
        response.put("gameResult", LOSER);

        for (int i = 1; i < 3; i++) {
            if (i == 1) {
                userSocket = userWebSockets.get(firstPlayer.getUserID());
            } else {
                userSocket = userWebSockets.get(secondPlayer.getUserID());
            }
            if (winner == i) {
                response.put("gameResult", WINNER);
            } else if (winner == 0) {
                response.put("gameResult", DRAW);
            }
            setGameID(userSocket, 0);
            sendJson(userSocket, response);
        }

    }

    //Уведомляет о том, что игрок сделал ход. playerSetter: тот кто сделал, playerObserver, тот кого ждут
    public void notifyActionSet(Player playerSetter, Player playerObserver) {
        for(int i = 0; i < 2; i++) {
            JSONObject response = new JSONObject();
            response.put("action", "game_action_set");
            HashSet<CustomWebSocket> userSockets = null;
            if (i == 0) {
                response.put("isSetter", true);
                userSockets = userWebSockets.get(playerSetter.getUserID());
            } else {
                response.put("isSetter", false);
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
                response.put("userAction", firstAction);
                response.put("opponentAction", secondAction);
                userSockets = userWebSockets.get(firstPlayer.getUserID());
            } else {
                response.put("userAction", secondAction);
                response.put("opponentAction", firstAction);
                userSockets = userWebSockets.get(secondPlayer.getUserID());
            }
            sendJson(userSockets, response);
        }
    }


}
