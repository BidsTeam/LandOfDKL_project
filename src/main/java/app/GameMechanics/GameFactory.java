package app.GameMechanics;

import DAO.logic.UserLogic;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import service.DBService;
import util.LogFactory;

import java.util.HashSet;


public class GameFactory {
    //private HashMap<Integer, GameLobby> gameLobbyHashMap = new java.util.HashMap<>();
    //private int idCounter;
    private Player firstPlayer;
    private Player secondPlayer;
    private static GameFactory GameFinder = null;
    private GameSessionStorage gameSessionStorage;
    private HashSet<Integer> inGameUsers;
    private static boolean isInitialized = false;
    private DBService dbService;

    private GameFactory() {
        gameSessionStorage = new GameSessionStorage();
        inGameUsers = new HashSet<>();
    }

    //Есть ли лучше идея как запустить базу данных на синглтоне?
    public static void initialize(DBService dbService) {
        GameFinder = new GameFactory();
        GameFinder.dbService = dbService;
        isInitialized = true;
    }

    //TODO сделать проверку на isInitialized
    public static GameFactory getInstance() {
        if (GameFinder == null)
        {
            GameFinder = new GameFactory();
        }
        return GameFinder;
    }

    public static void deleteGameFactory() {
        GameFinder = null;
    }

    public void FindGameLobby(UserLogic user, WebSocketService webSocketService) {
        if (firstPlayer == null) {
            if (inGameUsers.contains(user.getId())) {
                LogFactory.getInstance().getLogger(this.getClass()).error("Illegal try to search 2 games at once");
            } else {
                firstPlayer = new Player(user);
                inGameUsers.add(user.getId());
            }
        } else {
            if (inGameUsers.contains(user.getId())) {
                LogFactory.getInstance().getLogger(this.getClass()).error("Illegal try to search 2 games at once");
            } else {
                secondPlayer = new Player(user);
                inGameUsers.add(user.getId());
                gameSessionStorage.newGameSession(firstPlayer, secondPlayer, webSocketService, dbService);
                firstPlayer = null;
                secondPlayer = null;
            }
        }
    }

    public void freePlayer(int userID) {
        inGameUsers.remove(userID);
    }

    public GameSession getGameSession(int id) {
        return gameSessionStorage.getGameSessionByID(id);
    }

    public GameSession getLastGame() { return gameSessionStorage.getLastGame(); }
}