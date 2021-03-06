package app.WebSocket;

import DAO.logic.CardLogic;
import app.AccountMap.AccountMap;
import app.Admin.Card;
import app.GameMechanics.Player;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBService;
import sun.rmi.runtime.Log;
import util.LogFactory;
import util.RPS;
import util.StepEffect;

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
        accountMap = new AccountMap();
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
        notify(secondPlayer, generateResponseNewGame(secondPlayer, firstPlayer.getUsername(), gameId, firstPlayer.getUserID()), gameId);
        notify(firstPlayer, generateResponseNewGame(firstPlayer, secondPlayer.getUsername(), gameId, secondPlayer.getUserID()), gameId);
    }

    private JSONObject generateResponseNewGame(Player player, String opponentName, int gameId, int userID) {
        JSONObject response = new JSONObject();
        response.put("action", "new_game");
        response.put("gameId", gameId);
        response.put("opponentName", opponentName);
        org.hibernate.Session session = dbService.getSession();
        try {
            List<CardLogic> deck = dbService.getCardService(session).getUserDeck(player.getUserID());
            JSONArray array = new JSONArray();
            for (CardLogic card : deck) {
                array.put(card.getId());
            }
            response.put("deck", array);
        } finally {
            dbService.closeSession(session);
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
    public void notifyActionSet(Player playerSetter, Player playerObserver, int cardID) {
        JSONObject response;
        for(int i = 0; i < 2; i++) {
            HashSet<CustomWebSocket> userSockets;
            if (i == 0) {
                response = generateResponseActionSet(true, cardID);
                userSockets = userWebSockets.get(playerSetter.getUserID());
            } else {
                response = generateResponseActionSet(false, 0);
                userSockets = userWebSockets.get(playerObserver.getUserID());
            }
            sendJson(userSockets, response);
        }
    }

    private JSONObject generateResponseActionSet(boolean status, int cardID){
        JSONObject response = new JSONObject();
        response.put("action", "game_action_set");
        response.put("isSetter", status);
        if (status) {
            response.put("cardNumber", cardID);
        }
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
            JSONArray cardJSONArray = new JSONArray();
            for (CardLogic card : dbService.getCardService(session).getAllCardsInfo()){
                cardJSONArray.put(card.putAllCardInformation());
            }
            json.put("cards", cardJSONArray);
            sendJson(userWebSockets.get(userID), json);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("error in greeting user");
        } finally {
            dbService.closeSession(session);
        }
    }

    public void notifyGameState(Player firstPlayer, Player secondPlayer) {
        JSONObject json = generateGameStateJson(firstPlayer, secondPlayer);
        sendJson(userWebSockets.get(firstPlayer.getUserID()), json);
        json = generateGameStateJson(secondPlayer, firstPlayer);
        sendJson(userWebSockets.get(secondPlayer.getUserID()), json);
    }

    private JSONObject generateGameStateJson(Player player, Player opponent) {
        JSONObject json = new JSONObject();
        json.put("action", "newGameState");
        json.put("player", formPlayerJSON(player));
        json.put("opponent", formPlayerJSON(opponent));
        return json;
    }

    private JSONObject formPlayerJSON(Player player) {
        JSONObject playerJSON = new JSONObject();
        playerJSON.put("name", player.getUsername());
        playerJSON.put("health", player.getHealth());
        Set<JSONObject> effectSet = new HashSet<>();
        for (StepEffect e : player.getEffectList()) {
            effectSet.add(e.getDescription());
        }
        playerJSON.put("effectList", effectSet);
        return playerJSON;
    }

    public void notifyReconnectPossibility(int userID) {
        JSONObject json = new JSONObject();
        json.put("action", "reconnect");
        json.put("message", "You can reconnect to game");
        HashSet<CustomWebSocket> sockets = userWebSockets.get(userID);
        sendJson(sockets, json);
    }

    public void notifyReconnect(JSONObject gameState, List<Integer> deck, int userID) {
        gameState.put("action", "currentGameState");
        gameState.put("deck", deck);
        HashSet<CustomWebSocket> sockets = userWebSockets.get(userID);
        sendJson(sockets, gameState);
    }

    public void notifyBadDeck(int userID) {
        JSONObject json = new JSONObject();
        json.put("action", "deckBuilder");
        json.put("result", "wrong");
        sendJson(userWebSockets.get(userID), json);
    }

    public void notifyGoodDeck(int userID) {
        JSONObject json = new JSONObject();
        json.put("action", "deckBuilder");
        json.put("result", "ok");
        sendJson(userWebSockets.get(userID), json);
    }

    public void getDeck(int userID) {
        org.hibernate.Session dbSession = dbService.getSession();
        List<CardLogic> cardList = dbService.getCardService(dbSession).getUserDeck(userID);
        List<CardLogic> allCardsInfo = dbService.getCardService(dbSession).getAllCardsInfo();
        dbSession.close();
        JSONObject json = new JSONObject();
        json.put("action", "getDeck");
        JSONArray cardJSONArrayKnight = new JSONArray();
        JSONArray cardJSONArrayLady = new JSONArray();
        JSONArray cardJSONArrayDragon = new JSONArray();
        for (CardLogic userCard: cardList) {
            for (CardLogic card : allCardsInfo) {
                if (userCard.getId() == card.getId()) {
                    if (card.getCardType().equals("knight")) {
                        cardJSONArrayKnight.put(card.putAllCardInformation());
                    } else if (card.getCardType().equals("lady")) {
                        cardJSONArrayLady.put(card.putAllCardInformation());
                    } else if (card.getCardType().equals("dragon")) {
                        cardJSONArrayDragon.put(card.putAllCardInformation());
                    }
                }
            }
        }
        JSONObject deckJSON = new JSONObject();
        deckJSON.put("dragon", cardJSONArrayDragon);
        deckJSON.put("knight", cardJSONArrayKnight);
        deckJSON.put("lady", cardJSONArrayLady);
        json.put("deck", deckJSON);

        JSONArray cardJSONArray = new JSONArray();
        for (CardLogic card : allCardsInfo){
            cardJSONArray.put(card.putAllCardInformation());
        }

        json.put("cardList", cardJSONArray);
        sendJson(userWebSockets.get(userID), json);
    }

}
