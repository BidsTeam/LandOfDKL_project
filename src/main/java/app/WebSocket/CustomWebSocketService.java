package app.WebSocket;

import app.AccountMap.AccountMap;
import app.GameMechanics.Player;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBService;
import sun.rmi.runtime.Log;
import util.LogFactory;
import util.RPS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CustomWebSocketService implements WebSocketService {
    private HashMap<Integer, HashSet<CustomWebSocket>> userWebSockets;
    private AccountMap accountMap;
    private HashSet<String> onlineUsers;
    private static final Integer WINNER = 1;
    private static final Integer LOSER = -1;
    private static final Integer DRAW = 0;
    private DBService dbService;

    public CustomWebSocketService(DBService dbService) {
        userWebSockets = new HashMap<>();
        onlineUsers = new HashSet<>();
        accountMap = AccountMap.getInstance();
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
        notify(secondPlayer, generateResponseNewGame(firstPlayer.getUsername(), gameId, firstPlayer.getUserID()), gameId);
        notify(firstPlayer, generateResponseNewGame(secondPlayer.getUsername(), gameId, secondPlayer.getUserID()), gameId);
    }

    private JSONObject generateResponseNewGame(String opponentName, int gameId, int userID) {
        JSONObject response = new JSONObject();
        response.put("action", "new_game");
        response.put("gameId", gameId);
        response.put("opponentName", opponentName);
        org.hibernate.Session session = dbService.getSession();
        try {
            response.put("deck", dbService.getCardService(session).getUserDeck(dbService.getUserService(session).getUserById(userID)));
        } finally {
            session.close();
        }
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

    public void notifyActionsReveal(Player firstPlayer, int firstCardID, Player secondPlayer, int secondCardID) {
        JSONObject response;
        HashSet<CustomWebSocket> userSockets;
        for(int i = 0; i < 2; i++) {
            if (i == 0) {
                response = generateResponseActionReveal(firstCardID, secondCardID);
                userSockets = userWebSockets.get(firstPlayer.getUserID());
            } else {
                response = generateResponseActionReveal(secondCardID, firstCardID);
                userSockets = userWebSockets.get(secondPlayer.getUserID());
            }
            sendJson(userSockets, response);
        }
    }

    private JSONObject generateResponseActionReveal(int userCard, int opponentCard) {
        JSONObject response = new JSONObject();
        response.put("action", "gameCardsReveal");
        response.put("userCard", userCard);
        response.put("opponentCard", opponentCard);
        return response;
    }

    public DBService getDbService(){
        return dbService;
    }
    public void sendPublicMessage(JSONObject json) {
        JSONObject response = new JSONObject();
        response.put("action", "public_message");
        JSONObject responseBody = new JSONObject();
        try {
            responseBody.put("author", json.get("author").toString());
            responseBody.put("message", json.get("message").toString());
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("WebChat/sendMessage",e);
            responseBody.put("author", "err");
            responseBody.put("message", "err");

        }
        response.put("body", responseBody);

        sendPublicJson(response);

    }

    private void sendPublicJson(JSONObject json) {
        String jsonResp = json.toString();
        try {
            for (HashSet<CustomWebSocket> userConnections : userWebSockets.values()) {
                for (CustomWebSocket socket : userConnections) {
                    socket.getSession().getRemote().sendString(jsonResp);
                }
            }
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).fatal(e);
        }
    }

    public void sendPrivateMessage(JSONObject json, int receiverID) {

        JSONObject responseBody = new JSONObject();
        responseBody.put("author",json.get("author"));
        responseBody.put("message", json.get("message"));

        JSONObject response = new JSONObject();
        response.put("action", "private_message");
        response.put("body", responseBody);
        //String jsonResp = response.toString();

        HashSet<CustomWebSocket> userSockets = userWebSockets.get(receiverID);
        sendJson(userSockets, response);
    }

    public void notifyUserEnter(int userID) {
        onlineUsers.add(accountMap.getUser(userID).getUsername());
        notifyUpdateChatUsers();
    }

    public void notifyUserExit(int userID) {
        if (userWebSockets.get(userID).isEmpty()) {
            onlineUsers.remove(accountMap.getUser(userID).getUsername());
            notifyUpdateChatUsers();
        }
    }

    private void notifyUpdateChatUsers() {
        JSONObject json = new JSONObject();

        try {
            //JSONArray jsonArray = new JSONArray();
            //jsonArray.put(usernames);
            json.put("usernames", onlineUsers);
            json.put("action", "newChatUsers");
            sendPublicJson(json);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("Error in notifyUpdateChatUsers");
        }
    }

    public void greetUser(int userID) {
        JSONObject json = new JSONObject();
        org.hibernate.Session session = dbService.getSession();
        try {
            json.put("action", "hello");
            json.put("cards", dbService.getCardService(session).getAllCardsInfo());
            sendJson(userWebSockets.get(userID), json);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("error in greeting user");
        } finally {
            session.close();
        }
    }

    public void notifyGameState(Player firstPlayer, Player secondPlayer, int firstPlayerHealth, int secondPlayerHealth) {
        JSONObject json = generateGameStateJson(firstPlayerHealth, secondPlayerHealth);
        sendJson(userWebSockets.get(firstPlayer.getUserID()), json);
        json = generateGameStateJson(secondPlayerHealth, firstPlayerHealth);
        sendJson(userWebSockets.get(secondPlayer.getUserID()), json);
    }

    private JSONObject generateGameStateJson(int playerHealth, int opponentHealth) {
        JSONObject json = new JSONObject();
        json.put("action", "newGameState");
        json.put("playerHealth", playerHealth);
        json.put("opponentHealth", opponentHealth);
        return json;
    }

    public void notifyReconnectPossibility(int userID) {
        JSONObject json = new JSONObject();
        json.put("action", "reconnect");
        json.put("message", "You can reconnect to game");
        HashSet<CustomWebSocket> sockets = userWebSockets.get(userID);
        sendJson(sockets, json);
    }

    public void notifyReconnect(JSONObject gameState, List<Integer> deck, int userID) {
        org.hibernate.Session session = dbService.getSession();
        gameState.put("action", "currentGameState");
        gameState.put("deck", deck);
        HashSet<CustomWebSocket> sockets = userWebSockets.get(userID);
        sendJson(sockets, gameState);
    }
}
