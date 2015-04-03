package app.GameMechanics;

import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import util.LogFactory;
import util.RPS;

import java.util.HashMap;

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
        if (json.has("gameAction")) {
            switch (json.getString("gameAction")) {
                case "setAction": {
                    //todo Убедиться в том, что это строка либо камень, либо ножницы, либо бумага.
                    setGameAction(playerNumber, json.get("chosen_action").toString());
                    break;
                }
                default: {
                    LogFactory.getInstance().getMainLogger().error("Wrong Json action from user");
                }
            }
        } else {
            LogFactory.getInstance().getSessionLogger().debug("GameMechanics.GameSession/doGameAction: invalid json");
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
//        HashMap<String,HashMap<String,Integer>> rules = new HashMap<String,HashMap<String,Integer>>(){{
//            put("scissors",new HashMap<String,Integer>(){{
//                put("scissors",DRAW);
//                put("rock",SECOND_WON);
//                put("paper",FIRST_WON);
//            }});
//            put("rock",new HashMap<String,Integer>(){{
//                put("scissors",FIRST_WON);
//                put("rock",DRAW);
//                put("paper",SECOND_WON);
//            }});
//            put("paper",new HashMap<String,Integer>(){{
//                put("scissors",SECOND_WON);
//                put("rock",FIRST_WON);
//                put("paper",DRAW);
//            }});
//        }};
        try {
            RPS.RPSResult result = RPS.Palm.fromString(firstPlayerAction).fight(RPS.Palm.fromString(secondPlayerAction));
            webSocketService.notifyGameOver(firstPlayer, secondPlayer, result);
        } catch (Exception e) {
            LogFactory.getInstance().getGameLogger().error("GameMechanics.GameSession/gameActionReveal: Wrong game_action from json! ",e);
        }

//          todo решить какой вариант предпочительнее: то, что закомментированно или выше
//
//        HashMap<String,HashMap<String,Integer>> rules = new HashMap<String,HashMap<String,Integer>>(){{
//            put(ActionList.SCISSORS,new HashMap<String,Integer>(){{
//                put(ActionList.SCISSORS, DRAW);
//                put(ActionList.ROCK, SECOND_WON);
//                put(ActionList.PAPER, FIRST_WON);
//            }});
//            put(ActionList.ROCK,new HashMap<String,Integer>(){{
//                put(ActionList.SCISSORS, FIRST_WON);
//                put(ActionList.ROCK, DRAW);
//                put(ActionList.PAPER, SECOND_WON);
//            }});
//            put(ActionList.PAPER,new HashMap<String,Integer>(){{
//                put(ActionList.SCISSORS, SECOND_WON);
//                put(ActionList.ROCK, FIRST_WON);
//                put(ActionList.PAPER, DRAW);
//            }});
//        }};

//        todo Или этот

//        switch (firstPlayerAction) {
//            case "scissors" : {
//                switch (secondPlayerAction) {
//                    case "scissors" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
//                        break;
//                    }
//                    case "rock" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, SECOND_WON);
//                        break;
//                    }
//                    case "paper" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, FIRST_WON);
//                        break;
//                    }
//                    default: {
//                        LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
//                        break;
//                    }
//                }
//                break;
//            }
//            case "rock" : {
//                switch (secondPlayerAction) {
//                    case "scissors" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, FIRST_WON);
//                        break;
//                    }
//                    case "rock" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
//                        break;
//                    }
//                    case "paper" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, SECOND_WON);
//                        break;
//                    }
//                    default: {
//                        LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
//                        break;
//                    }
//                }
//                break;
//            }
//            case "paper" : {
//                switch (secondPlayerAction) {
//                    case "scissors" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, SECOND_WON);
//                        break;
//                    }
//                    case "rock" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, FIRST_WON);
//                        break;
//                    }
//                    case "paper" : {
//                        webSocketService.notifyGameOver(firstPlayer, secondPlayer, DRAW);
//                        break;
//                    }
//                    default: {
//                        LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
//                        break;
//                    }
//                }
//                break;
//            }
//            default: {
//                LogFactory.getInstance().getGameLogger().error("Wrong game_action from json");
//            }
//        }
    }

}