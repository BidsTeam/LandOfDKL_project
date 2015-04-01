package app.GameMechanics;

import DAO.logic.UserLogic;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
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

    private GameFactory() {
        gameSessionStorage = new GameSessionStorage();
        inGameUsers = new HashSet<>();
    }

    public static GameFactory getInstance() {
        if (GameFinder == null)
        {
            GameFinder = new GameFactory();
        }
        return GameFinder;
    }

    public void FindGameLobby(UserLogic user, WebSocketService webSocketService) {
        if (firstPlayer == null) {
            if (inGameUsers.contains(user.getId())) {
                LogFactory.getInstance().getApiLogger().error("Illegal try to search 2 games at once");
            } else {
                firstPlayer = new Player(user);
                inGameUsers.add(user.getId());
            }
        } else {
            if (inGameUsers.contains(user.getId())) {
                LogFactory.getInstance().getApiLogger().error("Illegal try to search 2 games at once");

            } else {
                secondPlayer = new Player(user);
                inGameUsers.add(user.getId());
                int gameID = gameSessionStorage.newGameSession(firstPlayer, secondPlayer, webSocketService);
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

}