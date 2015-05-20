package app.GameMechanics;

import DAO.logic.UserLogic;
import SocketService.MockSocketService;
import TestSetups.TestsCore;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;
import util.HibernateUtil;

import static org.junit.Assert.*;

public class GameFactoryTest extends TestsCore {
    DBService dbService = new DBServiceImpl(sessionFactory);
    GameFactory gameFactory;
    WebSocketService webSocketService = new MockSocketService();


    private GameSession createNewGame(int firstID, int secondID) {
        UserLogic userOne = dbService.getUserService(dbService.getSession()).getUserById(firstID);
        UserLogic userTwo = dbService.getUserService(dbService.getSession()).getUserById(secondID);
        gameFactory.FindGameLobby(userOne, webSocketService);
        gameFactory.FindGameLobby(userTwo, webSocketService);
        return gameFactory.getLastGame();
    }

    @Before
    public void beforeNewGame() {
        GameFactory.initialize(dbService);
        gameFactory = GameFactory.getInstance();
    }

    @Test
    public void testNewGame() {
        GameSession gameSession = createNewGame(1, 2);
        JSONObject json = new JSONObject();
        json.put("firstHealth", 20);
        json.put("secondHealth", 20);
        json.put("cardAmount", 15);
        json.put("firstName", "admin");
        json.put("secondName", "blabla");
        assertEquals(json.toString(), gameSession.reportGameState().toString());
    }

    @After
    public void afterNewGame() {
        GameFactory.deleteGameFactory();
    }

    @Before
    public void beforeTryQueueWithYourself() {
        GameFactory.initialize(dbService);
        gameFactory = GameFactory.getInstance();
    }

    @Test
    public void testTryQueueWithYourself() {
        GameSession gameSession = createNewGame(1, 1);
        assertNull(gameSession);
    }

    @After
    public void afterTryQueueWithYourself() {
        GameFactory.deleteGameFactory();
    }

    @Before
    public void beforeStartTwoGames() {
        GameFactory.initialize(dbService);
        gameFactory = GameFactory.getInstance();
    }

    @Test
    public void testStartTwoGames() {
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

    @After
    public void afterStartTwoGames() {
        GameFactory.deleteGameFactory();
    }

}