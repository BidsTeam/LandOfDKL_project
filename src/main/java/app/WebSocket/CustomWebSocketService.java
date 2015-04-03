package app.WebSocket;

import app.GameMechanics.Player;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.RPS;

import java.util.HashMap;
import java.util.HashSet;


public class CustomWebSocketService implements WebSocketService {
    private HashMap<Integer, HashSet<CustomWebSocket>> userWebSockets;
    private static final Integer WINNER = 1;
    private static final Integer LOSER = -1;
    private static final Integer DRAW = 0;
    private DBService dbService;

    public CustomWebSocketService(DBService dbService) {
        userWebSockets = new HashMap<>();
        this.dbService = dbService;
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
            LogFactory.getInstance().getLogger(this.getClass()).fatal(e);
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
            LogFactory.getInstance().getLogger(this.getClass()).fatal(e);
        }
    }

    public void notifyNewGame(Player firstPlayer, Player secondPlayer, int gameId) {
        notify(secondPlayer, generateResponseNewGame(firstPlayer.getUsername(), gameId), gameId);
        notify(firstPlayer, generateResponseNewGame(secondPlayer.getUsername(),gameId), gameId);
    }

    private JSONObject generateResponseNewGame(String opponentName,int gameId){
        JSONObject response = new JSONObject();
        response.put("action", "new_game");
        response.put("gameId", gameId);
        response.put("opponentName", opponentName);
        return response;
    }

    private void notify(Player player,JSONObject response,int gameId){
        HashSet<CustomWebSocket> userSocket = userWebSockets.get(player.getUserID());
        sendJson(userSocket, response);
        setGameID(userSocket, gameId);
    }



    public void notifyGameOver(Player firstPlayer, Player secondPlayer, RPS.RPSResult winner) {
        JSONObject firstPlayerResponse;
        JSONObject secondPlayerResponse;

        if (winner == RPS.RPSResult.FIRST_WON){
            firstPlayerResponse  = generateResponseGameOver(firstPlayer,WINNER);
            secondPlayerResponse = generateResponseGameOver(secondPlayer,LOSER);
        } else if (winner == RPS.RPSResult.SECOND_WON){
            firstPlayerResponse  = generateResponseGameOver(firstPlayer,LOSER);
            secondPlayerResponse = generateResponseGameOver(secondPlayer,WINNER);
        } else {
            firstPlayerResponse  = generateResponseGameOver(firstPlayer,DRAW);
            secondPlayerResponse = generateResponseGameOver(secondPlayer,DRAW);
        }
        notify(firstPlayer, firstPlayerResponse, 0);
        notify(secondPlayer, secondPlayerResponse, 0);
    }

    private JSONObject generateResponseGameOver(Player player,int status){
        JSONObject response = new JSONObject();
        response.put("action", "game_over");
        response.put("gameResult", status);
        return response;
    }

    //Уведомляет о том, что игрок сделал ход. playerSetter: тот кто сделал, playerObserver, тот кого ждут
    public void notifyActionSet(Player playerSetter, Player playerObserver) {
        JSONObject response;
        for(int i = 0; i < 2; i++) {
            HashSet<CustomWebSocket> userSockets;
            if (i == 0) {
                response = generateResponseActionSet(true);
                userSockets = userWebSockets.get(playerSetter.getUserID());
            } else {
                response = generateResponseActionSet(false);
                userSockets = userWebSockets.get(playerObserver.getUserID());
            }
            sendJson(userSockets, response);
        }
    }

    private JSONObject generateResponseActionSet(boolean status){
        JSONObject response = new JSONObject();
        response.put("action", "game_action_set");
        response.put("isSetter", status);
        return response;
    }

    public void notifyActionsReveal(Player firstPlayer, String firstAction, Player secondPlayer, String secondAction) {
        JSONObject response;
        HashSet<CustomWebSocket> userSockets;
        for(int i = 0; i < 2; i++) {
            if (i == 0) {
                response = generateResponseActionReveal(firstAction,secondAction);
                userSockets = userWebSockets.get(firstPlayer.getUserID());
            } else {
                response = generateResponseActionReveal(secondAction,firstAction);
                userSockets = userWebSockets.get(secondPlayer.getUserID());
            }
            sendJson(userSockets, response);
        }
    }

    private JSONObject generateResponseActionReveal(String userAction,String opponentAction){
        JSONObject response = new JSONObject();
        response.put("action", "game_action_reveal");
        response.put("userAction", userAction);
        response.put("opponentAction", opponentAction);
        return response;
    }

    public DBService getDbService(){
        return dbService;
    }
}
