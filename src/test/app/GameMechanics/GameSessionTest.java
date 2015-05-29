package app.GameMechanics;

import StubClasses.MockSocketService;
import TestSetups.TestsCore;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.hibernate.Session;
import org.json.JSONObject;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;

import static org.junit.Assert.*;

public class GameSessionTest extends TestsCore {
    DBService dbService = new DBServiceImpl(sessionFactory);

    private Player getPlayer(int id) {
        Session session = dbService.getSession();
        Player player = new Player(dbService.getUserService(session).getUserById(id));
        session.close();
        return player;
    }

    private GameSession createNewGameSession() {
        WebSocketService webSocketService = new MockSocketService();
        GameSession gameSession = new GameSession(getPlayer(1), getPlayer(2), 1, webSocketService, dbService);
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
        json.put("firstHealth", 20);
        json.put("secondHealth", 20);
        json.put("cardAmount", 15);
        json.put("firstName", "admin");
        json.put("secondName", "blabla"); //можно оставить как основу мира что первые два юзера у нас должны быть
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
        json.put("firstHealth", 20);
        json.put("secondHealth", 15);
        json.put("cardAmount", 14);
        json.put("firstName", "admin");
        json.put("secondName", "blabla");
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

    @Test
    public void testDraw() throws Exception {
        GameSession gameSession = createNewGameSession();
        gameSession.doGameAction(putCardOnTable(1), 1);
        gameSession.doGameAction(putCardOnTable(1), 2);
        JSONObject json = new JSONObject();
        json.put("firstHealth", 20);
        json.put("secondHealth", 20);
        json.put("cardAmount", 14);
        json.put("firstName", "admin");
        json.put("secondName", "blabla");
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
        json.put("firstHealth", 20);
        json.put("secondHealth", 0);
        json.put("cardAmount", 11);
        json.put("firstName", "admin");
        json.put("secondName", "blabla");
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }


}