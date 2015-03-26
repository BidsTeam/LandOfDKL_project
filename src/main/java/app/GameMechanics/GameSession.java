package app.GameMechanics;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import sun.rmi.runtime.Log;
import util.LogFactory;

public class GameSession {
    private static final int DRAW = 0;
    private static final int FIRST_WON = 1;
    private static final int SECOND_WON = 2;
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
        //this.webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
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
            LogFactory.getInstance().getMainLogger().error("Error on comparasion of player of game and socket user");
        }
        switch (json.getString("game_action")) {
            case "set_action" : {
                setGameAction(playerNumber, json.get("chosen_action").toString());
                break;
            }
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
        if (firstPlayerAction != null & secondPlayerAction != null) {
            gameActionReveal();
        }

    }

    private void gameActionReveal() {
        webSocketService.notifyActionsReveal(firstPlayer, firstPlayerAction, secondPlayer, secondPlayerAction);
        switch (firstPlayerAction) {
            case "scissors" : {
                switch (secondPlayerAction) {
                    case "scissors" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
                        break;
                    }
                    case "rock" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, SECOND_WON);
                        break;
                    }
                    case "paper" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, FIRST_WON);
                        break;
                    }
                    default: {
                        LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
                    }
                }
                break;
            }
            case "rock" : {
                switch (secondPlayerAction) {
                    case "scissors" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, FIRST_WON);
                        break;
                    }
                    case "rock" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
                        break;
                    }
                    case "paper" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, SECOND_WON);
                        break;
                    }
                    default: {
                        LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
                    }
                }
                break;
            }
            case "paper" : {
                switch (secondPlayerAction) {
                    case "scissors" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, SECOND_WON);
                        break;
                    }
                    case "rock" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, FIRST_WON);
                        break;
                    }
                    case "paper" : {
                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
                        break;
                    }
                    default: {
                        LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
                    }
                }
                break;
            }
            default: {
                LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
            }
        }
    }

}