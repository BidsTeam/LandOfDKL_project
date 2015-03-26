package app.GameMechanics;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import sun.rmi.runtime.Log;
import util.LogFactory;

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
        this.webSocketService = webSocketService;
        this.webSocketService.notifyNewGame(firstPlayer, secondPlayer, gameID);
        this.webSocketService.notifyGameOver(firstPlayer, secondPlayer, true);
    }

    public void doGameAction(JSONObject json, int userID) {
        Player actionPlayer;
        if (userID == firstPlayer.getUserID()) {
            actionPlayer = firstPlayer;
        } else if (userID == secondPlayer.getUserID()) {
            actionPlayer = secondPlayer;
        } else {
            LogFactory.getInstance().getMainLogger().error("Error on comparasion of player of game and socket user");
        }
        switch (json.getString("game_action")) {
            default: {
                LogFactory.getInstance().getMainLogger().error("Wrong Json action from user");
            }
        }
    }

    private void setGameAction(int playerNumber, String action) {
        if (playerNumber == 1) {
            if (firstPlayerAction == null) {
                firstPlayerAction = action;
                webSocketService.notifyActionSet(firstPlayer, secondPlayer);
            } else {
                LogFactory.getInstance().getGameLogger().error("Try for selecting action second time");
            }
        } else if (playerNumber == 2) {
            if (secondPlayerAction == null) {
                secondPlayerAction = action;
                webSocketService.notifyActionSet(secondPlayer, firstPlayer);
            } else {
                LogFactory.getInstance().getGameLogger().error("Try for selecting action second time");
            }
        } else {
            LogFactory.getInstance().getGameLogger().error("really strange error in setGameAction()");
        }


    }

    private void gameActionReveal() {
        webSocketService.notifyActionsReveal(firstPlayer, firstPlayerAction, secondPlayer, secondPlayerAction);
        switch (firstPlayerAction) {
            case "scissors" : {
                break;
            }
            case "rock" : {
                break;
            }
            case "paper" : {
                break;
            }
            default: {
                LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
            }
        }
    }

}