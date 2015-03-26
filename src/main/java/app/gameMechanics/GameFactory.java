package app.GameMechanics;

import DAO.logic.UserLogic;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import util.LogFactory;

import java.util.HashSet;

/**
 * Created by andreybondar on 25.03.15.
 */
public class GameFactory {
    //private HashMap<Integer, GameLobby> gameLobbyHashMap = new java.util.HashMap<>();
    //private int idCounter;
    private Player firstPlayer;
    private Player secondPlayer;
    private static GameFactory GameFinder = null;
    private GameSessionStorage gameSessionStorage;
    private HashSet<Integer> playersSet;

    private GameFactory() {
        gameSessionStorage = new GameSessionStorage();
        playersSet = new HashSet<>();
    }

    public static GameFactory getInstance() {
        if (GameFinder == null)
        {
            GameFinder = new GameFactory();
        }
        return GameFinder;
    }

    public void FindGameLobby(UserLogic user) {
        if (firstPlayer == null) {
            if (playersSet.contains(user.getId())) {
                LogFactory.getInstance().getApiLogger().error("Illegal try to search 2 games at once");
            } else {
                firstPlayer = new Player(user);
                playersSet.add(user.getId());
            }
        }
        else {
            if (playersSet.contains(user.getId())) {
                LogFactory.getInstance().getApiLogger().error("Illegal try to search 2 games at once");

            } else {
                secondPlayer = new Player(user);
                playersSet.add(user.getId());
                int gameID = gameSessionStorage.newGameSession(firstPlayer, secondPlayer);
                firstPlayer = null;
                secondPlayer = null;
            }
        }
    }

}