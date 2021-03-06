package app.GameMechanics;

import DAO.logic.UserLogic;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;

import java.util.HashMap;
import java.util.HashSet;


public class GameFactory {
    //private HashMap<Integer, GameLobby> gameLobbyHashMap = new java.util.HashMap<>();
    //private int idCounter;
    private Player firstPlayer;
    private Player secondPlayer;
    //private static GameFactory GameFinder = null;
    private GameSessionStorage gameSessionStorage;
    private HashSet<Integer> inGameUsers;
    private static boolean isInitialized = false;
    private DBService dbService;
    private HashMap<Integer, Integer> playersToGameMap;
    private DeckBuilder deckBuilder;

//    private GameFactory() {
//        gameSessionStorage = new GameSessionStorage();
//        inGameUsers = new HashSet<>();
//        playersToGameMap = new HashMap<>();
//    }
//
//    //Есть ли лучше идея как запустить базу данных на синглтоне?
//    public static void initialize(DBService dbService) {
//        GameFinder = new GameFactory();
//        GameFinder.dbService = dbService;
//        isInitialized = true;
//    }
//
//    //TODO сделать проверку на isInitialized
//    public static GameFactory getInstance() {
//        if (GameFinder == null)
//        {
//            GameFinder = new GameFactory();
//        }
//        return GameFinder;
//    }

    public GameFactory(DBService dbService) {
        gameSessionStorage = new GameSessionStorage();
        inGameUsers = new HashSet<>();
        playersToGameMap = new HashMap<>();
        this.dbService = dbService;
        deckBuilder = new DeckBuilder(dbService);
    }

//    public static void deleteGameFactory() {
//        GameFinder = null;
//    }

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
                int gameId = gameSessionStorage.newGameSession(firstPlayer, secondPlayer, webSocketService, dbService);
                playersToGameMap.put(firstPlayer.getUserID(), gameId);
                playersToGameMap.put(secondPlayer.getUserID(), gameId);
                firstPlayer = null;
                secondPlayer = null;
            }
        }
    }

    public void exitQueue(int userID) {
        if (firstPlayer != null) {
            if (firstPlayer.getUserID() == userID) {
                freePlayer(userID);
                firstPlayer = null;
            }
        } else {
            LogFactory.getInstance().getLogger(this.getClass()).error("Trying to exit queue while not queued from player " + userID);
        }
    }

    public void freePlayer(int userID) {
        inGameUsers.remove(userID);
        playersToGameMap.remove(userID);
    }

    public GameSession getGameSession(int id) {
        return gameSessionStorage.getGameSessionByID(id);
    }

    public GameSession getLastGame() { return gameSessionStorage.getLastGame(); }

    public void buildDeck(int userID, JSONArray jsonArray, WebSocketService webSocketService) {
        if (deckBuilder.buildDeck(userID, jsonArray)) {
            webSocketService.notifyGoodDeck(userID);
        } else {
            webSocketService.notifyBadDeck(userID);
        }
    }

    public int getUserGame(int userID) {
        if (inGameUsers.contains(userID)) {
            return playersToGameMap.get(userID);
        }
        return 0;
    }
}