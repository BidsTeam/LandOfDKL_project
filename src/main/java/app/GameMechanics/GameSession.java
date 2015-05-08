package app.GameMechanics;

import DAO.logic.CardLogic;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.RPS;

public class GameSession {
    private Player firstPlayer;
    private Player secondPlayer;
    private int gameID;
    private WebSocketService webSocketService;
    private CardLogic firstPlayerCard;
    private CardLogic secondPlayerCard;
    private DBService dbService;
    private int cardCount;
    private final int CARD_AMOUNT = 15;

    public GameSession(Player playerOne, Player playerTwo, int id, WebSocketService webSocketService, DBService dbService) {
        firstPlayer = playerOne;
        secondPlayer = playerTwo;
        firstPlayer.setUserDeck(dbService.getCardService().getUserDeck(dbService.getUserService().getUserById(firstPlayer.getUserID())));
        secondPlayer.setUserDeck(dbService.getCardService().getUserDeck(dbService.getUserService().getUserById(secondPlayer.getUserID())));
        gameID = id;
        firstPlayerCard = null;
        secondPlayerCard = null;
        this.dbService = dbService;
        this.webSocketService = webSocketService;
        this.webSocketService.notifyNewGame(firstPlayer, secondPlayer, gameID);
        cardCount = CARD_AMOUNT;
    }

    public void doGameAction(JSONObject json, int userID) {
        int playerNumber = 0;
        if (userID == firstPlayer.getUserID()) {
            //firstPlayerCard = json.get("gameAction")
            playerNumber = 1;
        } else if (userID == secondPlayer.getUserID()) {
            //secondPlayerCard = json.get("game_action").toString();
            playerNumber = 2;
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).error("Error on comparasion of player of game and socket user");
        }
        if (json.has("gameAction")) {
            switch (json.getString("gameAction")) {
                case "setCard": {
                    //todo Убедиться в том, что это строка либо камень, либо ножницы, либо бумага.
                    setGameAction(playerNumber, json.getInt("chosenCard"));
                    break;
                }
                case "concede": {
                    concede(playerNumber);
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

    private void setGameAction(int playerNumber, int cardID) {
        if (playerNumber == 1) {
            if (firstPlayerCard == null) {
                int realCardID = firstPlayer.getCard(cardID);
                if (realCardID != -1) {
                    firstPlayerCard = dbService.getCardService().getCard(realCardID);
                    webSocketService.notifyActionSet(firstPlayer, secondPlayer);
                }
            } else {
                LogFactory.getInstance().getLogger(this.getClass()).error("Try for selecting action second time");
            }
        } else if (playerNumber == 2) {
            if (secondPlayerCard == null) {
                int realCardID = secondPlayer.getCard(cardID);
                if (realCardID != -1) {
                    secondPlayerCard = dbService.getCardService().getCard(realCardID);
                    webSocketService.notifyActionSet(secondPlayer, firstPlayer);
                }
            }
            else {
                LogFactory.getInstance().getLogger(this.getClass()).error("Try for selecting action second time");
            }
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).error("really strange error in setGameAction()");
        }
        if (firstPlayerCard != null && secondPlayerCard != null) {
            gameActionReveal();
        }

    }

    private void gameActionReveal() {
        webSocketService.notifyActionsReveal(firstPlayer, firstPlayerCard.getId(), secondPlayer, secondPlayerCard.getId());
        try {
            RPS.RPSResult result = RPS.Palm.fromString(firstPlayerCard.getCardType())
                    .fight(RPS.Palm.fromString(secondPlayerCard.getCardType()));
            if (result == RPS.RPSResult.FIRST_WON) {
                if (!secondPlayer.takeDamage(firstPlayerCard.getAttack())) {
                    webSocketService.notifyGameOver(firstPlayer, secondPlayer, result);
                } else {
                    webSocketService.notifyGameState(firstPlayer, secondPlayer, firstPlayer.getHealth(), secondPlayer.getHealth());
                }
            } else if (result == RPS.RPSResult.SECOND_WON) {
                if (!firstPlayer.takeDamage(secondPlayerCard.getAttack())) {
                    webSocketService.notifyGameOver(firstPlayer, secondPlayer, result);
                } else {
                    webSocketService.notifyGameState(firstPlayer, secondPlayer, firstPlayer.getHealth(), secondPlayer.getHealth());
                }
            }
            cardCount--;
            if (cardCount == 0) {
                if (firstPlayer.getHealth() < secondPlayer.getHealth()) {
                    webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.SECOND_WON);
                } else if (firstPlayer.getHealth() > secondPlayer.getHealth()) {
                    webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.FIRST_WON);
                } else {
                    webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.DRAW);
                }
            }
            firstPlayerCard = null;
            secondPlayerCard = null;
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("GameMechanics.GameSession/gameActionReveal: Wrong game_action from json! ",e);
        }

    }

    public void concede(int playerNumber) {
        if (playerNumber == 1) {
            webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.SECOND_WON);
        } else if (playerNumber == 2) {
            webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.FIRST_WON);
        }
    }

}