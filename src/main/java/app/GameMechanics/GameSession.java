package app.GameMechanics;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import util.LogFactory;
import util.RPS;

import java.util.HashMap;

public class GameSession {
    private Player firstPlayer;
    private Player secondPlayer;
    private int gameID;
    private WebSocketService webSocketService;
    private String firstPlayerAction;
    private String secondPlayerAction;

    public GameSession(Player playerOne, Player playerTwo, int id, WebSocketService webSocketService) {
        firstPlayer = playerOne;
        secondPlayer = playerTwo;
        gameID = id;
        firstPlayerAction = null;
        secondPlayerAction = null;
        this.webSocketService = webSocketService;
        this.webSocketService.notifyNewGame(firstPlayer, secondPlayer, gameID);
    }

    public void doGameAction(JSONObject json, int userID) {
        int playerNumber = 0;
        if (userID == firstPlayer.getUserID()) {
            //firstPlayerAction = json.get("game_action").toString();
            playerNumber = 1;
        } else if (userID == secondPlayer.getUserID()) {
            //secondPlayerAction = json.get("game_action").toString();
            playerNumber = 2;
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).error("Error on comparasion of player of game and socket user");
        }
        if (json.has("gameAction")) {
            switch (json.getString("gameAction")) {
                case "setAction": {
                    //todo Убедиться в том, что это строка либо камень, либо ножницы, либо бумага.
                    setGameAction(playerNumber, json.get("chosen_action").toString());
                    break;
                }
                default: {
                    LogFactory.getInstance().getLogger(this.getClass()).error("Wrong Json action from user");
                }
            }
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).debug("GameMechanics.GameSession/doGameAction: invalid json");
        }
    }

    private void setGameAction(int playerNumber, String action) {
        if (playerNumber == 1) {
            if (firstPlayerAction == null) {
                firstPlayerAction = action;
                webSocketService.notifyActionSet(firstPlayer, secondPlayer);
            } else {
                LogFactory.getInstance().getLogger(this.getClass()).error("Try for selecting action second time");
            }
        } else if (playerNumber == 2) {
            if (secondPlayerAction == null) {
                secondPlayerAction = action;
                webSocketService.notifyActionSet(secondPlayer, firstPlayer);
            } else {
                LogFactory.getInstance().getLogger(this.getClass()).error("Try for selecting action second time");
            }
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).error("really strange error in setGameAction()");
        }
        if (firstPlayerAction != null && secondPlayerAction != null) {
            gameActionReveal();
        }

    }

    private void gameActionReveal() {
        webSocketService.notifyActionsReveal(firstPlayer, firstPlayerAction, secondPlayer, secondPlayerAction);
        try {
            RPS.RPSResult result = RPS.Palm.fromString(firstPlayerAction).fight(RPS.Palm.fromString(secondPlayerAction));
            webSocketService.notifyGameOver(firstPlayer, secondPlayer, result);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("GameMechanics.GameSession/gameActionReveal: Wrong game_action from json! ",e);
        }

    }

}