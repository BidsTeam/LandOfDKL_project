package app.GameMechanics;

import DAO.logic.UserLogic;
import TestSetups.TestsCore;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.hibernate.Session;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;
import service.DBService;
import service.serviceImpl.DBServiceImpl;

import static org.junit.Assert.*;

public class GameSessionTest extends TestsCore {
    DBService dbService = new DBServiceImpl(sessionFactory);
    final int MAX_HEALTH = 20;
    final int KNIGHT_ATTACK = 5;
    final String FIRST_PLAYER_NAME = "admin";
    final String SECOND_PLAYER_NAME = "blabla";
    final int CARD_AMOUNT = 15;

    private Player getPlayer(String name, int id) {
        UserLogic user = new UserLogic(name, "12345", "aa@aa.ru");
        user.setId(id);
        Player player = new Player(user);
        return player;
    }

    private GameSession createNewGameSession() {
        WebSocketService webSocketService = Mockito.mock(WebSocketService.class);
        GameSession gameSession = new GameSession(getPlayer(FIRST_PLAYER_NAME, 1), getPlayer(SECOND_PLAYER_NAME, 2), 1, webSocketService, dbService);
        return gameSession;
    }

    private JSONObject putCardOnTable(int cardID) {
        JSONObject json = new JSONObject();
        json.put("action", "gameAction");
        json.put("gameAction", "setCard");
        json.put("chosenCard", cardID);
        return json;
    }

    @Test
    public void testGameStart() throws Exception {
        GameSession gameSession = createNewGameSession();
        JSONObject json = new JSONObject();
        json.put("firstHealth", MAX_HEALTH);
        json.put("secondHealth", MAX_HEALTH);
        json.put("cardAmount", CARD_AMOUNT);
        json.put("firstName", FIRST_PLAYER_NAME);
        json.put("secondName", SECOND_PLAYER_NAME); //можно оставить как основу мира что первые два юзера у нас должны быть
                                    //именно такими, а можно сделать по-другому, тут по желанию
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

    @Test
    public void testFightWin() throws Exception {
        GameSession gameSession = createNewGameSession();
        //По задумке в колоде первые 5 карт это - рыцарь, вторые 5 - дракон и тд, т.е. 1 карта колоды всегда побеждает 2
        gameSession.doGameAction(putCardOnTable(1), 1);
        gameSession.doGameAction(putCardOnTable(6), 2);
        JSONObject json = new JSONObject();
        json.put("firstHealth", MAX_HEALTH);
        json.put("secondHealth", MAX_HEALTH - KNIGHT_ATTACK);
        json.put("cardAmount", CARD_AMOUNT - 1);
        json.put("firstName", FIRST_PLAYER_NAME);
        json.put("secondName", SECOND_PLAYER_NAME);
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

    @Test
    public void testDraw() throws Exception {
        GameSession gameSession = createNewGameSession();
        gameSession.doGameAction(putCardOnTable(1), 1);
        gameSession.doGameAction(putCardOnTable(1), 2);
        JSONObject json = new JSONObject();
        json.put("firstHealth", MAX_HEALTH);
        json.put("secondHealth", MAX_HEALTH);
        json.put("cardAmount", CARD_AMOUNT - 1);
        json.put("firstName", FIRST_PLAYER_NAME);
        json.put("secondName", SECOND_PLAYER_NAME);
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

    @Test
    public void testGameEnd() throws Exception {
        GameSession gameSession = createNewGameSession();
        for (int i = 0; i < 4; i++) {
            gameSession.doGameAction(putCardOnTable(i), 1);
            gameSession.doGameAction(putCardOnTable(5 + i), 2);
        }
        JSONObject json = new JSONObject();
        json.put("firstHealth", MAX_HEALTH);
        json.put("secondHealth", 0);
        json.put("cardAmount", CARD_AMOUNT - 4);
        json.put("firstName", FIRST_PLAYER_NAME);
        json.put("secondName", SECOND_PLAYER_NAME);
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }


}