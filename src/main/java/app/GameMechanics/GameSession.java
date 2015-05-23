package app.GameMechanics;

import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.RPS;

import java.util.Set;

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
        Session session = dbService.getSession();
        try {
            firstPlayer.setUserDeck(dbService.getCardService(session).getUserDeck(dbService.getUserService(session).getUserById(firstPlayer.getUserID())));
            secondPlayer.setUserDeck(dbService.getCardService(session).getUserDeck(dbService.getUserService(session).getUserById(secondPlayer.getUserID())));
        } finally {
            dbService.closeSession(session);
        }
        gameID = id;
        firstPlayerCard = null;
        secondPlayerCard = null;
        this.dbService = dbService;
        this.webSocketService = webSocketService;
        this.webSocketService.notifyNewGame(firstPlayer, secondPlayer, gameID);
        this.webSocketService.notifyGameState(firstPlayer, secondPlayer, firstPlayer.getHealth(), secondPlayer.getHealth());
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
        Session session = dbService.getSession();
        try{
            if (playerNumber == 1) {
                if (firstPlayerCard == null) {
                    int realCardID = firstPlayer.getCard(cardID);
                    if (realCardID != -1) {
                        firstPlayerCard = dbService.getCardService(session).getCard(realCardID);
                        webSocketService.notifyActionSet(firstPlayer, secondPlayer);
                    }
                } else {
                    LogFactory.getInstance().getLogger(this.getClass()).error("Try for selecting action second time");
                }
            } else if (playerNumber == 2) {
                if (secondPlayerCard == null) {
                    int realCardID = secondPlayer.getCard(cardID);
                    if (realCardID != -1) {
                        secondPlayerCard = dbService.getCardService(session).getCard(realCardID);
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
        }finally {
            dbService.closeSession(session);
        }


    }

    private void gameActionReveal() {
        webSocketService.notifyActionsReveal(firstPlayer, firstPlayerCard.getId(), secondPlayer, secondPlayerCard.getId());
        try {
            RPS.RPSResult result = RPS.Palm.fromString(firstPlayerCard.getCardType())
                    .fight(RPS.Palm.fromString(secondPlayerCard.getCardType()));
            if (result == RPS.RPSResult.FIRST_WON) {
                damageCalc(firstPlayer, secondPlayer, firstPlayerCard, secondPlayerCard, result);
            } else if (result == RPS.RPSResult.SECOND_WON) {
                damageCalc(secondPlayer, firstPlayer, secondPlayerCard, firstPlayerCard, result);
            } else if (result == RPS.RPSResult.DRAW) {
                webSocketService.notifyGameState(firstPlayer, secondPlayer, firstPlayer.getHealth(), secondPlayer.getHealth());
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

    //todo Не нравится разделение на winner и winnerCard: 1)Лишние почти дублирующие свойства 2) Можно легко перепутать порядок
    private void damageCalc(Player winner, Player loser, CardLogic winnerCard, CardLogic loserCard, RPS.RPSResult result){
        int damage = winnerCard.getAttack();
        damage += effectCalc(winnerCard.getEffects());
        damage += effectCalc(loserCard.getEffects());
        System.out.println(damage);
        if (!loser.takeDamage(damage)){
            webSocketService.notifyGameOver(winner, loser, result);
        } else {
            System.out.println(loser.getHealth());
            webSocketService.notifyGameState(winner, loser, winner.getHealth(), loser.getHealth());
        }
    }

    private int effectCalc(Set<EffectLogic> effectLogicSet){
        int damage = 0;
        for (EffectLogic e : effectLogicSet){
            if (e.getName().equals("poison")){ //todo Когда будет много эффектов, вынести в отдельные классы и супер класс
                damage = e.getValue();
            }
        }
        return damage;
    }

    private void concede(int playerNumber) {
        if (playerNumber == 1) {
            webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.SECOND_WON);
        } else if (playerNumber == 2) {
            webSocketService.notifyGameOver(firstPlayer, secondPlayer, RPS.RPSResult.FIRST_WON);
        }
    }

    public JSONObject reportGameState() {
        JSONObject json = new JSONObject();
        json.put("firstHealth", firstPlayer.getHealth());
        json.put("secondHealth", secondPlayer.getHealth());
        json.put("cardAmount", cardCount);
        json.put("firstName", firstPlayer.getUsername());
        json.put("secondName", secondPlayer.getUsername());
        return json;
    }

    private JSONObject reportOneSideGameState(int number) {
        JSONObject json = new JSONObject();
        if (number == 1) {
            json.put("yourHealth", firstPlayer.getHealth());
            json.put("opponentHealth", secondPlayer.getHealth());
        } else if (number == 2) {
            json.put("yourHealth", secondPlayer.getHealth());
            json.put("opponentHealth", firstPlayer.getHealth());
        }
        json.put("cardAmount", cardCount);
        json.put("firstName", firstPlayer.getUsername());
        json.put("secondName", secondPlayer.getUsername());
        return json;
    }

    public void reconnect(int userID) {
        if (userID == firstPlayer.getUserID()) {
            webSocketService.notifyReconnect(reportOneSideGameState(1), firstPlayer.getUserDeck(), userID);
        } else if (userID == secondPlayer.getUserID()) {
            webSocketService.notifyReconnect(reportOneSideGameState(2), secondPlayer.getUserDeck(), userID);
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).error("Trying to reconnect to wrong game from user " + userID);
        }
    }
}