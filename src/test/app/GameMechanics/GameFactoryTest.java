package app.GameMechanics;

import DAO.logic.UserLogic;
import StubClasses.MockSocketService;
import TestSetups.TestsCore;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;

import static org.junit.Assert.*;

public class GameFactoryTest extends TestsCore {
    private DBService dbService = new DBServiceImpl(sessionFactory);
    private GameFactory gameFactory;
    private WebSocketService webSocketService = new MockSocketService();


    private GameSession createNewGame(int firstID, int secondID) {
        UserLogic userOne = dbService.getUserService(dbService.getSession()).getUserById(firstID);
        UserLogic userTwo = dbService.getUserService(dbService.getSession()).getUserById(secondID);
        gameFactory.FindGameLobby(userOne, webSocketService);
        gameFactory.FindGameLobby(userTwo, webSocketService);
        return gameFactory.getLastGame();
    }

    @Before
    public void beforeNewGame() {
        gameFactory = new GameFactory(dbService);
    }

    @Test
    public void testNewGame() throws Exception {
        GameSession gameSession = createNewGame(1, 2);
        JSONObject json = new JSONObject();
        json.put("firstHealth", 20);
        json.put("secondHealth", 20);
        json.put("cardAmount", 15);
        json.put("firstName", "admin");
        json.put("secondName", "blabla");
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

    @Test
    public void testTryQueueWithYourself() throws Exception{
        GameSession gameSession = createNewGame(1, 1);
        assertNull(gameSession);
    }

    @Test
    public void testStartTwoGames() throws Exception {
        createNewGame(1, 2);
        GameSession gameSession = createNewGame(4, 5);
        JSONObject json = new JSONObject();
        json.put("firstHealth", 20);
        json.put("secondHealth", 20);
        json.put("cardAmount", 15);
        json.put("firstName", "Player1");
        json.put("secondName", "Player2");
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

}